package com.egreycom.ssl.data;

import com.egreycom.ssl.data.domain.SSLInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SSLInformationRepository extends JpaRepository<SSLInformation, Long> {
    Optional<SSLInformation> findBySubjectAndIssuerAndIsValid(String subject, String issuer, String isValid);
}
