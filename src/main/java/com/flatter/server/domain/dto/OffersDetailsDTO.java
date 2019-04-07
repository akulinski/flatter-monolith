package com.flatter.server.domain.dto;

import com.flatter.server.domain.Offer;
import com.flatter.server.domain.Photo;
import lombok.Data;

import java.util.List;

@Data
public class OffersDetailsDTO {
    private Offer offer;
    private List<Photo> photos;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
