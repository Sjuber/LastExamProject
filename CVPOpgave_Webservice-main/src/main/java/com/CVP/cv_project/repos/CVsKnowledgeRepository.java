package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CVs_Knowledge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CVsKnowledgeRepository extends CrudRepository<CVs_Knowledge, Integer> {
    @Query("SELECT cvKnowledge FROM CVs_Knowledge cvKnowledge WHERE cvKnowledge.note = :#{#note} and cvKnowledge.cv.id = :#{#cv.id}")
    Optional<CVs_Knowledge> findByNoteAndCV(@Param("note") String note, @Param("cv") CV cv);
    @Query("SELECT cvKnowledge FROM CVs_Knowledge cvKnowledge WHERE cvKnowledge.cv.id = :#{#cv.id}")

    List<CVs_Knowledge> findAllByCV(CV cv);

    @Query("SELECT cvKnowledge FROM CVs_Knowledge cvKnowledge WHERE cvKnowledge.knowledge.name = :#{#cvKnowledge.knowledge.name} and cvKnowledge.cv.id = :#{#cvKnowledge.cv.id}")
    Optional<CVs_Knowledge> findByKnowledgeNameAndCV(@Param("cvKnowledge") CVs_Knowledge cvKnowledge);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM CVs_Knowledge cvKnowledge WHERE cvKnowledge.id = :#{#cvKnowledge.id}")
    void deleteCVKnowledge(@Param("cvKnowledge")CVs_Knowledge cvKnowledge);

    @Transactional
    @Modifying
    @Query("DELETE FROM CVs_Knowledge cvKnowledge WHERE cvKnowledge.cv.id = :cvID")
    void deleteAllByCV(int cvID);
}
