package com.egreycom.ssl.service;

import com.egreycom.ssl.data.SSLInformationRepository;
import com.egreycom.ssl.data.domain.SSLInformation;
import com.egreycom.ssl.service.dtos.SSLInformationDTO;
import com.egreycom.ssl.service.dtos.SSLInformationInputDTO;
import com.egreycom.ssl.service.dtos.mapper.SSLInformationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class SSLInformationService {
    private final SSLInformationRepository sslInformationRepository;

    private URL url;
    private SSLContext sslContext;
    private HttpsURLConnection connection;

    private SSLInformationMapper sslInformationMapper = Mappers.getMapper(SSLInformationMapper.class);

    public ResponseEntity<List<SSLInformationDTO>> fetchSSLCertificateInformation(SSLInformationInputDTO sslInformationInputDTO) throws Exception {
        List<SSLInformation> sslInformations = getCertificates(sslInformationInputDTO.getUrl());

        if (CollectionUtils.isEmpty(sslInformations)) {
            return ResponseEntity.badRequest().build();
        }

        List<SSLInformationDTO> sslInformationDTOS = new ArrayList<>();

        for (SSLInformation sslInformation : sslInformations) {

            // find in database if certificate exists
            Optional<SSLInformation> bySubjectAndIssuerAndIsValid = sslInformationRepository.findBySubjectAndIssuerAndIsValid(sslInformation.getSubject(), sslInformation.getIssuer(), sslInformation.getIsValid());
            if (bySubjectAndIssuerAndIsValid.isPresent()) {
                sslInformationDTOS.add(sslInformationMapper.mapSSLInformationToSSLInformationDto(bySubjectAndIssuerAndIsValid.get()));
            } else {
                // save the certificate in database
                sslInformationRepository.save(sslInformation);
                sslInformationDTOS.add(sslInformationMapper.mapSSLInformationToSSLInformationDto(sslInformation));
            }
        }

        return ResponseEntity.ok(sslInformationDTOS);
    }

    private List<SSLInformation> getCertificates(String url) throws MalformedURLException, NoSuchAlgorithmException, KeyManagementException {
        this.url = new URL(url);
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            private X509Certificate[] accepted;

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                accepted = chain;
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return accepted;
            }
        }}, null);
        try {
            this.connection = (HttpsURLConnection) this.url.openConnection();
        } catch (IOException e) {
            return null;
        }
        this.connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        connection.setSSLSocketFactory(sslContext.getSocketFactory());
        List<SSLInformation> sslInformations = null;
        try {
            if (connection.getResponseCode() == 200) {
                sslInformations = new ArrayList<>();
                Certificate[] certificates = connection.getServerCertificates();
                for (Certificate certificate : certificates) {
                    String subject = ((X509Certificate) certificate).getSubjectDN().getName();
                    String issuer = ((X509Certificate) certificate).getIssuerDN().getName();
                    Date notAfter = ((X509Certificate) certificate).getNotAfter();
                    String isValid = "False";

                    if (notAfter.after(new Date())) {
                        isValid = "True";
                    }

                    SSLInformation sslInformation = SSLInformation.builder()
                            .subject(subject)
                            .issuer(issuer)
                            .isValid(isValid)
                            .build();

                    sslInformations.add(sslInformation);
                }
            }
        } catch (IOException e) {
            log.error("can not connect to specific url {} : {}", url, e.getCause());
            return null;
        }
        connection.disconnect();
        return sslInformations;
    }
}
