package com.flatter.server.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A ProfilePicture.
 */
@Entity
@Table(name = "profile_picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProfilePicture implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "image", nullable = false)
    private byte[] image;

    @Column(name = "image_content_type", nullable = false)
    private String imageContentType;

    @Column(name = "height")
    private Integer height;

    @Column(name = "width")
    private Integer width;

    @Column(name = "taken")
    private Instant taken;

    @Column(name = "uploaded")
    private Instant uploaded;

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

    public byte[] getImage() {
        return image;
    }

    public ProfilePicture image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public ProfilePicture imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Integer getHeight() {
        return height;
    }

    public ProfilePicture height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public ProfilePicture width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Instant getTaken() {
        return taken;
    }

    public ProfilePicture taken(Instant taken) {
        this.taken = taken;
        return this;
    }

    public void setTaken(Instant taken) {
        this.taken = taken;
    }

    public Instant getUploaded() {
        return uploaded;
    }

    public ProfilePicture uploaded(Instant uploaded) {
        this.uploaded = uploaded;
        return this;
    }

    public void setUploaded(Instant uploaded) {
        this.uploaded = uploaded;
    }

    public User getUser() {
        return user;
    }

    public ProfilePicture user(User user) {
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
        ProfilePicture profilePicture = (ProfilePicture) o;
        if (profilePicture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profilePicture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfilePicture{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", height=" + getHeight() +
            ", width=" + getWidth() +
            ", taken='" + getTaken() + "'" +
            ", uploaded='" + getUploaded() + "'" +
            "}";
    }
}
