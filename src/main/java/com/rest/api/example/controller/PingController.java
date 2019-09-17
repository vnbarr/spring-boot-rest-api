package com.rest.api.example.controller;

import com.rest.api.example.domain.dto.ApiVersionDto;
import com.rest.api.example.service.CompanyService;
import com.rest.api.example.utils.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by vbarros on 16/09/2019 .
 */

@RestController
@RequestMapping("/ping")
@CrossOrigin
public class PingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingController.class);

    private final CompanyService companyService;

    public PingController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity ping() {
        String version = ConfigurationLoader.getPropValue("API_VERSION_NUMBER");
        //just for controlling db access
        companyService.getSize();
        return ResponseEntity.ok(new ApiVersionDto(version));
    }
}
