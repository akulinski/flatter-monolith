package com.flatter.server.domain.elastic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonSubTypes.Type(domain.QuestionnaireableOffer.class)
public class QuestionnaireableOffer extends Questionnaireable implements Serializable {
    private String description;

    public QuestionnaireableOffer() {
        name = "offer";
    }
}
