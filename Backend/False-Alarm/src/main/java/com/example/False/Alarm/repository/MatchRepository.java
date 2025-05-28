package com.example.False.Alarm.repository;

import com.example.False.Alarm.model.Match;
import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, String> {
    // Get all invites sent by a user
    List<Match> findBySender(User sender);

    // Get all invites received by a user
    List<Match> findByReceiver(User receiver);

}
