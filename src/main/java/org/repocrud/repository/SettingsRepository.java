package org.repocrud.repository;

import java.util.UUID;
import org.repocrud.domain.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, UUID>, JpaSpecificationExecutor<Settings> {


}
