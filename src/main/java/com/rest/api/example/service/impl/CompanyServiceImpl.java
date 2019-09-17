package com.rest.api.example.service.impl;

import com.rest.api.example.domain.model.Company;
import com.rest.api.example.domain.repository.CompanyRepository;
import com.rest.api.example.domain.request.CompanyRequest;
import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.MissingParameterException;
import com.rest.api.example.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vbarros on 16/09/2019 .
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyServiceImpl.class);

    private final CompanyRepository companyRepository;


    public CompanyServiceImpl(@Autowired  CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Company findById(Long id){
        try{
            return companyRepository.getOne(id);
        }catch (javax.persistence.EntityNotFoundException ex){
            throw new EntityNotFoundException(Company.class, "id", id.toString());
        }

    }

    @Override
    public Long getSize() {
        return companyRepository.count();
    }

    @Override
    @Transactional
    public List<Company> create(List<CompanyRequest> companiesToCreate) throws MissingParameterException {
        List<Company> companies = new ArrayList<>();
        for (CompanyRequest companyDTO : companiesToCreate) {
            try {
                //let' validateCreate mandatory fields
                validateCreate(companyDTO);
                companies.add(buildCompany(companyDTO));
            }catch ( DuplicatedEntityException ex){
                LOGGER.error("Company already in salesforce, let's skip it");
            }
        }
        if (!companies.isEmpty()) {
            companyRepository.saveAll(companies);
        }

        return companies;
    }

    @Override
    @Transactional
    public Company create(CompanyRequest companyDTO) throws DuplicatedEntityException, MissingParameterException {
        validateCreate(companyDTO);
        Company company = buildCompany(companyDTO);
        companyRepository.save(company);
        return company;
    }


    @Override
    @Transactional
    public List<Company> update(List<CompanyRequest> companiesToUpdate) throws EntityNotFoundException, MissingParameterException {
        List<Company> companies = new ArrayList<>();
        for (CompanyRequest companyDTO : companiesToUpdate) {
            Company company = getCompanyUpdated(companyDTO);
            companies.add(company);
        }
        companyRepository.saveAll(companies);
        return companies;
    }

    private Company getCompanyUpdated(CompanyRequest companyToUpdate) throws MissingParameterException, EntityNotFoundException {
        validateFields(companyToUpdate);
        if (companyToUpdate.getId() == null) {
            throw new MissingParameterException("company", "id");
        }
        Company company = findById(companyToUpdate.getId());
        updateCompanyFields(company, companyToUpdate);
        return company;
    }

    @Override
    @Transactional
    public Company update(CompanyRequest companyToUpdate) throws EntityNotFoundException, MissingParameterException {
        Company company = getCompanyUpdated(companyToUpdate);
        companyRepository.save(company);
        return company;
    }

    @Override
    @Transactional
    public void delete(List<Long> companiesDelete) throws EntityNotFoundException {
        int deleted = companyRepository.softDeleteByIds(companiesDelete);
        if (deleted != companiesDelete.size()) {
            throw new EntityNotFoundException(Company.class, "externalId", companiesDelete.toString());
        }
    }

    @Override
    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        int deleted = companyRepository.softDeleteById(id);
        if (deleted == 0) {
            throw new EntityNotFoundException(Company.class, "externalId", id.toString());
        }
    }

    private void validateFields(CompanyRequest companyDTO) throws MissingParameterException {
        if (StringUtils.isEmpty(companyDTO.getName())) {
            throw new MissingParameterException("company", "name");
        }
        if (StringUtils.isEmpty(companyDTO.getWebsite())) {
            throw new MissingParameterException("company", "website");
        }

    }

    private void validateCreate(CompanyRequest companyDTO) throws MissingParameterException, DuplicatedEntityException {
        validateFields(companyDTO);
        //first let's check if company is already in our system with same name
        Company company = companyRepository.findByNameAndDeleted(companyDTO.getName(), false);
        if (company != null) {
            throw new DuplicatedEntityException("company");
        }
    }

    private void updateCompanyFields(Company company, CompanyRequest dto) {
        company.setName(dto.getName());
        if (!StringUtils.isEmpty(dto.getWebsite())) {
            company.setWebsite(dto.getWebsite());
        } else {
            company.setWebsite(null);
        }

    }

    private Company buildCompany(CompanyRequest companyDTO) {
        Company company = new Company();
        updateCompanyFields(company, companyDTO);
        return company;
    }
}
