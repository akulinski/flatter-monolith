package com.flatter.server.service;

import com.flatter.server.domain.Photo;
import com.flatter.server.domain.ProfilePicture;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PictureService {
    public PictureService() {
    }

    public void scaleDownPicture(@RequestBody @Valid ProfilePicture profilePicture) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(profilePicture.getImage());
        BufferedImage img = ImageIO.read(in);
        Integer profilePictureHeight = profilePicture.getHeight();
        Integer profilePictureWidth = profilePicture.getWidth();

        if (profilePictureHeight == 0) {
            profilePictureHeight = (profilePictureWidth * img.getHeight()) / img.getWidth();
        }
        if (profilePictureWidth == 0) {
            profilePictureWidth = (profilePictureHeight * img.getWidth()) / img.getHeight();
        }
        Image scaledImage = img.getScaledInstance(profilePictureWidth, profilePictureHeight, Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(profilePictureWidth, profilePictureHeight, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ImageIO.write(imageBuff, "jpg", buffer);

        profilePicture.setImage(buffer.toByteArray());
    }

    public void scaleDownPicture(@RequestBody @Valid Photo photo) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(photo.getImage());
        BufferedImage img = ImageIO.read(in);
        Integer profilePictureHeight = photo.getHeight();
        Integer profilePictureWidth = photo.getWidth();

        if (profilePictureHeight == 0) {
            profilePictureHeight = (profilePictureWidth * img.getHeight()) / img.getWidth();
        }
        if (profilePictureWidth == 0) {
            profilePictureWidth = (profilePictureHeight * img.getWidth()) / img.getHeight();
        }
        Image scaledImage = img.getScaledInstance(profilePictureWidth, profilePictureHeight, Image.SCALE_SMOOTH);
        BufferedImage imageBuff = new BufferedImage(profilePictureWidth, profilePictureHeight, BufferedImage.TYPE_INT_RGB);
        imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        ImageIO.write(imageBuff, "jpg", buffer);

        photo.setImage(buffer.toByteArray());
    }
}
