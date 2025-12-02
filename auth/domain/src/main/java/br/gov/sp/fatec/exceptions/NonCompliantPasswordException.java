package br.gov.sp.fatec.exceptions;

public class NonCompliantPasswordException extends RuntimeException {
    public NonCompliantPasswordException(String message) {
        super(message);
    }
}
