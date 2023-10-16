package com.CVP.cv_project.repos;

import com.CVP.cv_project.domain.CVs_Knowledge;
import com.CVP.cv_project.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User findByName(String name);
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);

    @Query(" SELECT DISTINCT user FROM User user JOIN CV cv ON user.id = cv.consultant.id " +
            " WHERE cv.cvState = 'Udgivet' ")
    Page<User> findAllPagedConsultantsWhoHasPublishedCVs(Pageable pageable);

    @Query(value = "select "
            + "(case"
            + " when :#{#knowledgeName} = any (SELECT ck.knowledge.name FROM ck)"
            + " and :#{#level} = any (SELECT ck.levelSkill FROM ck)"
            + " then (1 * (1 + :#{#index}))"
            + " when :#{#knowledgeName} = any (SELECT ck.knowledge.name FROM ck)"
            + " then (2 * (1 + :#{#index}))"
            + " end) as sortScore,"
            + " u as userDB "
            + " FROM User u"
            + " left join CV cv ON cv.consultant.id = u.id"
            + " left join CVs_Knowledge ck on ck.cv.id = cv.id"
            + " where u.id = any (SELECT ck.cv.consultant.id from ck)"
            + " group by u.id"
            + " order by case"
            + " when :#{#knowledgeName} = any (SELECT ck.knowledge.name FROM ck)"
            + " and :#{#level} = any (SELECT ck.levelSkill FROM ck)"
            + " then 0"
            + " when :#{#knowledgeName} = any (SELECT ck.knowledge.name FROM ck)"
            + " then 1"
            + " else 2"
            + " end "
            + " asc "
    )
    Page<Tuple> findAllUsersWithPublishedCVsAndCVKnowledgeFromListPerfectMatchOrBetter(
            @Param("knowledgeName") String knowledgeName, @Param("level") String cvKnowledgeLevel, @Param("index") int indexForCVKnowledge, Pageable pageable);

    @Query(" SELECT DISTINCT user FROM User user JOIN CV cv ON user.id = cv.consultant.id JOIN CVs_Knowledge ck ON ck.cv.id = cv.id" +
            " WHERE cv.cvState = 'Udgivet' AND NOT ck.knowledge.name = :#{#cvKnowledgeList[0].knowledge.name}")
    List<User> findAllUsersWithPublishedCVsButNothingFromCVKnowledgeFromList(@Param("cvKnowledgeList") List<CVs_Knowledge> cVs_knowledgeList);

}
