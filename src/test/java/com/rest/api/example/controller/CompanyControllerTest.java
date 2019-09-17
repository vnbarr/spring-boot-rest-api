package com.rest.api.example.controller;

import com.rest.api.example.JsonUtilsTest;
import com.rest.api.example.domain.model.Company;
import com.rest.api.example.domain.request.CompanyRequest;
import com.rest.api.example.exception.DuplicatedEntityException;
import com.rest.api.example.exception.EntityNotFoundException;
import com.rest.api.example.exception.MissingParameterException;
import com.rest.api.example.service.CompanyService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by vbarros on 16/09/2019 .
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = CompanyController.class)
public class CompanyControllerTest {
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;


    @Test
    public void testGetAllCompaniesWithNoParameters() throws Exception {
        List<Company> companies = new ArrayList<Company>();
        companies.add(new Company());
        when(companyService.findAll(null)).thenReturn(companies);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get("/companies"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONArray jsonResponse = new JSONArray(responseBodyContent);
        assertEquals(1L, jsonResponse.length());
    }

    @Test
    public void testGetAllCompaniesWithDeletedTrue() throws Exception {
        List<Company> companies = new ArrayList<Company>();
        companies.add(new Company());
        when(companyService.findAll(Boolean.TRUE)).thenReturn(companies);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get("/companies?deleted=true"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONArray jsonResponse = new JSONArray(responseBodyContent);
        assertEquals(1L, jsonResponse.length());
    }

    @Test
    public void testGetAllCompaniesWithDeletedFalse() throws Exception {
        List<Company> companies = new ArrayList<Company>();
        companies.add(new Company());
        when(companyService.findAll(Boolean.FALSE)).thenReturn(companies);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get("/companies?deleted=false"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONArray jsonResponse = new JSONArray(responseBodyContent);
        assertEquals(1L, jsonResponse.length());
    }

    @Test
    public void testGetNonExistingCompanyByIdThrowsError() throws Exception {
        when(companyService.findById(1L)).thenThrow(new EntityNotFoundException(Company.class, "id", "1"));
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.
                get("/companies/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("NOT_FOUND", jsonResponse.getString("status"));
        assertEquals("Company", jsonResponse.getString("keyword"));
    }

    @Test
    public void testGetExistingCompanyById() throws Exception {
        Company company = new Company();
        company.setName("Sprig");
        company.setWebsite("sprig.com");
        company.setId(100L);
        when(companyService.findById(100L)).thenReturn(company);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/companies/100"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("Sprig", jsonResponse.getString("name"));
        assertEquals("sprig.com", jsonResponse.getString("website"));
    }

    @Test
    public void testCreateCompanyMissingParamsThrowsError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/companies/bulk"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCompanyMissingMandatoryFieldThrowsError() throws Exception {
        when(companyService.create(anyList())).thenThrow(new MissingParameterException("company", "website"));
        String jsonCompany = "[{\"name\":\"Test\"}]";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("company", jsonResponse.getString("keyword"));
    }

    @Test
    public void testCreateDuplicatedCompanyThrowsError() throws Exception {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Test");
        companyDTO.setWebsite("www.test.com");
        List<CompanyRequest> companies = new ArrayList<CompanyRequest>();
        companies.add(companyDTO);
        String jsonCompany = JsonUtilsTest.fromJsonToObject(companies);
        when(companyService.create(companies)).thenThrow(new DuplicatedEntityException("company"));
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("company", jsonResponse.getString("keyword"));
    }


    @Test
    public void testCreateCompanyWithMandatoryFields() throws Exception {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Test");
        companyDTO.setWebsite("www.test.com");
        List<CompanyRequest> companies = new ArrayList<CompanyRequest>();
        companies.add(companyDTO);
        String jsonCompany = JsonUtilsTest.fromJsonToObject(companies);

        Company company = new Company();
        company.setName("Test");
        company.setId(1L);

        List<Company> companyList = new ArrayList<>();
        companyList.add(company);
        when(companyService.create(companies)).thenReturn(companyList);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.post("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONArray jsonResponse = new JSONArray(responseBodyContent);
        assertEquals("Test", jsonResponse.getJSONObject(0).getString("name"));
        assertEquals(1L, jsonResponse.getJSONObject(0).getLong("id"));
    }



    @Test
    public void testUpdateNonExistingCompanyThrowsError() throws Exception {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setName("Test");
        companyDTO.setId(10L);
        companyDTO.setWebsite("www.test.com");

        when(companyService.update(anyList())).thenThrow(new EntityNotFoundException(Company.class, "id", "10L"));
        List<CompanyRequest> companies = new ArrayList<CompanyRequest>();
        companies.add(companyDTO);
        String jsonCompany = JsonUtilsTest.fromJsonToObject(companies);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("NOT_FOUND", jsonResponse.getString("status"));
        assertEquals("Company", jsonResponse.getString("keyword"));
    }

    @Test
    public void testUpdateExistingCompanyWithNoErrors() throws Exception {
        CompanyRequest companyDTO = new CompanyRequest();
        companyDTO.setId(500L);
        companyDTO.setName("KWI Test edited");
        companyDTO.setWebsite("test.com");
        List<CompanyRequest> companies = new ArrayList<CompanyRequest>();
        companies.add(companyDTO);
        String jsonCompany = JsonUtilsTest.fromJsonToObject(companies);
        List<Company> companiesUpdated = new ArrayList<>();
        Company comp = new Company();
        comp.setName("KWI Test");
        companiesUpdated.add(comp);
        when(companyService.update(companies)).thenReturn(companiesUpdated);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.put("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONArray jsonResponse = new JSONArray(responseBodyContent);
        assertEquals("KWI Test", jsonResponse.getJSONObject(0).getString("name"));

    }


    @Test
    public void testDeleteNonExistingCompanyByIdThrowsError() throws Exception {
        doThrow(new EntityNotFoundException(Company.class, "Id", "25")).when(companyService).delete(anyList());
        String jsonCompany = "[\"25\"]";
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.delete("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();

        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertEquals("NOT_FOUND", jsonResponse.getString("status"));
        assertEquals("Company", jsonResponse.getString("keyword"));
    }

    @Test
    public void testDeleteExistingCompanyBySalesforceId() throws Exception {
        String jsonCompany = "[\"25\"]";
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/companies/bulk")
                        .content(jsonCompany)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }




}