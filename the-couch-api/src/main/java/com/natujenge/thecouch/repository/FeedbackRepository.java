package com.natujenge.thecouch.repository;

import com.natujenge.thecouch.domain.Feedback;
import com.natujenge.thecouch.web.rest.dto.FeedbackDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long> {


    Optional<FeedbackDto> findBySessionIdAndCoachId(Long sessionId, Long coachId);

    Optional<FeedbackDto> findByClientIdAndCoachId(Long clientId, Long coachId);

    List<FeedbackDto> findBySessionId(Long sessionId);

}