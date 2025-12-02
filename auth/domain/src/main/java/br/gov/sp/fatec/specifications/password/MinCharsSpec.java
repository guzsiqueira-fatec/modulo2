package br.gov.sp.fatec.specifications.password;

import br.gov.sp.fatec.SpecFailData;

import java.util.List;

public class MinCharsSpec implements PasswordSpec {
    private final int minChars;

    public MinCharsSpec(int minChars) {
        this.minChars = minChars;
    }

    @Override
    public SpecFailData<PasswordSpec> test(String password) {
        if(password != null && password.length() >= minChars) {
            return null;
        } else {
            return new SpecFailData<PasswordSpec>(
                    List.of(this)
            );
        }
    }

    @Override
    public String getErrorMessage() {
        return "Password must be at least " + minChars + " characters long.";
    }
}
