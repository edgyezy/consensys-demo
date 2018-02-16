package com.consensys.demo.web.content;

import com.consensys.demo.web.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Repository
public interface ContentIndexRepository extends JpaRepository<UserContent, Integer> {

    @Query("select c from UserContent c where (:owner is null or c.owner = :owner)")
    List<UserContent> findByOwner(@Param("owner") Account account);

    UserContent findByContentId(String contentId);

}
