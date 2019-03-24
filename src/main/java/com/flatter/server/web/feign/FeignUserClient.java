package com.flatter.server.web.feign;

import com.flatter.server.config.ClientConfiguration;
import com.flatter.server.domain.Questionnaire;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CLUSTERINGSERVICE",configuration = ClientConfiguration.class)
public interface FeignUserClient {

    @PostMapping("/api/createUser")
    @Headers("Content-Type: application/json")
    ResponseEntity addUser(@RequestBody Questionnaire questionnaire);
}
