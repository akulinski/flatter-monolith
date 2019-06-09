package com.flatter.server.repository;

import com.flatter.server.domain.Questionnaire;
import com.flatter.server.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Questionnaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
    Optional<Questionnaire> findByUser(User user);
}
