package com.consensys.demoweb.content;

import com.consensys.demoweb.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Repository
public interface UserContentRepository extends JpaRepository<UserContent, Integer> {

    List<UserContent> findByOwner(Account account);
    List<UserContent> findByContentType(String contentType);

}
