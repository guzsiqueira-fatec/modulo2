package br.gov.sp.fatec.entity;

public record User(
    String id,
    String name,
    String email,
    String password
) {
}
