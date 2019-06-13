package com.flatter.server.repository.elastic;

import com.flatter.server.domain.ClusteringDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusteringDocumentRepository extends ElasticsearchRepository<ClusteringDocument, String> {
    List<ClusteringDocument> findByQuestionnaireable_Name(String name);

    Integer countAllByQuestionnaireable_Name(String name);

}
