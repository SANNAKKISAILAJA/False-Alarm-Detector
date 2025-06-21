package com.example.False.Alarm.controller;

import com.example.False.Alarm.model.FlaggedUserDetails;
import com.example.False.Alarm.service.TrackerBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/trackerboard")
public class TrackerBoardController {

    @Autowired
    private TrackerBoardService trackerBoardService;

    @GetMapping("/flagged")
    public ResponseEntity<List<FlaggedUserDetails>> getFlaggedUsers() {
        return ResponseEntity.ok(trackerBoardService.getFlaggedUsersUnderAdminWatch());
    }
}
