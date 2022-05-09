package com.swisscom.ssl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swisscom.ssl.service.SSLInformationService;
import com.swisscom.ssl.service.dtos.SSLInformationDTO;
import com.swisscom.ssl.service.dtos.SSLInformationInputDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SSLInformationController.class)
//@ExtendWith(SpringExtension.class)
//@WebAppConfiguration
class SSLInformationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private SSLInformationService sslInformationService;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .build();
    }

    @Test
    void givenAndURL_WhenTheURLIsValid_Then_ShouldStatus200() throws Exception {
        // Given
        String url = "https://www.google.com";
        SSLInformationInputDTO sslInformationInputDTO = SSLInformationInputDTO.builder()
                .url(url)
                .build();

        SSLInformationDTO sslInformationDTO = SSLInformationDTO.builder()
                .subject("www.google")
                .issuer("issuer.com")
                .isValid("True")
                .build();

        when(sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO)).thenReturn(ResponseEntity.ok(List.of(sslInformationDTO)));

        // When
        mockMvc.perform(
                post("/fetch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sslInformationInputDTO))
        )
        .andDo(print())
        // Then
        .andExpect(status().isOk());
    }

    @Test
    void givenAndURL_WhenTheURLIsNotValid_Then_ShouldStatus400() throws Exception {
        // Given
        String url = "https://www.badrequest.com";
        SSLInformationInputDTO sslInformationInputDTO = SSLInformationInputDTO.builder()
                .url(url)
                .build();
        SSLInformationDTO sslInformationDTO = SSLInformationDTO.builder()
                .subject("www.badrequest.com")
                .issuer("issuer.com")
                .isValid("False")
                .build();

        when(sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO)).thenReturn(ResponseEntity.badRequest().build());

        // When
        mockMvc.perform(
                post("/fetch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sslInformationInputDTO))
        )
        .andDo(print())
        // Then
        .andExpect(status().isOk());
    }
}