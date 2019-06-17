package com.flatter.server.domain.dto;

import com.flatter.server.domain.Address;
import com.flatter.server.domain.Album;
import com.flatter.server.domain.Offer;
import lombok.Data;

import java.io.Serializable;

@Data
public class FullOfferDTO implements Serializable {

    private Album album;
    private Address address;
    private Offer offer;
}
