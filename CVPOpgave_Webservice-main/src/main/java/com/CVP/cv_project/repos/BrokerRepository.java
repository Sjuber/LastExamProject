package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.Broker;
import com.CVP.cv_project.domain.Project;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BrokerRepository extends CrudRepository<Broker, Integer> {
    @Query("SELECT broker FROM Broker broker WHERE broker.project.id =:projectID")
    Project findByProjectId(Integer projectID);

    @Transactional
    @Modifying
    @Query("DELETE FROM Broker broker WHERE broker.project.id =:projectID")
    void deleteByProjectId(Integer projectID);
}
