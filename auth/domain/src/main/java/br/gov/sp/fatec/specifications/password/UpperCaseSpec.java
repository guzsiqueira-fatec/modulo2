package br.gov.sp.fatec.specifications.password;

import br.gov.sp.fatec.SpecFailData;

import java.util.List;

public class UpperCaseSpec implements PasswordSpec {
    @Override
    public SpecFailData test(String password) {
        if (password == null) {
            return new SpecFailData(
                    List.of(this)
            );
        }
        if(password.codePoints().noneMatch(Character::isUpperCase)) {
            return new SpecFailData(
                    List.of(this)
            );
        } else {
            return null;
        }
    }

    @Override
    public String getErrorMessage() {
        return "Password must contain at least one uppercase letter.";
    }
}
