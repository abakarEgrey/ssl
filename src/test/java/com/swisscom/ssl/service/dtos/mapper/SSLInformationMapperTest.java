package com.swisscom.ssl.service.dtos.mapper;

import com.swisscom.ssl.data.domain.SSLInformation;
import com.swisscom.ssl.service.dtos.SSLInformationDTO;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

class SSLInformationMapperTest {

    private SSLInformationMapper sslInformationMapper = Mappers.getMapper(SSLInformationMapper.class);

    @Test
    void mapSSLSslInformationToSSslInformationDto() {
        // Given
        String subject = "www.google.com";
        String issuer = "issuer.com";
        String isValid = "True";

        SSLInformation sslInformation = SSLInformation.builder()
                .subject(subject)
                .issuer(issuer)
                .isValid(isValid)
                .build();
        // When
        SSLInformationDTO sslInformationDTO = sslInformationMapper.mapSSLInformationToSSLInformationDto(sslInformation);

        // Then
        assertThat(sslInformationDTO, allOf(
           hasProperty("subject", is(subject)),
           hasProperty("issuer", is(issuer)),
           hasProperty("isValid", is(isValid))
        ));
    }
}