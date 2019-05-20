package com.flatter.server.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Questionnaire.
 */
@Entity
@Table(name = "questionnaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "pets")
    private Boolean pets;

    @Column(name = "smoking_inside")
    private Boolean smokingInside;

    @Column(name = "is_furnished")
    private Boolean isFurnished;

    @Column(name = "room_amount_min")
    private Integer roomAmountMin;

    @Column(name = "room_amount_max")
    private Integer roomAmountMax;

    @Column(name = "size_min")
    private Double sizeMin;

    @Column(name = "size_max")
    private Double sizeMax;

    @Column(name = "construction_year_min")
    private Integer constructionYearMin;

    @Column(name = "construction_year_max")
    private Integer constructionYearMax;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "total_cost_min")
    private Double totalCostMin;

    @Column(name = "total_cost_max")
    private Double totalCostMax;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isPets() {
        return pets;
    }

    public Questionnaire pets(Boolean pets) {
        this.pets = pets;
        return this;
    }

    public void setPets(Boolean pets) {
        this.pets = pets;
    }

    public Boolean isSmokingInside() {
        return smokingInside;
    }

    public Questionnaire smokingInside(Boolean smokingInside) {
        this.smokingInside = smokingInside;
        return this;
    }

    public void setSmokingInside(Boolean smokingInside) {
        this.smokingInside = smokingInside;
    }

    public Boolean isIsFurnished() {
        return isFurnished;
    }

    public Questionnaire isFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished;
        return this;
    }

    public void setIsFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished;
    }

    public Integer getRoomAmountMin() {
        return roomAmountMin;
    }

    public Questionnaire roomAmountMin(Integer roomAmountMin) {
        this.roomAmountMin = roomAmountMin;
        return this;
    }

    public void setRoomAmountMin(Integer roomAmountMin) {
        this.roomAmountMin = roomAmountMin;
    }

    public Integer getRoomAmountMax() {
        return roomAmountMax;
    }

    public Questionnaire roomAmountMax(Integer roomAmountMax) {
        this.roomAmountMax = roomAmountMax;
        return this;
    }

    public void setRoomAmountMax(Integer roomAmountMax) {
        this.roomAmountMax = roomAmountMax;
    }

    public Double getSizeMin() {
        return sizeMin;
    }

    public Questionnaire sizeMin(Double sizeMin) {
        this.sizeMin = sizeMin;
        return this;
    }

    public void setSizeMin(Double sizeMin) {
        this.sizeMin = sizeMin;
    }

    public Double getSizeMax() {
        return sizeMax;
    }

    public Questionnaire sizeMax(Double sizeMax) {
        this.sizeMax = sizeMax;
        return this;
    }

    public void setSizeMax(Double sizeMax) {
        this.sizeMax = sizeMax;
    }

    public Integer getConstructionYearMin() {
        return constructionYearMin;
    }

    public Questionnaire constructionYearMin(Integer constructionYearMin) {
        this.constructionYearMin = constructionYearMin;
        return this;
    }

    public void setConstructionYearMin(Integer constructionYearMin) {
        this.constructionYearMin = constructionYearMin;
    }

    public Integer getConstructionYearMax() {
        return constructionYearMax;
    }

    public Questionnaire constructionYearMax(Integer constructionYearMax) {
        this.constructionYearMax = constructionYearMax;
        return this;
    }

    public void setConstructionYearMax(Integer constructionYearMax) {
        this.constructionYearMax = constructionYearMax;
    }

    public String getType() {
        return type;
    }

    public Questionnaire type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getTotalCostMin() {
        return totalCostMin;
    }

    public Questionnaire totalCostMin(Double totalCostMin) {
        this.totalCostMin = totalCostMin;
        return this;
    }

    public void setTotalCostMin(Double totalCostMin) {
        this.totalCostMin = totalCostMin;
    }

    public Double getTotalCostMax() {
        return totalCostMax;
    }

    public Questionnaire totalCostMax(Double totalCostMax) {
        this.totalCostMax = totalCostMax;
        return this;
    }

    public void setTotalCostMax(Double totalCostMax) {
        this.totalCostMax = totalCostMax;
    }

    public User getUser() {
        return user;
    }

    public Questionnaire user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Questionnaire questionnaire = (Questionnaire) o;
        if (questionnaire.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), questionnaire.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
            "id=" + getId() +
            ", pets='" + isPets() + "'" +
            ", smokingInside='" + isSmokingInside() + "'" +
            ", isFurnished='" + isIsFurnished() + "'" +
            ", roomAmountMin=" + getRoomAmountMin() +
            ", roomAmountMax=" + getRoomAmountMax() +
            ", sizeMin=" + getSizeMin() +
            ", sizeMax=" + getSizeMax() +
            ", constructionYearMin=" + getConstructionYearMin() +
            ", constructionYearMax=" + getConstructionYearMax() +
            ", type='" + getType() + "'" +
            ", totalCostMin=" + getTotalCostMin() +
            ", totalCostMax=" + getTotalCostMax() +
            "}";
    }
}
