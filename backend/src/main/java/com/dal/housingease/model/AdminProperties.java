package com.dal.housingease.model;
import com.dal.housingease.enums.PropertiesEnums;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
/**
 * Represents an Admin Property entity in the system.
 */

@Setter
@Getter
@Entity
@Table(name = "properties")
public class AdminProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonProperty("owner_id")
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @JsonProperty("property_type")
    @Column(name = "property_type", nullable = false)
    private String propertyType;

    @JsonProperty("street_address")
    @Column(name = "street_address", nullable = false)
    private String streetAddress;

    @JsonProperty("city")
    @Column(name = "city", nullable = false)
    private String city;

    @JsonProperty("province")
    @Column(name = "province", nullable = false)
    private String province;

    @JsonProperty("postal_code")
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @JsonProperty("unit_number")
    @Column(name = "unit_number", nullable = false)
    private String unitNumber;

    @JsonProperty("monthly_rent")
    @Column(name = "monthly_rent", nullable = false)
    private Double monthlyRent;


    @JsonProperty("availability")
    @Column(name = "availability", nullable = false)
    private Date availability;

    @JsonProperty("latitude")
    @Column(name = "latitude")
    private String latitude;

    @JsonProperty("longitude")
    @Column(name = "longitude")
    private String longitude;

    @JsonProperty("property_heading")
    @Column(name = "property_heading", nullable = false)
    private String propertyHeading;

    @JsonProperty("full_description")
    @Column(name = "full_description", columnDefinition = "TEXT", nullable = false)
    private String fullDescription;

    @JsonProperty("bathrooms")
    @Column(name = "bathrooms", nullable = false)
    private Integer bathrooms;

    @JsonProperty("bedrooms")
    @Column(name = "bedrooms", nullable = false)
    private Integer bedrooms;

    @JsonProperty("furnishing")
    @Column(name = "furnishing", nullable = false)
    private String furnishing;

    @JsonProperty("parking")
    @Column(name = "parking", nullable = false)
    private String parking;

    @JsonProperty("email")
    @Column(name = "email", nullable = false)
    private String email;

    @JsonProperty("mobile")
    @Column(name = "mobile", nullable = false)
    private String mobile;

    @JsonProperty("youtube")
    @Column(name = "youtube", nullable = false,columnDefinition = "TEXT")
    private String youtube;

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    @Column(name = "status", nullable = false)
    private PropertiesEnums.Status status = PropertiesEnums.Status.Pending;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PropertyImages> images;

}
