package com.flatter.server.config;

import com.flatter.server.domain.ClusteringDocument;
import com.flatter.server.repository.elastic.ClusteringDocumentRepository;
import com.github.javafaker.Faker;
import domain.QuestionnaireableOffer;
import domain.QuestionnaireableUser;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Configuration
@Log4j2
public class DataMockConfig {

    @Value("${datamock.count.user}")
    private Integer userCount;

    @Value("${datamock.count.flat}")
    private Integer flatCount;

    private final Environment environment;

    private final ClusteringDocumentRepository clusteringDocumentRepository;

    private final Faker faker;

    @Autowired
    public DataMockConfig(Environment environment, ClusteringDocumentRepository clusteringDocumentRepository, Faker faker) {
        this.environment = environment;
        this.clusteringDocumentRepository = clusteringDocumentRepository;
        this.faker = faker;
    }

    private void initDataInElastic() {
        createUsers();
        createOffers();

    }

    //public for @Async annotation
    @Async
    public void createOffers() {
        for (int i = 0; i < flatCount; i++) {
            ClusteringDocument clusteringDocument = new ClusteringDocument();
            QuestionnaireableOffer questionnaireableOffer = new QuestionnaireableOffer();
            setUpOffer(questionnaireableOffer);
            clusteringDocument.setQuestionnaireable(questionnaireableOffer);
            clusteringDocumentRepository.save(clusteringDocument);
            log.debug("Adding document with offer: " + clusteringDocument.toString());
        }
    }

    //public for @Async annotation
    @Async
    public void createUsers() {
        for (int i = 0; i < userCount; i++) {
            ClusteringDocument clusteringDocument = new ClusteringDocument();
            QuestionnaireableUser questionnaireableUser = new QuestionnaireableUser();
            setUpUser(questionnaireableUser);
            clusteringDocument.setQuestionnaireable(questionnaireableUser);
            clusteringDocumentRepository.save(clusteringDocument);
            log.debug("Adding document with user: " + clusteringDocument.toString());
        }
    }

    private void setUpUser(QuestionnaireableUser questionnaireableUser) {
        questionnaireableUser.setConstructionYearMax(getCurrentYear());
        questionnaireableUser.setConstructionYearMin(faker.number().numberBetween(1980, getCurrentYear()));
        questionnaireableUser.setFurnished(faker.bool().bool());
        questionnaireableUser.setPets(faker.bool().bool());
        questionnaireableUser.setSmokingInside(faker.bool().bool());

        int maxCost = faker.number().numberBetween(0, 10000);
        int minCost = faker.number().numberBetween(0, maxCost);
        questionnaireableUser.setTotalCostMax((double) maxCost);
        questionnaireableUser.setTotalCostMin((double) minCost);

        int maxRooms = faker.number().numberBetween(1, 10);
        int minRooms = faker.number().numberBetween(1, maxRooms);
        questionnaireableUser.setRoomAmountMax(maxRooms);
        questionnaireableUser.setRoomAmountMin(minRooms);

        int sizeMax = faker.number().numberBetween(15, 100);
        int sizeMin = faker.number().numberBetween(15, sizeMax);
        questionnaireableUser.setSizeMax((double) sizeMax);
        questionnaireableUser.setSizeMin((double) sizeMin);

        questionnaireableUser.setEmail(faker.cat().name());
        questionnaireableUser.setFirstName(faker.funnyName().name());
        questionnaireableUser.setLogin(faker.lordOfTheRings().character());
        questionnaireableUser.setLastName(faker.chuckNorris().fact());
        questionnaireableUser.setName("user");
    }

    private void setUpOffer(QuestionnaireableOffer questionnaireableOffer) {
        int year = faker.number().numberBetween(1980, getCurrentYear());
        questionnaireableOffer.setConstructionYearMax(year);
        questionnaireableOffer.setConstructionYearMin(year);
        questionnaireableOffer.setFurnished(faker.bool().bool());
        questionnaireableOffer.setPets(faker.bool().bool());
        questionnaireableOffer.setSmokingInside(faker.bool().bool());

        int cost = faker.number().numberBetween(0, 10000);
        questionnaireableOffer.setTotalCostMax((double) cost);
        questionnaireableOffer.setTotalCostMin((double) cost);

        int rooms = faker.number().numberBetween(1, 10);
        questionnaireableOffer.setRoomAmountMax(rooms);
        questionnaireableOffer.setRoomAmountMin(rooms);

        int size = faker.number().numberBetween(15, 100);
        questionnaireableOffer.setSizeMax((double) size);
        questionnaireableOffer.setSizeMin((double) size);

        questionnaireableOffer.setDescription(faker.gameOfThrones().quote());
        questionnaireableOffer.setName("offer");
    }

    private int getCurrentYear() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }


    @EventListener(ApplicationReadyEvent.class)
    public void initDataIfInDevMode() {
        if (isInDevMode() && clusteringDocumentRepository.count() < 1000) {
            log.debug("Current count is: " + clusteringDocumentRepository.count());
            log.debug("Application running in DEV profile starting init data in elastic");
            initDataInElastic();
        }
    }

    private boolean isInDevMode() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }
}
