package com.agrosecure.controller;

import com.agrosecure.dto.CreateCropInput;
import com.agrosecure.model.Crop;
import com.agrosecure.model.Farm;
import com.agrosecure.repository.CropRepository;
import com.agrosecure.repository.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CropGraphQLController {

    @Autowired
    CropRepository cropRepository;

    @Autowired
    FarmRepository farmRepository;

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Crop> crops() {
        return cropRepository.findAll();
    }

    @MutationMapping
    @PreAuthorize("isAuthenticated()")
    public Crop createCrop(@Argument CreateCropInput input) {
        Farm farm = farmRepository.findById(input.getFarmId())
                .orElseThrow(() -> new RuntimeException("Farm not found"));

        Crop crop = new Crop();
        crop.setType(input.getType());
        crop.setFarm(farm);

        return cropRepository.save(crop);
    }
}

