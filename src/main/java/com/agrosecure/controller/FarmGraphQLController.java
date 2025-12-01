package com.agrosecure.controller;

import com.agrosecure.dto.CreateFarmInput;
import com.agrosecure.model.Farm;
import com.agrosecure.repository.FarmRepository;
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

@Controller
public class FarmGraphQLController {
    @Autowired
    FarmRepository farmRepository;

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
        farm.setOwnerId(userDetails.getId());
        farm.setCreatedAt(LocalDateTime.now());
        return farmRepository.save(farm);
    }
}

