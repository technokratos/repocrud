package org.repocrud.repository;

import java.util.List;
import java.util.UUID;
import org.repocrud.domain.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter, UUID>, JpaSpecificationExecutor<Filter> {


    List<Filter> findByEntityOrderByPositionAsc(String entity);
}
