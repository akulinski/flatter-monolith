package com.flatter.server.domain.dto;

import com.flatter.server.domain.*;
import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class ProfileWithReviewsDTO {
    private User receiver;
    private ProfilePicture profilePicture;
    private List<Pair<Review, ProfilePicture>> pairs;

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Pair<Review, ProfilePicture>> getPairs() {
        return pairs;
    }

    public void setPairs(List<Pair<Review, ProfilePicture>> pairs) {
        this.pairs = pairs;
    }
}
