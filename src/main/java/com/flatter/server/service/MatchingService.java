package com.flatter.server.service;

import com.flatter.server.domain.Offer;
import com.flatter.server.domain.User;
import com.flatter.server.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class MatchingService {

    private SecureRandom secureRandom;

    private final OfferRepository offerRepository;

    @Autowired
    public MatchingService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
        this.secureRandom = new SecureRandom();
    }

    public List<Offer> getMockOffers(User user) {
        int limit = secureRandom.nextInt(10);
        ArrayList<Offer> offers = (ArrayList<Offer>) offerRepository.findAll();
        LinkedList<Offer> returnOffers = new LinkedList<>();

        for (int i = 0; i < limit; i++) {
            returnOffers.add(offers.get(secureRandom.nextInt(offers.size())));
        }

        return returnOffers;
    }

}
