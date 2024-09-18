package com.dal.housingease.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int image_id;

    @JsonProperty("image_url")
    @Column(name = "image_url", nullable = false)
    private String image_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Properties property;

    @JsonProperty("property_id")
    public Integer getPropertyId() {
        return property != null ? property.getId() : null;
    }

    @JsonProperty("property_id")
    public void setPropertyId(Integer propertyId) {
        if (property == null) {
            property = new Properties();
        }
        property.setId(propertyId);
    }

}
