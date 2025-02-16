package com.robert.shipgame.account.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity(name = "Account")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

}
