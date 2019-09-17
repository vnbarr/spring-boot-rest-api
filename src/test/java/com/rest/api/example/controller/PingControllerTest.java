package com.rest.api.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.api.example.service.CompanyService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.assertTrue;

/**
 * Created by vbarros on 16/09/2019.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = PingController.class)
public class PingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CompanyService companyService;

    @Test
    public void testPing() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/ping"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String responseBodyContent = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(responseBodyContent);
        assertTrue(jsonResponse.getString("version").contains("v"));
    }
}
