package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.PractisedLanguage;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PractisedLanguageRepository extends CrudRepository<PractisedLanguage, Integer> {
    @Query(value = "SELECT pl FROM PractisedLanguage pl WHERE pl.cv.id = :#{#cv.id} AND pl.name =:#{#name}" )
    Optional<PractisedLanguage> findByNameAndCV(@Param("name")String name, @Param("cv") CV cv);

    @Transactional
    @Modifying
    @Query("DELETE FROM PractisedLanguage p WHERE p.cv.id = :cvID")
    void deleteAllByCV(int cvID);

    @Query(value = "SELECT pl FROM PractisedLanguage pl WHERE pl.cv.id = :#{#cv.id}" )
    List<PractisedLanguage> findAllByCV(CV cv);
}
