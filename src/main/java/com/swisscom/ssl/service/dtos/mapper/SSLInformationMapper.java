package com.swisscom.ssl.service.dtos.mapper;

import com.swisscom.ssl.data.domain.SSLInformation;
import com.swisscom.ssl.service.dtos.SSLInformationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SSLInformationMapper {

    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "issuer", target = "issuer")
    @Mapping(source = "isValid", target = "isValid")
    SSLInformationDTO mapSSLInformationToSSLInformationDto(SSLInformation sslInformation);
}
