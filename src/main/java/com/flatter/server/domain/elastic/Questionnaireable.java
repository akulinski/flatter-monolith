package com.flatter.server.domain.elastic;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.flatter.server.domain.Offer;
import com.flatter.server.domain.User;
import domain.CSVWritable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

import static utils.ClusteringUtils.weightMap;


@Data
@NoArgsConstructor
@Log4j2
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "name")
@JsonSubTypes({
    @JsonSubTypes.Type(value = QuestionnaireableUser.class, name = "user"),

    @JsonSubTypes.Type(value = QuestionnaireableOffer.class, name = "offer")}
)

public abstract class Questionnaireable implements Clusterable, CSVWritable, Serializable, Comparable<Questionnaireable> {

    public static final String OFFER = "offer";
    public static final String USER = "user";

    @JsonProperty
    @JsonTypeId
    @JsonDeserialize
    protected String name;

    @JsonProperty
    private Offer offer;

    @JsonProperty
    private boolean pets;

    @JsonProperty
    private boolean smokingInside;

    @JsonProperty
    private boolean isFurnished;

    @JsonProperty
    private int roomAmountMin;

    @JsonProperty
    private int roomAmountMax;

    @JsonProperty
    private double sizeMin;

    @JsonProperty
    private double sizeMax;

    @JsonProperty
    private int constructionYearMin;

    @JsonProperty
    private int constructionYearMax;

    @JsonProperty
    private String type;

    @JsonProperty
    private double totalCostMin;

    @JsonProperty
    private double totalCostMax;

    @CreatedDate
    private Date dateOfCreation;

    @LastModifiedDate
    private Date dateOfModification;

    @JsonProperty
    private User user;

    @JsonProperty
    private Double sumOfPoints = null;


    @Override
    public double[] getPoint() {
        try {
            return this.getArrayOfPoints();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return new double[0];
    }

    @Override
    public int compareTo(Questionnaireable o) {

        return (int) (this.calcSumOfPoints() - o.calcSumOfPoints());

    }

    public double calcSumOfPoints() {
        double sum = 0;
        for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {
            try {
                if (field != null && !field.getName().equals("sumOfPoints")) {
                    if (checkIfFieldIsBooleanType(field)) {
                        boolean booleanValueOfField = false;
                        booleanValueOfField = field.getBoolean(this);
                        if (booleanValueOfField) {
                            sum += 1 * weightMap.get(field.getName());
                        }
                    } else if (checkIfFiledIsInteger(field)) {
                        sum += field.getInt(this) * weightMap.get(field.getName());

                    } else if (checkIfDouble(field)) {
                        sum += field.getDouble(this) * weightMap.get(field.getName());
                    }
                }
            } catch (IllegalAccessException ex) {
                log.error(ex.getMessage());
            }
        }
        this.sumOfPoints = sum;
        return sum;
    }

    public double[] getArrayOfPoints() throws IllegalAccessException {
        double[] arrayOfPoints = new double[this.getClass().getSuperclass().getDeclaredFields().length];
        int index = 0;
        for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {

            if (checkIfFieldIsBooleanType(field)) {
                boolean booleanValueOfField = field.getBoolean(this);
                if (booleanValueOfField) {
                    arrayOfPoints[index] = 1 * weightMap.get(field.getName());
                    ;
                }
            } else if (checkIfFiledIsInteger(field)) {
                arrayOfPoints[index] = field.getInt(this) * weightMap.get(field.getName());
            } else if (checkIfDouble(field)) {
                arrayOfPoints[index] = field.getDouble(this) * weightMap.get(field.getName());
            }
            index++;
        }
        return arrayOfPoints;
    }


    private static boolean checkIfDouble(Field field) {
        return field.getType() == double.class;
    }

    private static boolean checkIfFiledIsInteger(Field field) {
        return field.getType() == int.class;
    }

    private static boolean checkIfFieldIsBooleanType(Field field) {
        return field.getType() == boolean.class;
    }

    @Override
    public ArrayList<String> getData() throws IllegalAccessException {
        Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
        ArrayList<String> data = new ArrayList<>();

        for (Field field : fields) {

            if (checkIfFieldIsBooleanType(field)) {
                boolean booleanValueOfField = field.getBoolean(this);
                data.add(String.valueOf(booleanValueOfField));
            } else if (checkIfFiledIsInteger(field)) {
                int fieldInt = field.getInt(this);
                data.add(String.valueOf(fieldInt));

            } else if (checkIfDouble(field)) {
                double fieldDouble = field.getDouble(this);
                data.add(String.valueOf(fieldDouble));
            }

        }

        return data;
    }


}
