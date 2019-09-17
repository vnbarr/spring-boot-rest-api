package com.rest.api.example.service;

import com.rest.api.example.domain.model.Company;
import com.rest.api.example.domain.repository.CompanyRepository;
import com.rest.api.example.domain.request.CompanyRequest;
import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.MissingParameterException;
import com.rest.api.example.service.impl.CompanyServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by vbarros on 16/09/2019 .
 */
@RunWith(SpringRunner.class)
public class CompanyServiceTest {

    private CompanyService companyService;

    @Mock
    private CompanyRepository repository;

    @Before
    public void setUp() {
        companyService = new CompanyServiceImpl(repository);
    }

    @Test
    public void testCreateCompany(){
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sprig");
        companyDTO.setWebsite("www.sprig.com.ar");

        try {
            Company company = companyService.create(companyDTO);
            verify(repository).save(any(Company.class));
            assertEquals(companyDTO.getName(), company.getName());
            assertEquals(companyDTO.getWebsite(), company.getWebsite());
        } catch (DuplicatedEntityException | MissingParameterException e) {
            fail();
        }
    }


    @Test(expected = MissingParameterException.class)
    public void testCreateCompanyWithMissingNameThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setWebsite("www.sprig.com.ar");
        companyService.create(companyDTO);
        verify(repository, never()).save(any(Company.class));
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCompanyWithMissingWebsiteThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sprig");
        companyService.create(companyDTO);
        verify(repository, never()).save(any(Company.class));
    }


    @Test(expected = DuplicatedEntityException.class)
    public void testCreateCompanyDuplicatedByIdThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sprig");
        companyDTO.setWebsite("www.sprig.com.ar");

        when(repository.findByNameAndDeleted("Sprig", false)).thenReturn(new Company());
        companyService.create(companyDTO);
        verify(repository, never()).save(any(Company.class));
    }


    @Test
    public void testCreateCompanyBulk(){
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sprig");
        companyDTO.setWebsite("www.sprig.com.ar");
        List<CompanyRequest> dtos = new ArrayList<>();
        dtos.add(companyDTO);
        try {
            List<Company> companies = companyService.create(dtos);
            verify(repository).saveAll(anyList());
            assertEquals(companyDTO.getName(), companies.get(0).getName());
            assertEquals(companyDTO.getWebsite(), companies.get(0).getWebsite());
        } catch (DuplicatedEntityException | MissingParameterException e) {
            fail();
        }
    }


    @Test(expected = MissingParameterException.class)
    public void testCreateCompanyBulkWithMissingNameThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setWebsite("www.sprig.com.ar");
        List<CompanyRequest> dtos = new ArrayList<>();
        dtos.add(companyDTO);
        companyService.create(dtos);
        verify(repository, never()).save(any(Company.class));
    }

    @Test(expected = MissingParameterException.class)
    public void testCreateCompanyBulkWithMissingWebsiteThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("sprig");
        List<CompanyRequest> dtos = new ArrayList<>();
        dtos.add(companyDTO);
        companyService.create(dtos);
        verify(repository, never()).save(any(Company.class));
    }

    @Test
    public void testCreateCompanyBulkDuplicatedByIdThrowsError() throws DuplicatedEntityException, MissingParameterException {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Sprig");
        companyDTO.setWebsite("www.sprig.com.ar");

        List<CompanyRequest> dtos = new ArrayList<>();
        dtos.add(companyDTO);
        when(repository.findByNameAndDeleted("Sprig", false)).thenReturn(new Company());
        companyService.create(dtos);
        verify(repository, never()).save(any(Company.class));
    }

    @Test
    public void testDeleteCompany(){
        when(repository.softDeleteById(1L)).thenReturn(1);
        companyService.delete(1L);

        verify(repository).softDeleteById(1L);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteCompanyWhenCompanyNotFound(){
        when(repository.softDeleteById(15L)).thenReturn(0);
        companyService.delete(1L);

        verify(repository).softDeleteById(15L);

    }

    @Test
    public void testDeleteBulkCompany(){
        List<Long> companiesDelete = new ArrayList<>();
        companiesDelete.add(1L);
        when(repository.softDeleteByIds(companiesDelete)).thenReturn(1);
        companyService.delete(companiesDelete);
        verify(repository).softDeleteByIds(companiesDelete);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDeleteBulkCompanyWhenCompanyNotFound(){
        List<Long> companiesDelete = new ArrayList<>();
        companiesDelete.add(1L);
        when(repository.softDeleteByIds(companiesDelete)).thenReturn(0);
        companyService.delete(companiesDelete);
        verify(repository).softDeleteByIds(companiesDelete);

    }

}
