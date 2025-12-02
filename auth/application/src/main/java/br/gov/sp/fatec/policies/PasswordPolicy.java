package br.gov.sp.fatec.policies;

import br.gov.sp.fatec.specifications.password.PasswordSpec;

import java.util.List;

@FunctionalInterface
public interface PasswordPolicy {
    List<PasswordSpec> getViolations(String password);
}
