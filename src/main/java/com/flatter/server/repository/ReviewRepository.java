package com.flatter.server.repository;

import com.flatter.server.domain.Review;
import com.flatter.server.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;


/**
 * Spring Data  repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    LinkedList<Review> getAllByReceiver(User receiver);
}
