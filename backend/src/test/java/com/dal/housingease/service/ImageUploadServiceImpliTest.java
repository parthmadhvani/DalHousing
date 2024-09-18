package com.dal.housingease.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.dal.housingease.model.PropertyImages;
import com.dal.housingease.repository.PropertyImagesRepository;
import com.dal.housingease.utils.Checker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageUploadServiceImpliTest {

    @InjectMocks
    private ImageUploadServiceImpli imageUploadService;
    @Mock
    private Uploader uploader;
    @Mock
    private Cloudinary cloudinaryMock;
    @Mock
    private PropertyImagesRepository propertyImagesRepositoryMock;
    @Mock
    private Checker checkerMock;

    @Test
    public void testUpload_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        Uploader uploader = mock(Uploader.class);

        when(file.getBytes()).thenReturn(new byte[]{});
        when(cloudinaryMock.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(Collections.singletonMap("url", "http://example.com/image.jpg"));

        Map result = imageUploadService.upload(file);

        assertEquals("http://example.com/image.jpg", result.get("url"));
    }

    @Test
    public void testUpload_Failure() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        Uploader uploader = mock(Uploader.class);

        when(file.getBytes()).thenReturn(new byte[]{});
        when(cloudinaryMock.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), anyMap())).thenThrow(new IOException("Upload error"));

        Exception exception = assertThrows(Exception.class, () -> {
            imageUploadService.upload(file);
        });

        assertEquals("Upload error", exception.getMessage());
    }

    @Test
    public void testSaveImage() {
        PropertyImages propertyImages = new PropertyImages();
        propertyImages.setImage_id(1);
        propertyImages.setImage_url("http://example.com/image.jpg");
        propertyImages.setPropertyId(1);

        when(propertyImagesRepositoryMock.save(propertyImages)).thenReturn(propertyImages);

        PropertyImages savedImage = imageUploadService.saveImage(propertyImages);

        assertEquals(1, savedImage.getImage_id());
        assertEquals("http://example.com/image.jpg", savedImage.getImage_url());
        assertEquals(1, savedImage.getPropertyId());
    }

    @Test
    public void testFindById_Found() {
        PropertyImages propertyImages = new PropertyImages();
        propertyImages.setImage_id(1);

        when(propertyImagesRepositoryMock.findById(1)).thenReturn(Optional.of(propertyImages));

        PropertyImages foundImage = imageUploadService.findById(1);

        assertEquals(1, foundImage.getImage_id());
    }

    @Test
    public void testFindById_NotFound() {
        when(propertyImagesRepositoryMock.findById(1)).thenReturn(Optional.empty());

        PropertyImages foundImage = imageUploadService.findById(1);

        assertNull(foundImage);
    }

    @Test
    public void testFindAll() {
        List<PropertyImages> imagesList = new ArrayList<>();
        PropertyImages image1 = new PropertyImages();
        image1.setImage_id(1);
        PropertyImages image2 = new PropertyImages();
        image2.setImage_id(2);
        imagesList.add(image1);
        imagesList.add(image2);

        when(propertyImagesRepositoryMock.findAll()).thenReturn(imagesList);

        List<PropertyImages> foundImages = imageUploadService.findAll();

        assertEquals(2, foundImages.size());
        assertEquals(1, foundImages.get(0).getImage_id());
        assertEquals(2, foundImages.get(1).getImage_id());
    }

    @Test
    public void testHandleMultipartFile_Success() throws Exception {
        // Creating a list of mock MultipartFile objects
        List<MultipartFile> files = getMultipartFiles();

        when(cloudinaryMock.uploader()).thenReturn(uploader);

        // Mocking the checkImages method to return true (no exceptions)
        when(checkerMock.checkImages(files)).thenReturn(true);

        // Mocking the upload result
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://example.com/image.jpg");

        // Mocking the upload method on the Uploader mock to return the upload result
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(uploadResult);

        // Mocking the save method to simply return the input object
        doAnswer(invocation -> {
            PropertyImages propertyImages = invocation.getArgument(0);
            assertEquals(1, propertyImages.getPropertyId());
            assertEquals("http://example.com/image.jpg", propertyImages.getImage_url());
            return propertyImages;
        }).when(propertyImagesRepositoryMock).save(any(PropertyImages.class));

        // Calling the handleMultipartFile method
        String result = imageUploadService.handleMultipartFile(files, 1);

        // Verifying the results
        assertEquals("Properties added successfully", result);
        verify(propertyImagesRepositoryMock, times(4)).save(any(PropertyImages.class));
    }

    private static List<MultipartFile> getMultipartFiles() {
        MultipartFile file1 = new MockMultipartFile(
                "file1", "file1.jpg", "image/jpeg", "image content".getBytes());
        MultipartFile file2 = new MockMultipartFile(
                "file2", "file2.jpg", "image/jpeg", "image content".getBytes());
        MultipartFile file3 = new MockMultipartFile(
                "file3", "file3.jpg", "image/jpeg", "image content".getBytes());
        MultipartFile file4 = new MockMultipartFile(
                "file4", "file4.jpg", "image/jpeg", "image content".getBytes());

        List<MultipartFile> files = Arrays.asList(file1, file2, file3, file4);
        return files;
    }

    @Test
    public void testHandleMultipartFile_Failure() throws Exception {
        MultipartFile file = mock(MultipartFile.class);

        List<MultipartFile> files = Collections.singletonList(file);
        when(checkerMock.checkImages(files)).thenReturn(true);

        imageUploadService.handleMultipartFile(files, 1);

        verify(propertyImagesRepositoryMock, never()).save(any(PropertyImages.class));
    }

    @Test
    public void testHandleMultipartFile_InvalidImages() {
        List<MultipartFile> files = Collections.emptyList();
        when(checkerMock.checkImages(files)).thenReturn(false);

        String result = imageUploadService.handleMultipartFile(files, 1);

        assertEquals("Properties added successfully", result);
        verify(propertyImagesRepositoryMock, never()).save(any(PropertyImages.class));
    }


}