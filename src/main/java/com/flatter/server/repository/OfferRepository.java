package com.flatter.server.repository;

import com.flatter.server.domain.Offer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Offer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("select offer from Offer offer where offer.user.login = ?#{principal.username}")
    List<Offer> findByUserIsCurrentUser();

}
