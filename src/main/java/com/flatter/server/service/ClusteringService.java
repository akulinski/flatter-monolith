package com.flatter.server.service;

import com.flatter.server.domain.ClusterEvent;
import com.flatter.server.domain.ClusteringDocument;
import com.flatter.server.repository.elastic.ClusteringDocumentRepository;
import com.google.common.cache.Cache;
import domain.Questionnaireable;
import domain.QuestionnaireableUser;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import utils.ClusteringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.math3.util.FastMath.toIntExact;

@Service
@Log4j2
public class ClusteringService {

    private final ClusteringDocumentRepository clusteringDocumentRepository;

    private final Cache<String, Questionnaireable> loginQuestionnaireableCache;

    private final Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache;


    @Autowired
    public ClusteringService(ClusteringDocumentRepository clusteringDocumentRepository, Cache<String, Questionnaireable> stringQuestionnaireableCache, Cache<Questionnaireable, CentroidCluster<Questionnaireable>> questionnaireableCentroidClusterCache) {
        this.clusteringDocumentRepository = clusteringDocumentRepository;
        this.loginQuestionnaireableCache = stringQuestionnaireableCache;
        this.questionnaireableCentroidClusterCache = questionnaireableCentroidClusterCache;
    }

    @EventListener({ApplicationReadyEvent.class, ClusterEvent.class})
    @Async
    public void clusterData() throws IOException, IllegalAccessException, InterruptedException {
        while (true) {
            Iterable<ClusteringDocument> documentRepositoryAll = clusteringDocumentRepository.findAll();
            LinkedList<Questionnaireable> questionnaireableLinkedList = new LinkedList<>();

            documentRepositoryAll.forEach(clusteringDocument -> questionnaireableLinkedList.add(clusteringDocument.getQuestionnaireable()));

            int count = toIntExact(clusteringDocumentRepository.count() * 10);

            KMeansPlusPlusClusterer<Questionnaireable> clusterer = new KMeansPlusPlusClusterer<>(clusteringDocumentRepository.countAllByQuestionnaireable_Name("offer"), count);

            List<CentroidCluster<Questionnaireable>> clusterResults = clusterer.cluster(questionnaireableLinkedList);

            FileWriter out = new FileWriter("data/data" + new Date().toString() + ".csv");

            invalideAllCaches();

            tryToWriteResultsToFile(clusterResults, out);

            out.close();
            Thread.sleep(10000);
        }
    }

    private void tryToWriteResultsToFile(List<CentroidCluster<Questionnaireable>> clusterResults, FileWriter out) throws IOException, IllegalAccessException {
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(ClusteringUtils.HEEADERS))) {
            for (int i = 0; i < clusterResults.size(); i++) {
                wirteToCSV(clusterResults, printer, i);
            }
        }
    }

    private void invalideAllCaches() {
        loginQuestionnaireableCache.invalidateAll();
        questionnaireableCentroidClusterCache.invalidateAll();
    }

    private void wirteToCSV(List<CentroidCluster<Questionnaireable>> clusterResults, CSVPrinter printer, int i) throws IOException, IllegalAccessException {
        for (Questionnaireable questionnaireable : clusterResults.get(i).getPoints()) {

            questionnaireableCentroidClusterCache.put(questionnaireable, clusterResults.get(i));

            checkIfUserAndCacheHim(questionnaireable);
            printer.printRecord(i, questionnaireable.getData());
        }
    }

    private void checkIfUserAndCacheHim(Questionnaireable questionnaireable) {
        if ("user".equals(questionnaireable.getName())) {
            QuestionnaireableUser questionnaireableUser = (QuestionnaireableUser) questionnaireable;
            loginQuestionnaireableCache.put(questionnaireableUser.getLogin(), questionnaireable);
        }
    }
}
