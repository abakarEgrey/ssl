package com.swisscom.ssl.controller;

import com.swisscom.ssl.service.SSLInformationService;
import com.swisscom.ssl.service.dtos.SSLInformationDTO;
import com.swisscom.ssl.service.dtos.SSLInformationInputDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fetch")
@Log4j2
@Validated
@RequiredArgsConstructor
public class SSLInformationController {
    private final SSLInformationService sslInformationService;

    @PostMapping
    public ResponseEntity<List<SSLInformationDTO>> fetchSSLCertificateInformation(@RequestBody SSLInformationInputDTO sslInformationInputDTO) throws Exception {
        return sslInformationService.fetchSSLCertificateInformation(sslInformationInputDTO);
    }
}
