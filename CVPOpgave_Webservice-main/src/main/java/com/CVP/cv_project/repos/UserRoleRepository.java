package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.UserRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRoleRepository extends CrudRepository<UserRole, Integer> {

    @Transactional
    void deleteByIdIn(List<Integer> ids);

}
