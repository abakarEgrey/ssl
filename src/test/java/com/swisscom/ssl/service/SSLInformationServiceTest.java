package com.swisscom.ssl.service;

import com.swisscom.ssl.data.SSLInformationRepository;
import com.swisscom.ssl.data.domain.SSLInformation;
import com.swisscom.ssl.service.dtos.SSLInformationDTO;
import com.swisscom.ssl.service.dtos.SSLInformationInputDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SSLInformationServiceTest {

    @Mock
    private SSLInformationRepository sslInformationRepository;

    @InjectMocks
    private SSLInformationService sslInformationService;


    @Test
    void givenABadUrl_WhenFetchingCertificates_ThenGetBadRequestResponse() throws Exception {
        // Given
        String url = "https://www.badrequest.com";
        SSLInformationInputDTO sslInformationInputDTO = SSLInformationInputDTO.builder()
                .url(url)
                .build();

        // When
        ResponseEntity<List<SSLInformationDTO>> response = sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void givenAValidUrl_WhenFetchingCertificates_ThenGetFromDatabase() throws Exception {
        // Given
        String url = "https://www.google.com";
        SSLInformationInputDTO sslInformationInputDTO = SSLInformationInputDTO.builder()
                .url(url)
                .build();

        SSLInformation sslInformation = SSLInformation.builder()
                .subject("www.google")
                .issuer("issuer.com")
                .isValid("True")
                .build();

        when(sslInformationRepository.findBySubjectAndIssuerAndIsValid(anyString(), anyString(), anyString())).thenReturn(Optional.of(sslInformation));

        // When
        ResponseEntity<List<SSLInformationDTO>> response = sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(sslInformationRepository, times(3)).findBySubjectAndIssuerAndIsValid(anyString(), anyString(), anyString());
    }

    @Test
    void givenAValidUrl_WhenFetchingCertificates_ThenGetFromRemoteServer() throws Exception {
        // Given
        String url = "https://www.google.com";
        SSLInformationInputDTO sslInformationInputDTO = SSLInformationInputDTO.builder()
                .url(url)
                .build();

        when(sslInformationRepository.findBySubjectAndIssuerAndIsValid(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

        // When
        ResponseEntity<List<SSLInformationDTO>> response = sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(3);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(sslInformationRepository, times(3)).findBySubjectAndIssuerAndIsValid(anyString(), anyString(), anyString());
        verify(sslInformationRepository, times(3)).save(any(SSLInformation.class));
    }
}