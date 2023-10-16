package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Knowledge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgeRepository extends CrudRepository<Knowledge,Integer> {
    Knowledge findByName(String name);

    @Query("SELECT k FROM Knowledge k")
    List<Knowledge> findAllKnowledge();

    @Query("SELECT k FROM Knowledge k WHERE k.name != :nameOfKnowledge or k.name = null")
    List<Knowledge> findOthersThanByName(String nameOfKnowledge);
}