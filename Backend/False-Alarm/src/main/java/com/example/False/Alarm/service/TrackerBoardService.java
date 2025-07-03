package com.example.False.Alarm.service;

import com.example.False.Alarm.enums.ObservationStatus;
import com.example.False.Alarm.model.FlaggedUserDetails;
import com.example.False.Alarm.model.User;
import com.example.False.Alarm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TrackerBoardService {

    @Autowired
    private ChatMonitorService chatMonitorService;

    @Autowired
    private UserRepository userRepository;

    public List<FlaggedUserDetails> getFlaggedUsersUnderAdminWatch() {
        List<FlaggedUserDetails> all = chatMonitorService.getFlaggedUsers();

        // Ensure only OBSERVED users are shown
        List<String> userIds = all.stream()
                .map(FlaggedUserDetails::getUserId)
                .collect(Collectors.toList());
        Map<String, ObservationStatus> statusMap = userRepository.findAll().stream()
                .filter(user -> userIds.contains(user.getUserId()))
                .collect(Collectors.toMap(User::getUserId, User::getObservationStatus));

        return all.stream()
                .filter(fu -> statusMap.getOrDefault(fu.getUserId(), ObservationStatus.NOT_OBSERVED)
                        .equals(ObservationStatus.OBSERVED))
                .collect(Collectors.toList());
    }
}
