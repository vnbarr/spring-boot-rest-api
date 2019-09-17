package com.rest.api.example.controller;

import com.rest.api.example.domain.model.Company;
import com.rest.api.example.domain.request.CompanyRequest;
import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.MissingParameterException;
import com.rest.api.example.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by vbarros on 16/09/2019 .
 */
@RestController
@RequestMapping("/companies")
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<Collection<Company>> getCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @ResponseBody
    @PostMapping(value = "/bulk")
    public ResponseEntity<List<Company>> createBulk(@RequestBody List<CompanyRequest> companiesToCreate) throws DuplicatedEntityException, MissingParameterException {
        List<Company> companies = companyService.create(companiesToCreate);
        return new ResponseEntity<List<Company>>(companies, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping(value = "/bulk")
    public ResponseEntity<List<Company>> updateBulk(@RequestBody List<CompanyRequest> companiesToCreate) throws MissingParameterException, EntityNotFoundException {
        List<Company> companies = companyService.update(companiesToCreate);
        return ResponseEntity.ok(companies);
    }

    @ResponseBody
    @DeleteMapping(value = "/bulk")
    public ResponseEntity deleteBulk(@RequestBody List<Long> companiesDelete) throws DuplicatedEntityException, MissingParameterException, EntityNotFoundException {
        companyService.delete(companiesDelete);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @ResponseBody
    @PutMapping
    public ResponseEntity<Company> update(@RequestBody CompanyRequest companyToUpdate) throws DuplicatedEntityException, MissingParameterException, EntityNotFoundException {
        Company company = companyService.update(companyToUpdate);
        return ResponseEntity.ok(company);
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CompanyRequest companyDTO) throws DuplicatedEntityException, MissingParameterException {
        Company company = companyService.create(companyDTO);
        return new ResponseEntity<Company>(company, HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping(value = "/{id}")
    public ResponseEntity<Company> getById(@PathVariable Long id) throws EntityNotFoundException {
        Company company = companyService.findById(id);
        return ResponseEntity.ok(company);
    }
}
