package com.flatter.server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import domain.Questionnaireable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Document(indexName = "clustering_index", type = "Clusterable")
@Data
@NoArgsConstructor
public class ClusteringDocument {

    @Id
    @JsonProperty
    private String documentId;

    @NotNull
    @JsonProperty
    @JsonDeserialize()
    private Questionnaireable questionnaireable;

    @CreatedDate
    @JsonProperty
    private Date dateOfCreation;

    @LastModifiedDate
    @JsonProperty
    private Date dateOfModification;

}
