package com.flatter.server.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserCheckDTO implements Serializable {
    private Boolean questionnaireFilled;
}
