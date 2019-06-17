package com.flatter.server.domain.dto;

import domain.Questionnaireable;
import lombok.Data;

import java.util.LinkedList;

@Data
public class LoadedClusteringDataResponse {
    private String requester;
    private LinkedList<Questionnaireable> questionnaireables;
}
