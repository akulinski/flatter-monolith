package com.flatter.server.web.feign;

import com.flatter.server.config.ClientConfiguration;
import domain.QuestionnaireableOffer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@FeignClient(name = "CLUSTERINGSERVICE", configuration = ClientConfiguration.class)
public interface FeignMatchClient {

    @GetMapping(value = "/api/get-matches/{login}")
    ArrayList<QuestionnaireableOffer> getMatchesOfUser(@PathVariable(name = "login") String login);
}
