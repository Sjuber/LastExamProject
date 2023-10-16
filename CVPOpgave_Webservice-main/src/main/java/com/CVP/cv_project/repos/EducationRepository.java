package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CoursesCertification;
import com.CVP.cv_project.domain.Education;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface EducationRepository extends CrudRepository<Education, Integer> {
    //READ
    @Query(value = "SELECT e FROM Education e WHERE e.cv.id = :#{#cv.id} AND e.title =:#{#title}" )
    Optional<Education> findByTitleAndCV(@Param("title")String title, @Param("cv") CV cv);

    @Query(value = "SELECT e FROM Education e WHERE e.cv.id = :#{#cvId}" )
    List<Education> findAllByCVID(int cvId);

    //DELETE
    @Transactional
    @Modifying
    @Query("DELETE FROM Education e WHERE e.cv.id = :cvID")
    void deleteAllByCV(int cvID);

}
