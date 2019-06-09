package com.flatter.server.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Offer.
 */
@Entity
@Table(name = "offer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "total_cost", nullable = false)
    @JsonProperty("totalCost")
    private Double totalCost;

    @NotNull
    @Column(name = "room_amount", nullable = false)
    private Integer roomAmount;

    @NotNull
    @Column(name = "jhi_size", nullable = false)
    private Double size;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "construction_year")
    private Integer constructionYear;

    @Column(name = "pets")
    private Boolean pets;

    @Column(name = "smoking_inside")
    private Boolean smokingInside;

    @Column(name = "is_furnished")
    private Boolean isFurnished;

    @ManyToOne
    @JsonIgnoreProperties(value = {"offers"})
    private User user;

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL)
    private Album album;

    @OneToOne(mappedBy = "offer")
    @JsonIgnore
    private Match match;

    @Column(name = "views")
    private Long views = 0L;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Offer description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public Offer totalCost(Double totalCost) {
        this.totalCost = totalCost;
        return this;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getRoomAmount() {
        return roomAmount;
    }

    public Offer roomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
        return this;
    }

    public void setRoomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
    }

    public Double getSize() {
        return size;
    }

    public Offer size(Double size) {
        this.size = size;
        return this;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public Offer type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getConstructionYear() {
        return constructionYear;
    }

    public Offer constructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
        return this;
    }

    public void setConstructionYear(Integer constructionYear) {
        this.constructionYear = constructionYear;
    }

    public Boolean isPets() {
        return pets;
    }

    public Offer pets(Boolean pets) {
        this.pets = pets;
        return this;
    }

    public void setPets(Boolean pets) {
        this.pets = pets;
    }

    public Boolean isSmokingInside() {
        return smokingInside;
    }

    public Offer smokingInside(Boolean smokingInside) {
        this.smokingInside = smokingInside;
        return this;
    }

    public void setSmokingInside(Boolean smokingInside) {
        this.smokingInside = smokingInside;
    }

    public Boolean isIsFurnished() {
        return isFurnished;
    }

    public Offer isFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished;
        return this;
    }

    public void setIsFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished;
    }

    public User getUser() {
        return user;
    }

    public Offer user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public Offer address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Album getAlbum() {
        return album;
    }

    public Offer album(Album album) {
        this.album = album;
        return this;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Match getMatch() {
        return match;
    }

    public Offer match(Match match) {
        this.match = match;
        return this;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Offer)) {
            return false;
        }
        return id != null && id.equals(((Offer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Offer{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", totalCost=" + getTotalCost() +
            ", roomAmount=" + getRoomAmount() +
            ", size=" + getSize() +
            ", type='" + getType() + "'" +
            ", constructionYear=" + getConstructionYear() +
            ", pets='" + isPets() + "'" +
            ", smokingInside='" + isSmokingInside() + "'" +
            ", isFurnished='" + isIsFurnished() + "'" +
            "}";
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public void addView(){
        this.views++;
    }
}
