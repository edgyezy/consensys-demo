package com.consensys.demoweb.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Calvin Ngo on 13/2/18.
 */

@Repository
public interface UserRepository extends JpaRepository<Account, Integer> {

    @Query("select a from Account a where a.enabled = true and a.username = :username")
    Account findByUsername(@Param("username") String username);

    @Query("select a from Account a where a.enabled = true order by a.username")
    List<Account> findAllAccounts();

}
