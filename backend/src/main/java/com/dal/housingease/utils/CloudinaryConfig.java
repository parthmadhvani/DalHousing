package com.dal.housingease.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dxyrjxcsy",
                "api_key", "711111385113719",
                "api_secret", "nT_vumPYt9MWOvW9SR7GeUmI-AA"));
    }
}
