package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.dto.LoginRequest;
import com.example.False.Alarm.mapper.UserMapper;
import com.example.False.Alarm.enums.UserType;
import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.model.Conversation;
import com.example.False.Alarm.model.Match;
import com.example.False.Alarm.enums.MatchStatus;
import com.example.False.Alarm.repository.UserRepository;
import com.example.False.Alarm.repository.MatchRepository;
import com.example.False.Alarm.repository.ConversationRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public ResponseEntity<?> addUser(String path, AddUserRequest addUserRequest) throws IOException {
        MultipartFile image = addUserRequest.getImage();
        String response = uploadImage(path, image);
        if ("The uploaded file format is not supported".equals(response)) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = UserMapper.mapToUser(addUserRequest);
        user.setObservationStatus(ObservationStatus.OBSERVED);
        userRepository.save(user);
        log.info("User added successfully");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    private String uploadImage(String path, MultipartFile image) throws IOException {
        String fileName = Objects.requireNonNull(image.getOriginalFilename());
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        if (!Arrays.asList("png", "jpg", "jpeg").contains(extension)) {
            log.info("File extension not supported");
            return "The uploaded file format is not supported";
        }

        String fileNameWhenStored = UUID.randomUUID() + fileName.substring(fileName.lastIndexOf('.'));
        String filePath = path + File.separator + fileNameWhenStored;

        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create directory: " + path);
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));
        log.info("Image Uploaded Successfully: {}", filePath);
        return fileNameWhenStored;
    }

    public User addAdmin(@Valid AddUserRequest addUserRequest) {
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(Optional.ofNullable(addUserRequest.getRole())
                .map(String::toUpperCase)
                .map(UserType::valueOf)
                .orElse(UserType.ADMIN));
        user.setObservationStatus(ObservationStatus.OBSERVED);
        return userRepository.save(user);
    }

    public List<User> searchByUsername(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    public List<User> searchByUserId(String query) {
        return userRepository.findByUserIdContainingIgnoreCase(query);
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userId + " does not exist"));
    }

    public ResponseEntity<?> login(LoginRequest request) {

        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }

        if (!user.getUserType().name().equalsIgnoreCase(request.getRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Role mismatch");
        }

        return ResponseEntity.ok("Login successful");
    }

    public ResponseEntity<String> sendInvite(String senderId, String receiverId) {
        User sender = userRepository.findByUserId(senderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));
        User receiver = userRepository.findByUserId(receiverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        if (matchRepository.findBySenderAndReceiver(sender, receiver).isPresent()) {
            return new ResponseEntity<>("Invite already sent", HttpStatus.BAD_REQUEST);
        }

        Match match = new Match();
        match.setSender(sender);
        match.setReceiver(receiver);
        match.setStatus(MatchStatus.PENDING);
        matchRepository.save(match);

        return new ResponseEntity<>("Invite sent successfully", HttpStatus.OK);
    }

    public ResponseEntity<String> acceptInvite(Long senderId, String receiverUsername) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        Match match = matchRepository.findBySenderIdAndReceiverId(senderId, Long.valueOf(receiver.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No invite found from sender"));

        if (MatchStatus.ACCEPTED.equals(match.getStatus())) {
            return new ResponseEntity<>("Already accepted", HttpStatus.BAD_REQUEST);
        }

        match.setStatus(MatchStatus.ACCEPTED);
        Conversation conversation = new Conversation();
        conversation.setUser(match.getSender());
        conversation = conversationRepository.save(conversation);
        match.setConversation(conversation);
        matchRepository.save(match);

        return new ResponseEntity<>("Invite accepted", HttpStatus.OK);
    }

    public ResponseEntity<String> rejectInvite(Long senderId, String receiverUsername) {
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        Match match = matchRepository.findBySenderIdAndReceiverId(senderId, Long.valueOf(receiver.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No invite found from sender"));

        matchRepository.delete(match);
        return new ResponseEntity<>("Invite rejected", HttpStatus.OK);
    }

    public List<User> getSentInvites(String senderUserId) {
        User sender = userRepository.findByUserId(senderUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found"));

        return matchRepository.findBySenderAndStatus(sender, MatchStatus.PENDING)
                .stream().map(Match::getReceiver).distinct().collect(Collectors.toList());
    }

    public List<User> getReceivedInvites(String receiverUserId) {
        User receiver = userRepository.findByUserId(receiverUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found"));

        return matchRepository.findByReceiverAndStatus(receiver, MatchStatus.PENDING)
                .stream().map(Match::getSender).distinct().collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}