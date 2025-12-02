package br.gov.sp.fatec.specifications.password;

import br.gov.sp.fatec.SpecFailData;

import java.util.List;

public class NumericCharSpec implements PasswordSpec {
    @Override
    public SpecFailData<PasswordSpec> test(String password) {
        if (password == null) {
            return new SpecFailData<PasswordSpec>(
                    List.of(this)
            );
        }
        if(password.codePoints().noneMatch(Character::isDigit)) {
            return new SpecFailData<PasswordSpec>(
                    List.of(this)
            );
        }
        return null;
    }

    @Override
    public String getErrorMessage() {
        return "Password must contain at least one numeric character.";
    }
}
