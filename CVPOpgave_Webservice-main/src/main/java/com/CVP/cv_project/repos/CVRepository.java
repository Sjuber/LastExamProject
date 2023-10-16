package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.CVState;
import com.CVP.cv_project.dtos.SimpleCVDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import net.minidev.asm.ex.NoSuchFieldException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CVRepository extends PagingAndSortingRepository<CV,Integer> {

    @Query("SELECT cv FROM CV cv JOIN cv.author author WHERE author.phone = ?1")
    Optional<List<CV>> findAllCVsByAuthorPhone(String phone);

    @Query("SELECT cv FROM CV cv WHERE cv.cvOriginal.id = :#{#cvOriginal.id}")
    List<CV> findCVByCVOriginal(@Param("cvOriginal")CV cvOriginal);

    @Query("SELECT cv FROM CV cv WHERE cv.cvState = 'Udgivet'")
    List<CV> findAllPublished();

    @Query("SELECT cv FROM CV cv JOIN User cons ON  cv.consultant.id = cons.id JOIN User author ON cv.author.id = author.id WHERE " +
            "cons.name = :#{#simpleCV.consultantName} " +
            "and cons.email= :#{#simpleCV.consultantEmail} " +
            "and cons.phone= :#{#simpleCV.consultantPhone} " +
            "and cv.description= :#{#simpleCV.description} "+
            "and cv.bookedHours= :#{#simpleCV.bookedHours} "+
            "and cv.maxHours= :#{#simpleCV.maxHours} "+
            "and cv.techBackground= :#{#simpleCV.techBackground} "+
            "and author.phone= :authorPhone "+
            "and cv.cvState = 'Udgivet'")

    Optional<CV>findByAllAttributes(
            @Param("simpleCV")SimpleCVDTO simpleCV,
            @Param("authorPhone") String authorPhone
    )
            throws NoSuchFieldException;

    @Query("SELECT cv FROM CV cv JOIN User cons ON  cv.consultant.id = cons.id JOIN User author ON cv.author.id = author.id WHERE " +
            "cons.email= :#{#cvToFind.consultant.email} " +
            "and cons.phone= :#{#cvToFind.consultant.phone} " +
            "and cv.description= :#{#cvToFind.description} "+
            "and cv.bookedHours= :#{#cvToFind.bookedHours} "+
            "and cv.maxHours= :#{#cvToFind.maxHours} "+
            "and cv.techBackground= :#{#cvToFind.techBackground} "+
            "and author.phone= :#{#cvToFind.author.phone} " +
            "and cv.cvState= :#{#cvState} " +
            "and cv.title= :#{#cvToFind.title}"
    )
    Optional<CV> findByAllCVAttributes(@Param("cvToFind") CV cvToFind, @Param("cvState") CVState cvState);

    @Query("SELECT cv FROM CV cv inner JOIN User cons ON cv.consultant.id = cons.id " +
            "inner join CVs_Knowledge ck on ck.cv.id = cv.id " +
            "WHERE cons.phone = :#{#phoneFromConsultant} AND cv.cvState = 'Udgivet' ")
    List<CV> findAllPublishedByConsulentAsAuthour(@Param("phoneFromConsultant")String phoneFromConsultant);

}
