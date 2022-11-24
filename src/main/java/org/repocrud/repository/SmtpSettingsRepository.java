package org.repocrud.repository;

import java.util.List;
import java.util.UUID;
import org.repocrud.domain.Company;
import org.repocrud.domain.SmtpSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SmtpSettingsRepository extends JpaRepository<SmtpSettings, UUID>, JpaSpecificationExecutor<SmtpSettings> {

    List<SmtpSettings> findByCompany(Company company);

    Long countByCompany(Company company);
}
