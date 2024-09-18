package com.dal.housingease.controller;

import com.dal.housingease.model.User;
import com.dal.housingease.service.UserServiceImpli;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/users")
@RestController
public class SeekerController 
{
	 private final UserServiceImpli userService;

	    public SeekerController(UserServiceImpli userService) {
	        this.userService = userService;
	    }

	    @GetMapping("/profile")
	    @PreAuthorize("hasRole('SEEKER')")
	    public ResponseEntity<User> getSeekerProfile() 
	    {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        User currentUser = (User) authentication.getPrincipal();
	        
	     // Check if the current user has the "seeker" role
	        boolean hasSeekerRole = authentication.getAuthorities().stream()
	                .anyMatch(role -> role.getAuthority().equals("ROLE_SEEKER"));
	        
	        if (!hasSeekerRole) {
	            // Handle unauthorized access if the user doesn't have the seeker role
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	        
	        return ResponseEntity.ok(currentUser);
	    }
}
