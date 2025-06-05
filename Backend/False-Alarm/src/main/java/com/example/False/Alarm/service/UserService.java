package com.example.False.Alarm.service;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.dto.LoginRequest;
import com.example.False.Alarm.enums.UserType;
import com.example.False.Alarm.mapper.UserMapper;
import com.example.False.Alarm.model.Conversation;
import com.example.False.Alarm.model.Match;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.repository.ConversationRepository;
import com.example.False.Alarm.repository.MatchRepository;
import com.example.False.Alarm.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    ConversationRepository conversationRepository;

    // Add user (signup)
    public ResponseEntity<?> addUser(String path, AddUserRequest addUserRequest) throws IOException {
        MultipartFile image = addUserRequest.getImage();
        String response = uploadImage(path, image);
        if (response.equals("The uploaded file format is not supported")) {
            return new ResponseEntity<>("The uploaded file format is not supported", HttpStatus.BAD_REQUEST);
        }

        User user = UserMapper.mapToUser(addUserRequest);
        user.setProfilePicUrl(response);
        userRepository.save(user);
        log.info("User added successfully");
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // Image upload helper
    private String uploadImage(String path, MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!extension.equals("png") && !extension.equals("jpg") && !extension.equals("jpeg")) {
            log.info("File extension not supported");
            return "The uploaded file format is not supported";
        }

        String randomID = UUID.randomUUID().toString();
        String fileNameWhenStored = randomID.concat(fileName.substring(fileName.lastIndexOf(".")));
        log.info("File name when stored is {}", fileNameWhenStored);
        String filePath = path + File.separator + fileNameWhenStored;
        log.info("File name saved: " + filePath);
        File dest = new File(path);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));
        log.info("Image Uploaded Successfully");
        return fileNameWhenStored;
    }

    // Add admin (for special admin creation)
    public User addAdmin(@Valid AddUserRequest addUserRequest) {
        User user = UserMapper.mapToUser(addUserRequest);
        user.setUserType(UserType.ADMIN);
        user.setAuthorities("ADMIN");
        return userRepository.save(user);
    }

    // Search by username
    public List<User> searchByUsername(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    // Search by userId
    public List<User> searchByUserId(String query) {
        return userRepository.findByUserIdContainingIgnoreCase(query);
    }

    // Spring Security user details loader
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user;
        }
        throw new UsernameNotFoundException(userId.concat(" does not exist"));
    }

    // Login method for authentication
    public ResponseEntity<?> login(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }
        // For real applications, use password encoder!
        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        if (!user.getUserType().name().equalsIgnoreCase(request.getRole())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Role mismatch");
        }
        return ResponseEntity.ok("Login successful");
    }

    // Send invite
    public ResponseEntity<String> sendInvite(String senderId, String receiverId) {
        User sender = userRepository.findByUserId(senderId);
        User receiver = userRepository.findByUserId(receiverId);
        if (sender == null || receiver == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Match match = Match.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        matchRepository.save(match);
        return new ResponseEntity<>("Invite sent successfully", HttpStatus.OK);
    }

    // Accept invite
    public ResponseEntity<String> acceptInvite(String matchId) {
        Match match = matchRepository.findById(matchId).orElse(null);
        if (match == null) {
            return new ResponseEntity<>("Invite not found", HttpStatus.NOT_FOUND);
        }

        Conversation conversation = Conversation.builder()
                .user(match.getSender())
                .build();

        conversation = conversationRepository.save(conversation);
        match.setConversation(conversation);

        matchRepository.save(match);
        return new ResponseEntity<>("Invite accepted.", HttpStatus.OK);
    }

    // Reject invite
    public ResponseEntity<String> rejectInvite(String matchId) {
        if (!matchRepository.existsById(matchId)) {
            return new ResponseEntity<>("Invite not found", HttpStatus.NOT_FOUND);
        }

        matchRepository.deleteById(matchId);
        return new ResponseEntity<>("Invite rejected", HttpStatus.OK);
    }

    // Get sent invites
    public List<User> getSentInvites(String senderUserId) {
        User sender = userRepository.findByUserId(senderUserId);

        List<Match> sentMatches = matchRepository.findBySender(sender);
        return sentMatches.stream()
                .map(Match::getReceiver)
                .collect(Collectors.toList());
    }

    // Get received invites
    public List<User> getReceivedInvites(String receiverUserId) {
        User receiver = userRepository.findByUserId(receiverUserId);

        List<Match> receivedMatches = matchRepository.findByReceiver(receiver);
        return receivedMatches.stream()
                .map(Match::getSender)
                .collect(Collectors.toList());
    }
}
