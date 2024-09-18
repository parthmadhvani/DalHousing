package com.dal.housingease.repository;

import com.dal.housingease.model.Properties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Repository interface for managing {@link Properties} entities.
 */
@Repository
public interface PropertiesRepository extends JpaRepository<Properties , Integer> {

    /**
     * Finds all properties owned by a specific owner.
     *
     * @param ownerId the ID of the property owner
     * @return a list of properties owned by the specified owner
     */
    @Query(value = "SELECT * FROM properties WHERE owner_id = :ownerId", nativeQuery = true)
    List<Properties> findAllByOwnerId (int ownerId);

    /**
     * Updates the details of an existing property.
     *
     * @param availability       the new availability date of the property
     * @param latitude           the new latitude of the property
     * @param longitude          the new longitude of the property
     * @param property_type      the new type of the property
     * @param monthly_rent       the new monthly rent of the property
     * @param security_deposite  the new security deposit for the property
     * @param street_address     the new street address of the property
     * @param city               the new city where the property is located
     * @param full_description   the new full description of the property
     * @param postal_code        the new postal code of the property
     * @param property_heading   the new heading or title of the property
     * @param province           the new province where the property is located
     * @param status             the new status of the property
     * @param unit_number        the new unit number of the property
     * @param mobile             the new mobile contact for the property
     * @param email              the new email contact for the property
     * @param bathrooms          the new number of bathrooms in the property
     * @param bedrooms           the new number of bedrooms in the property
     * @param parking            the new parking details of the property
     * @param furnishing         the new furnishing details of the property
     * @param id                 the ID of the property to be updated
     * @return the number of properties updated
     */
    @Query(value = "UPDATE properties SET availability = :availability , latitude = :latitude , longitude=:longitude , property_type=:property_type , monthly_rent=:monthly_rent , security_deposite=:security_deposite,street_address=:street_address , city=:city , full_description=:full_description , postal_code=:postal_code , property_heading=:property_heading , province=:province , status=:status , unit_number=:unit_number , mobile=:mobile , email=:email , bathrooms=:bathrooms , bedrooms=:bedrooms , parking=:parking , furnishing=:furnishing WHERE id=:id", nativeQuery = true)
    @Modifying
    @Transactional
    int updateProperty(Date availability , String latitude , String longitude , String property_type , double monthly_rent , double security_deposite , String street_address , String city , String full_description , String postal_code , String property_heading , String province , String status , String unit_number , int id , String mobile , String email , int bathrooms , int bedrooms , String parking , String furnishing);
}
