package com.rest.api.example.service;

import com.rest.api.example.domain.model.Company;
import com.rest.api.example.domain.request.CompanyRequest;
import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.MissingParameterException;

import java.util.List;

/**
 * Created by vbarros on 16/09/2019.
 */
public interface CompanyService {
    List<Company> findAll(Boolean deleted);
    Company findById(Long id);
    Long getSize();
    List<Company> create(List<CompanyRequest> companiesToCreate) throws DuplicatedEntityException, MissingParameterException;

    Company create(CompanyRequest companyDTO) throws DuplicatedEntityException, MissingParameterException;

    List<Company> update(List<CompanyRequest> companiesToCreate) throws EntityNotFoundException, MissingParameterException;

    Company update(CompanyRequest companyToUpdate) throws EntityNotFoundException, MissingParameterException;

    void delete(List<Long> companiesDelete) throws EntityNotFoundException;

    void delete(Long id) throws EntityNotFoundException;
}
