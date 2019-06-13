package com.flatter.server.service;

import com.flatter.server.domain.Offer;
import com.flatter.server.domain.Questionnaire;
import com.flatter.server.domain.User;
import com.flatter.server.repository.OfferRepository;
import com.flatter.server.repository.QuestionnaireRepository;
import com.flatter.server.web.feign.FeignMatchClient;
import com.google.common.cache.Cache;
import domain.Questionnaireable;
import domain.QuestionnaireableOffer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MatchingService {

    private final FeignMatchClient feignMatchClient;

    private SecureRandom secureRandom;

    private final OfferRepository offerRepository;

    private final QuestionnaireRepository questionnaireRepository;

    private final Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache;

    private final Cache<String, Questionnaireable> loginQuestionnaireableCache;

    @Autowired
    public MatchingService(
        OfferRepository offerRepository,
        FeignMatchClient feignMatchClient, QuestionnaireRepository questionnaireRepository, Cache<Questionnaireable,
        CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache, Cache<String, Questionnaireable> loginQuestionnaireableCache) {
        this.offerRepository = offerRepository;
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
        this.loginQuestionnaireableCache = loginQuestionnaireableCache;
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


    public Stream<Map.Entry<Questionnaireable, Double>> getSortedMatches(String login) {

        Questionnaireable questionnaireable = loginQuestionnaireableCache.getIfPresent(login);

        CentroidCluster<Questionnaireable> questionnaireableCentroidCluster = questionnaireableCentroidClusterCache.getIfPresent(questionnaireable);

        HashMap<Questionnaireable, Double> differnceBettwenPointAndCurrentUser = new HashMap<>();

        final List<Questionnaireable> offers = questionnaireableCentroidCluster.getPoints().stream().filter(questionnaireable1 -> questionnaireable1.getName().equals("offer")).collect(Collectors.toList());

        offers.forEach(offer -> {
            try {
                differnceBettwenPointAndCurrentUser.put(offer, questionnaireable.getSumOfPoints() - offer.getSumOfPoints());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });

        return differnceBettwenPointAndCurrentUser.entrySet().stream()
            .sorted(Map.Entry.comparingByValue());
    }
}
