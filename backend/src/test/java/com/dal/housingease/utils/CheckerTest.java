package com.dal.housingease.utils;

import com.dal.housingease.exceptions.InvalidPropertyDataException;
import com.dal.housingease.model.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CheckerTest {

    private Checker checker;
    private Properties properties;

    @BeforeEach
    public void setUp() {
        checker = new Checker();

        properties = Properties.builder()
                .property_type("House")
                .email("test@example.com")
                .street_address("123 Main St")
                .mobile("1234567890")
                .province("Province")
                .city("City")
                .postal_code("12345")
                .unit_number("1A")
                .monthly_rent(1000.0)
                .security_deposite(500.0)
                .availability(new Date())
                .latitude("40.7128")
                .longitude("74.0060")
                .property_heading("Beautiful House")
                .full_description("This is a great house.")
                .bedrooms(4)
                .bathrooms(3)
                .parking("Garage")
                .furnishing("Furnished")
                .build();
    }

    @Test
    public void testCheckProperties_ValidProperties() {
        assertTrue(checker.checkProperties(properties));
    }

    @Test
    public void testCheckProperties_InvalidPropertyType() {
        properties.setProperty_type(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("invalid property type", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidEmail() {
        properties.setEmail(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("invalid email address", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidStreetAddress() {
        properties.setStreet_address(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid street address", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidMobileNumber() {
        properties.setMobile(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid mobile number", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidProvince() {
        properties.setProvince(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid province", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidCity() {
        properties.setCity(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid city", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidPostalCode() {
        properties.setPostal_code(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid postal code", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidUnitNumber() {
        properties.setUnit_number(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid unit number", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidRent() {
        properties.setMonthly_rent(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid rent", exception.getMessage());

        properties.setMonthly_rent(Double.valueOf(-1));
        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid rent", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidSecurityDeposit() {
        properties.setSecurity_deposite(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid security deposit", exception.getMessage());

        properties.setSecurity_deposite(Double.valueOf(-1));
        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid security deposit", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidAvailabilityDate() {
        properties.setAvailability(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid availability date", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidLatitude() {
        properties.setLatitude(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Please choose the address from the autofill", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidLongitude() {
        properties.setLongitude(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Please choose the address from the autofill", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidPropertyHeading() {
        properties.setProperty_heading(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid heading", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidFullDescription() {
        properties.setFull_description(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid description", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidBedrooms() {
        properties.setBedrooms(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid number for bedrooms", exception.getMessage());

        properties.setBedrooms(-1);
        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid number for bedrooms", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidBathrooms() {
        properties.setBathrooms(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid number for bathrooms", exception.getMessage());

        properties.setBathrooms(-1);
        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid number for bathrooms", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidParking() {
        properties.setParking(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid value for parking", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidFurnishing() {
        properties.setFurnishing(null);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Invalid value for furnishing", exception.getMessage());
    }

    @Test
    public void testCheckProperties_InvalidMobile() {
        properties.setMobile("123456");
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkProperties(properties);
        });
        assertEquals("Mobile number is not valid", exception.getMessage());
    }

    @Test
    public void testCheckImages_ValidImages() {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getSize()).thenReturn(1 * 1024 * 1024L);
            files.add(file);
        }
        assertDoesNotThrow(() -> checker.checkImages(files));
    }

    @Test
    public void testCheckImages_NullFiles() {
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(null);
        });

        assertEquals("Minimum 4 images must be uploaded.", exception.getMessage());
    }



    @Test
    public void testCheckImages_FewerThan4Files() {
        List<MultipartFile> files = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class));

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });

        assertEquals("Minimum 4 images must be uploaded.", exception.getMessage());
    }

    @Test
    public void testCheckImages_MoreThan10Files() {
        List<MultipartFile> files = Arrays.asList(
                mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
                mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class),
                mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class)
        );

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });

        assertEquals("Maximum 10 images can be uploaded.", exception.getMessage());
    }

    @Test
    public void testCheckImages_FileSizeExceedsLimit() {
        MultipartFile largeFile = mock(MultipartFile.class);
        when(largeFile.getSize()).thenReturn(3 * 1024 * 1024L);

        List<MultipartFile> files = Arrays.asList(mock(MultipartFile.class), mock(MultipartFile.class), mock(MultipartFile.class), largeFile);

        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });

        assertEquals("Each image must be less than 2 MB.", exception.getMessage());
    }

    @Test
    public void testCheckImages_ValidFiles() {
        MultipartFile validFile = mock(MultipartFile.class);
        when(validFile.getSize()).thenReturn(1 * 1024 * 1024L); // 1 MB

        List<MultipartFile> files = Arrays.asList(validFile, validFile, validFile, validFile);

        assertTrue(checker.checkImages(files));
    }

    @Test
    public void testCheckImages_LessThanFourImages() {
        List<MultipartFile> files = new ArrayList<>();
        MultipartFile file = mock(MultipartFile.class);
        files.add(file);
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });
        assertEquals("Minimum 4 images must be uploaded.", exception.getMessage());
    }

    @Test
    public void testCheckImages_TooManyImages() {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            MultipartFile file = mock(MultipartFile.class);
            files.add(file);
        }
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });
        assertEquals("Maximum 10 images can be uploaded.", exception.getMessage());
    }

    @Test
    public void testCheckImages_FileTooLarge() {
        List<MultipartFile> files = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MultipartFile file = mock(MultipartFile.class);
            when(file.getSize()).thenReturn(6 * 1024 * 1024L); // 6 MB
            files.add(file);
        }
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.checkImages(files);
        });
        assertEquals("Each image must be less than 2 MB.", exception.getMessage());
    }

    @Test
    public void testValidateFilterValues_ValidValues() {
        assertDoesNotThrow(() -> checker.validateFilterValues(100.0, 500.0, 3, 2));
        assertDoesNotThrow(() -> checker.validateFilterValues(null, 500.0, 3, 2));
        assertDoesNotThrow(() -> checker.validateFilterValues(100.0, null, 3, 2));
        assertDoesNotThrow(() -> checker.validateFilterValues(100.0, 500.0, null, 2));
        assertDoesNotThrow(() -> checker.validateFilterValues(100.0, 500.0, 3, null));
    }

    @Test
    public void testValidateFilterValues_InvalidMinPrice() {
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(-100.0, 500.0, 3, 2);
        });
        assertEquals("Invalid minimum price as it cannot be negative or zero.", exception.getMessage());

        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(0.0, 500.0, 3, 2);
        });
        assertEquals("Invalid minimum price as it cannot be negative or zero.", exception.getMessage());
    }

    @Test
    public void testValidateFilterValues_InvalidMaxPrice() {
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, -500.0, 3, 2);
        });
        assertEquals("Invalid minimum price as it cannot be negative or zero.", exception.getMessage());

        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, 0.0, 3, 2);
        });
        assertEquals("Invalid minimum price as it cannot be negative or zero.", exception.getMessage());
    }

    @Test
    public void testValidateFilterValues_InvalidBedrooms() {
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, 500.0, -3, 2);
        });
        assertEquals("Invalid number of bedrooms as it cannot be negative or zero.", exception.getMessage());

        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, 500.0, 0, 2);
        });
        assertEquals("Invalid number of bedrooms as it cannot be negative or zero.", exception.getMessage());
    }

    @Test
    public void testValidateFilterValues_InvalidBathrooms() {
        InvalidPropertyDataException exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, 500.0, 3, -2);
        });
        assertEquals("Invalid number of bathrooms as it cannot be negative or zero.", exception.getMessage());

        exception = assertThrows(InvalidPropertyDataException.class, () -> {
            checker.validateFilterValues(100.0, 500.0, 3, 0);
        });
        assertEquals("Invalid number of bathrooms as it cannot be negative or zero.", exception.getMessage());
    }

}
