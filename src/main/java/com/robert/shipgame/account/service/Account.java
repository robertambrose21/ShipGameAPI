package com.robert.shipgame.account.service;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Account(UUID id,
                      String firstName,
                      String lastName,
                      String email,
                      String oauth2Sub) {
}
