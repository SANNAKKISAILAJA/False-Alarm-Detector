package com.example.False.Alarm.controller;

import com.example.False.Alarm.dto.AddUserRequest;
import com.example.False.Alarm.dto.UserSearchDTO;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.service.ChatMonitorService;
import com.example.False.Alarm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import com.example.False.Alarm.dto.TextInput;
import com.example.False.Alarm.dto.Response;
import com.example.False.Alarm.dto.LoginRequest;
import com.example.False.Alarm.model.FlaggedUserDetails;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ChatMonitorService chatMonitorService;

    @Value("${project.image}")
    private String path;

    // New endpoint: Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@ModelAttribute @Valid AddUserRequest addUserRequest) throws IOException {
        ResponseEntity<?> response = userService.addUser(path, addUserRequest);
        return response;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/admin")
    public ResponseEntity<User> addAdmin(@RequestBody @Valid AddUserRequest addUserRequest) {
        User addedUser = userService.addAdmin(addUserRequest);
        return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/chat/{userId}")
    public ResponseEntity<List<String>> checkChatMessage(@PathVariable String userId, @RequestBody String message) {
        if (chatMonitorService.isBlocked(userId)) {
            return ResponseEntity.ok(List.of("🚫 You are blocked. Type '/reset' to clear warnings."));
        }
        // Get user details (username, location) from userService or session
        User user = userService.getUserById(userId);
        String username = user != null ? user.getUsername() : "Unknown";
        String location = user != null ? user.getLocation() : "Unknown";
        return ResponseEntity.ok(chatMonitorService.checkMessage(userId, username, message, location));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/chat/reset/{userId}")
    public ResponseEntity<String> resetChatStatus(@PathVariable String userId) {
        return ResponseEntity.ok(chatMonitorService.resetCounts(userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("query") String query) {
        List<User> usersByName = userService.searchByUsername(query);
        List<User> usersById = userService.searchByUserId(query);

        Set<User> combined = new LinkedHashSet<>();
        combined.addAll(usersByName);
        combined.addAll(usersById);

        return ResponseEntity.ok(new ArrayList<>(combined));
    }

    @PostMapping("/invite/{senderId}/{receiverId}")
    public ResponseEntity<String> sendInvite(@PathVariable String senderId, @PathVariable String receiverId) {
        return userService.sendInvite(senderId, receiverId);
    }

    @PostMapping("/invite/accept/{matchId}")
    public ResponseEntity<String> acceptInvite(@PathVariable String matchId) {
        return userService.acceptInvite(matchId);
    }

    @DeleteMapping("/invite/reject/{matchId}")
    public ResponseEntity<String> rejectInvite(@PathVariable String matchId) {
        return userService.rejectInvite(matchId);
    }

    @GetMapping("/invites/sent/{senderUserId}")
    public ResponseEntity<List<User>> getSentInvites(@PathVariable String senderUserId) {
        return ResponseEntity.ok(userService.getSentInvites(senderUserId));
    }

    @GetMapping("/invites/received/{receiverUserId}")
    public ResponseEntity<List<User>> getReceivedInvites(@PathVariable String receiverUserId) {
        return ResponseEntity.ok(userService.getReceivedInvites(receiverUserId));
    }

    @GetMapping("/flagged-users")
    public ResponseEntity<List<FlaggedUserDetails>> getFlaggedUsers() {
        List<FlaggedUserDetails> flaggedUsers = chatMonitorService.getFlaggedUsers();
        return ResponseEntity.ok(flaggedUsers);
    }
}
