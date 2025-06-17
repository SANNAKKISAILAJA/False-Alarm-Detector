package com.example.False.Alarm.repository;

import com.example.False.Alarm.enums.MatchStatus;
import com.example.False.Alarm.model.Match;
import com.example.False.Alarm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, String> {

    Optional<Match> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    Optional<Match> findBySenderAndReceiver(User sender, User sender1);

    List<Match> findByReceiverAndStatus(User receiver, MatchStatus status);

    List<Match> findBySenderAndStatus(User sender, MatchStatus status);
}
