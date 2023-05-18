package com.example.postservice.service;

import com.cloudinary.Cloudinary;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    private Map<String, String> options = new HashMap<>();
    private Logger logger = Logger.getLogger(CloudinaryService.class);
    @Autowired
    private Cloudinary cloudinary;

    public CloudinaryService() {
        options.put("folder", "socialmedia/post");
    }

    public Map<String, String> uploadImage(String base64Image) {
        Map result = null;
        try {
            result = cloudinary.uploader()
                    .upload(base64Image, options);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return result;
    }
}
