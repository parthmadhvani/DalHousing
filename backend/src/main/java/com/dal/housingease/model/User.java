package com.dal.housingease.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
/**
 * Entity representing a User in the application.
 * Implements UserDetails interface for Spring Security.
 */
@Table(name = "users")
@Entity
public class User implements UserDetails 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(unique = true,nullable = false,length = 25)
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "Username should start with a letter and contain only letters and numbers")
    private String userName;

    @Column(unique = true, length = 30, nullable = false)
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @CreationTimestamp
    @Column(updatable = false, name = "Registered On")
    private Date registeredOn;

    @UpdateTimestamp
    @Column(name = "updated On")
    private Date updatedOn;
    
    @Column(nullable = false,length = 25)
    private String firstName;

    @Column(nullable = false,length = 25)
    private String lastName;

    @Column(nullable = false,unique = true,length = 14)
    @Pattern(regexp="(^$|[0-9]{10})", message = "Please provide a valid 10-digit mobile number")
    private String mobileNumber;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() 
    {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName().toString());

        return List.of(authority);
    }
    public String getPassword() 
    {
        return password;
    }
    
    @Override
    public String getUsername() 
    {
        return email; 
    }

    @Override
    public boolean isAccountNonExpired() 
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() 
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() 
    {
        return true;
    }

    @Override
    public boolean isEnabled() 
    {
        return true;
    }

    public Integer getId() 
    {
        return id;
    }

    public User setId(Integer id) 
    {
        this.id = id;
        return this;
    }

    public String getUserName() 
    {
        return userName;
    }

    public User setUserName(String userName) 
    {
        this.userName = userName;
        return this;
    }

    public String getEmail() 
    {
        return email;
    }

    public User setEmail(String email) 
    {
        this.email = email;
        return this;
    }

    public User setPassword(String password) 
    {
        this.password = password;
        return this;
    }

    public Date getCreatedAt() 
    {
        return registeredOn;
    }

    public User setCreatedAt(Date createdAt) 
    {
        this.registeredOn = createdAt;
        return this;
    }

    public Date getUpdatedAt() 
    {
        return updatedOn;
    }

    public User setUpdatedAt(Date updatedAt) 
    {
        this.updatedOn = updatedAt;
        return this;
    }

    public Role getRole()
    {
        return role;
    }

    public User setRole(Role role)
    {
        this.role = role;
        return this;
    }

    public String getFirstName() 
    {
        return firstName;
    }

    public User setFirstName(String firstName) 
    {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() 
    {
        return lastName;
    }

    public User setLastName(String lastName)
    {
        this.lastName = lastName;
        return this;
    }

    public String getMobileNumber() 
    {
        return mobileNumber;
    }

    public User setMobileNumber(String mobileNumber) 
    {
        this.mobileNumber = mobileNumber;
        return this;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", registeredOn=" + registeredOn +
                ", updatedOn=" + updatedOn +
                '}';
    }
    
    //methods used in test cases
    @Override
    public int hashCode() 
    {
        return  Objects.hash(id); 
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
}