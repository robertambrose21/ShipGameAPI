package com.robert.shipgame.account.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountDAO, UUID> {

    @Query("SELECT account FROM Account account WHERE account.oauth2Sub = :oauth2Sub")
    Optional<AccountDAO> findByOAuth2Sub(@Param("oauth2Sub") String oauth2Sub);

}
