package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends CrudRepository<Project, Integer> {
    @Query("SELECT project FROM Project project JOIN CV cv ON  project.cv.id = cv.id JOIN Company customer ON customer.id = project.customer.id WHERE " +
            "project.description= :#{#projectToAdd.description} " +
            "and cv.id= :#{#cvDB.id} " +
            "and project.roleForProject= :#{#projectToAdd.roleForProject} " +
            "and project.dateStart= :#{#projectToAdd.dateStart} "+
            "and customer.name= :#{#projectToAdd.customer.name}")
    Optional<Project> findByAllAttributesAndCV(@Param("projectToAdd")Project projectToAdd,@Param("cvDB") CV cvDB);

    @Transactional
    @Modifying
    @Query("DELETE FROM Project project WHERE project.id = :id")
    void deleteByProjectID(int id);

    @Query("SELECT project FROM Project project WHERE project.cv.id = :cvID")
    List<Project> findAllByCV(int cvID);
}
