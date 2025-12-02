package br.gov.sp.fatec.model;

import java.time.LocalDateTime;

public record TokenMetadata(
    String subject,
    String issuer,
    LocalDateTime issuedAt,
    LocalDateTime expiresAt,
    String audience,
    Object claims
) {

}
