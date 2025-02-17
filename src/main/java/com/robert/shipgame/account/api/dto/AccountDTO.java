package com.robert.shipgame.account.api.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AccountDTO(UUID id,
                      String firstName,
                      String lastName,
                      String email,
                      String oauth2Sub) {
}
