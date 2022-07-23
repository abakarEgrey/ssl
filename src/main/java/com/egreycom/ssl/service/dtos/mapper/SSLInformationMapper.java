package com.egreycom.ssl.service.dtos.mapper;

import com.egreycom.ssl.service.dtos.SSLInformationDTO;
import com.egreycom.ssl.data.domain.SSLInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SSLInformationMapper {

    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "issuer", target = "issuer")
    @Mapping(source = "isValid", target = "isValid")
    SSLInformationDTO mapSSLInformationToSSLInformationDto(SSLInformation sslInformation);
}
