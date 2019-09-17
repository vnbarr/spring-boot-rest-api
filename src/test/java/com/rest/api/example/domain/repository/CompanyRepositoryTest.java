package com.rest.api.example.domain.repository;

import com.rest.api.example.domain.model.Company;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vbarros on 16/09/2019 .
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    public void testGetCompanyByNameAndDeleted() {
        Company company = companyRepository.findByNameAndDeleted("Uber", false);
        assertNotNull(company);
        assertEquals("Uber", company.getName());
    }

    @Test
    public void testGetCompanyByNameAndDeletedWhenDeleted() {
        Company company = companyRepository.findByNameAndDeleted("Fruta", false);
        assertNull(company);
    }

    @Test
    public void testGetNonExistingCompanyByNameAndDeleted() {
        Company company = companyRepository.findByNameAndDeleted("Frutaaaa", false);
        assertNull(company);
    }

    @Test
    public void testGetCompaniesByOrderByNameAsc() {
        List<Company> companies = companyRepository.findAllByOrderByNameAsc();
        assertEquals(5, companies.size());
    }

    @Test
    public void testSoftDeleteById() {
        Company companyBefore = companyRepository.getOne(4L);
        Assertions.assertThat(companyBefore.isDeleted()).isFalse();
        int rowCount = companyRepository.softDeleteById(4L);
        Assertions.assertThat(rowCount).isEqualTo(1);
        Company companyAfter = companyRepository.getOne(4L);
        Assertions.assertThat(companyAfter.isDeleted()).isTrue();
    }

    @Test
    public void testSoftDeleteByIds() {
        Company companyBefore = companyRepository.getOne(3L);
        Assertions.assertThat(companyBefore.isDeleted()).isFalse();
        companyBefore = companyRepository.getOne(4L);
        Assertions.assertThat(companyBefore.isDeleted()).isFalse();
        int rowCount = companyRepository.softDeleteByIds(Arrays.asList(3L, 4L));
        Assertions.assertThat(rowCount).isEqualTo(2);
        Company companyAfter = companyRepository.getOne(3L);
        Assertions.assertThat(companyAfter.isDeleted()).isTrue();
        companyAfter = companyRepository.getOne(4L);
        Assertions.assertThat(companyAfter.isDeleted()).isTrue();
    }

    @Test
    public void testSoftDeleteByIdNotFound() {
        int rowCount = companyRepository.softDeleteById(999L);
        Assertions.assertThat(rowCount).isEqualTo(0);
    }

}
