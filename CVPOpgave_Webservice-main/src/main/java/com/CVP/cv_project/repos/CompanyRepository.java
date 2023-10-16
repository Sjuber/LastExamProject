package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CompanyRepository extends CrudRepository<Company,Integer> {
    @Query("SELECT comp FROM Company comp WHERE comp.name =:name")
    Optional<Company> findByName(String name);
}
