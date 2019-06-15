package com.flatter.server.repository.elastic;

import com.flatter.server.domain.ClusteringDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ClusteringDocumentRepository extends ElasticsearchRepository<ClusteringDocument, String> {

    Optional<ClusteringDocument> findByQuestionnaireable_User_Login(String user);


    List<ClusteringDocument> findAllByQuestionnaireable_Offer_Address_CityAndQuestionnaireable_SumOfPoints(String city, double sumOfPoints);

}
