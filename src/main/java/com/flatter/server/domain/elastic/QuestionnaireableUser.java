package com.flatter.server.domain.elastic;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.Data;

@Data
@JsonSubTypes.Type(QuestionnaireableUser.class)
public class QuestionnaireableUser extends Questionnaireable {

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    public QuestionnaireableUser() {
        name = "user";
    }
}
