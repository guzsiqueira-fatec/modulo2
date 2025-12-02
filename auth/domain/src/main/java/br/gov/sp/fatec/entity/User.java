package br.gov.sp.fatec.entity;

import java.util.UUID;

public record User(
    String id,
    String name,
    String email,
    String password
) {
}
