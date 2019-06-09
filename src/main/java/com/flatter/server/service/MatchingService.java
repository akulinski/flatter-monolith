package com.flatter.server.service;

import com.flatter.server.domain.Offer;
import com.flatter.server.domain.Questionnaire;
import com.flatter.server.domain.User;
import com.flatter.server.repository.OfferRepository;
import com.flatter.server.repository.QuestionnaireRepository;
import com.flatter.server.web.feign.FeignMatchClient;
import domain.QuestionnaireableOffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MatchingService {

    private final FeignMatchClient feignMatchClient;

    private SecureRandom secureRandom;

    private final OfferRepository offerRepository;

    private final QuestionnaireRepository questionnaireRepository;

    @Autowired
    public MatchingService(
        OfferRepository offerRepository,
        FeignMatchClient feignMatchClient, QuestionnaireRepository questionnaireRepository) {
        this.offerRepository = offerRepository;
        this.secureRandom = new SecureRandom();
        this.feignMatchClient = feignMatchClient;
        this.questionnaireRepository = questionnaireRepository;
    }

    public List<Offer> getMockOffers() {

        int limit = secureRandom.nextInt(10);
        ArrayList<Offer> offers = (ArrayList<Offer>) offerRepository.findAll();

        LinkedList<Offer> returnOffers = new LinkedList<>();

        for (int i = 0; i < limit; i++) {
            returnOffers.add(offers.get(secureRandom.nextInt(offers.size())));
        }

        return returnOffers;
    }

    public List<QuestionnaireableOffer> getOffersOfUser(User user) {
        return feignMatchClient.getMatchesOfUser(user.getLogin());
    }


    public List<Offer> getTopViewedOffersInUsersCity(User user) {
        Questionnaire questionnaire = questionnaireRepository.findByUser(user).orElseThrow(() -> new IllegalArgumentException("No questionnaire found for user"));

        List<Offer> offers;

        try {
            offers = offerRepository.findByAddress_City(questionnaire.getCity()).
                stream().sorted(Comparator.comparing(Offer::getViews)
                .reversed()).collect(Collectors.toList()).subList(0, 3);
        } catch (IndexOutOfBoundsException ex) {
            log.error(ex.getMessage());
            offers = offerRepository.findByAddress_City(questionnaire.getCity()).
                stream().sorted(Comparator.comparing(Offer::getViews)
                .reversed()).collect(Collectors.toList());
        }

        return offers;
    }

}
