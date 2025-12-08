package com.agrosecure.controller;

import com.agrosecure.dto.CreateFarmInput;
import com.agrosecure.dto.UpdateFarmInput;
import com.agrosecure.model.Farm;
import com.agrosecure.repository.FarmRepository;
import com.agrosecure.repository.UserRepository;
import com.agrosecure.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class FarmGraphQLController {
    @Autowired
    FarmRepository farmRepository;

    @Autowired
    UserRepository userRepository;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Farm> farms() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (isAdmin) {
            return farmRepository.findAll();
        }
        return farmRepository.findByOwnerId(userDetails.getId());
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Farm createFarm(@Argument CreateFarmInput input) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Farm farm = new Farm();
        farm.setName(input.getName());
        farm.setLocation(input.getLocation());
        farm.setOwner(userRepository.findById(userDetails.getId()).orElseThrow());
        farm.setCreatedAt(LocalDateTime.now());
        return farmRepository.save(farm);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Farm updateFarm(@Argument UpdateFarmInput input) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Farm farm = farmRepository.findById(input.getId())
                .orElseThrow(() -> new RuntimeException("Farm not found"));

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (!isAdmin && !farm.getOwner().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Access Denied");
        }

        if (input.getName() != null) {
            farm.setName(input.getName());
        }
        if (input.getLocation() != null) {
            farm.setLocation(input.getLocation());
        }

        return farmRepository.save(farm);
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Boolean deleteFarm(@Argument UUID id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Farm farm = farmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farm not found"));

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        if (!isAdmin && !farm.getOwner().getId().equals(userDetails.getId())) {
            throw new RuntimeException("Access Denied");
        }

        farmRepository.delete(farm);
        return true;
    }
}

