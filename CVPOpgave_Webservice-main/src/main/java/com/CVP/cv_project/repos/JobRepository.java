package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CV;
import com.CVP.cv_project.domain.Job;
import net.minidev.asm.ex.NoSuchFieldException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends CrudRepository<Job, Integer> {


    @Query("SELECT job FROM Job job JOIN CV cv ON  cv.id = job.cv.id JOIN User author " +
            "On cv.author.id = author.id JOIN User consultant On cv.consultant.id = consultant.id WHERE " +
            "consultant.email = :#{#cvToFind.consultant.email} " +
            "and consultant.phone= :#{#cvToFind.consultant.phone} " +
            "and cv.description= :#{#cvToFind.description} "+
            "and cv.bookedHours= :#{#cvToFind.bookedHours} "+
            "and cv.maxHours= :#{#cvToFind.maxHours} "+
            "and cv.techBackground= :#{#cvToFind.techBackground} "+
            "and author.phone= :#{#cvToFind.author.phone}")

    Optional<List<Job>> findAllByCV(@Param("cvToFind") CV cvTofFind) throws NoSuchFieldException;

    @Query("SELECT job FROM Job job " +
            "WHERE job.title = :#{#jobToFind.title} " +
            "and job.cv.description = :#{#jobToFind.cv.description} " +
            "and job.cv.techBackground = :#{#jobToFind.cv.techBackground} " +
            "and job.cv.maxHours = :#{#jobToFind.cv.maxHours} " +
            "and job.cv.bookedHours = :#{#jobToFind.cv.bookedHours} " +
            "and job.cv.consultant.email = :#{#jobToFind.cv.consultant.email} "
    )
    Optional<Job> findJobWithIDByJobAttributes(@Param("jobToFind") Job jobToFind);

    @Transactional
    @Modifying
    @Query("DELETE FROM Job job WHERE job.cv.id = :cvID")
    void deleteAllByCV(int cvID);
}
