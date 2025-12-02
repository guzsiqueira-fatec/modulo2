package br.gov.sp.fatec.services;

public interface PasswordHasherService {
    String hash(String password);
    boolean verify(String password, String hashedPassword);
}
