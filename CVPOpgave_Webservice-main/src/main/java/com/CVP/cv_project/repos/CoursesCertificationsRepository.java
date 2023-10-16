package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CoursesCertification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CoursesCertificationsRepository extends CrudRepository<CoursesCertification,Integer> {
    @Query(value = "SELECT cc FROM CoursesCertification cc WHERE cc.cv.id = :#{#cv.id} AND cc.title =:#{#title}" )
    Optional<CoursesCertification> findByTitleAndCV(@Param("title")String title, @Param("cv") CV cv);

    @Query(value = "SELECT cc FROM CoursesCertification cc WHERE cc.cv.id = :#{#cv.id}" )
    List<CoursesCertification> findAllByCV(CV cv);

    @Query(value = "SELECT cc FROM CoursesCertification cc WHERE cc.cv.id = :#{#cvId}" )
    List<CoursesCertification> findAllByCVID(int cvId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CoursesCertification cc WHERE cc.cv.id = :cvID")
    void deleteAllByCV(int cvID);
}
