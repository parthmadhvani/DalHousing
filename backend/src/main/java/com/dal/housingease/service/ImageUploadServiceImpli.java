package com.dal.housingease.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.utils.Checker;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageUploadServiceImpli implements ImageUploadService
{
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadServiceImpli.class);
    private final Cloudinary cloudinary;
    private final PropertyImagesRepository propertyImagesRepository;
    private final Checker checker;
    public Map upload(MultipartFile file) throws Exception
    {
        try
        {
            logger.info("Uploading file: {}", file.getOriginalFilename());
            return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        }
        catch (Exception e)
        {
            logger.error("Error uploading file: {}", file.getOriginalFilename(), e);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public PropertyImages saveImage(PropertyImages propertyImages)
    {
        logger.info("Saving property image: {}", propertyImages);
        return propertyImagesRepository.save(propertyImages);
    }

    @Override
    public PropertyImages findById(int id)
    {
        logger.info("Finding property image by ID: {}", id);
        Optional<PropertyImages> optional = propertyImagesRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<PropertyImages> findAll()
    {
        logger.info("Finding all property images");
        return propertyImagesRepository.findAll();
    }

    @Override
    public String handleMultipartFile(List<MultipartFile> files, int savedPropertyId)
    {
        logger.info("Handling multiple files for property ID: {}", savedPropertyId);
        boolean isValid = checker.checkImages(files);
        if (isValid)
        {
            logger.warn("Invalid images detected");
            System.out.println("Invalid images");
        }
        try

        {    if (files !=null)

            {    for (MultipartFile file : files)
                {
                    // Upload image and get URL
                    Map uploadResult = upload(file);
                    String imageUrl = uploadResult.get("url").toString();

                    // Save image URL with property ID
                    PropertyImages propertyImages = new PropertyImages();
                    propertyImages.setPropertyId(savedPropertyId);
                    propertyImages.setImage_url(imageUrl);
                    saveImage(propertyImages);
                }
            }
            logger.info("Properties added successfully for property ID: {}", savedPropertyId);
            return "Properties added successfully";
        }
        catch (Exception e)
        {
            logger.error("Failed to add properties for property ID: {}", savedPropertyId, e);
            return "Failed to add properties: " + e.getMessage();
        }
    }
}