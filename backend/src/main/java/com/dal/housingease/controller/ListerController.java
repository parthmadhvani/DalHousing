package com.dal.housingease.controller;

import com.dal.housingease.model.User;
import com.dal.housingease.service.UserServiceImpli;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

public class ListerController
{
	private final UserServiceImpli userService;
	public ListerController(UserServiceImpli userService)
	{
        this.userService = userService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('LISTER')")
    public ResponseEntity<User> getListerProfile()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
     // Check if the current user has the "lister" role
        boolean hasListerRole = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_LISTER"));
        
        if (!hasListerRole) {
            // Handle unauthorized access if the user doesn't have the lister role
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(currentUser);
    }
}
