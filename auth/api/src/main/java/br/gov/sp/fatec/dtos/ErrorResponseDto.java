package br.gov.sp.fatec.dtos;


import java.time.Instant;

public record ErrorResponseDto(
    String path,
    String message,
    int status,
    String statusMessage,
    Instant timestamp,
    Object details
) {

}
