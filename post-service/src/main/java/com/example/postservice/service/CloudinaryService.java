package com.example.postservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
        Map<String,String> result = null;
        try {
            result = cloudinary.uploader()
                    .upload(base64Image, options);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        return result;
    }

    public Map<String, String> deleteImage(String imageUrl) {
        Map result = null;
        int beginIndex = imageUrl.lastIndexOf("/");
        int lastIndex = imageUrl.lastIndexOf(".");
        imageUrl = imageUrl.substring(beginIndex,lastIndex);
        logger.info("[Public Id] " + imageUrl+ "["+beginIndex+","+lastIndex+"]");
        try {
            result = cloudinary.uploader()
                    .destroy(options.get("folder")+imageUrl, ObjectUtils.emptyMap());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
