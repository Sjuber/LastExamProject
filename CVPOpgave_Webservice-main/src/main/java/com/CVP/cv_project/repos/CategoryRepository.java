package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.KnowCategory;
import com.github.dockerjava.api.exception.NotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<KnowCategory, Integer> {
    Optional<KnowCategory> findByName(String name) throws NotFoundException;
}
