package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Role;
import com.CVP.cv_project.dtos.RoleDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role,Integer> {

    Role findByTitle(String title);

    @Query("SELECT r FROM Role r JOIN r.userRole ur JOIN ur.user u WHERE u.phone = ?1")
    List<Role> findByUserPhone(String phone);
}
