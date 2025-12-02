package br.gov.sp.fatec.specifications.password;

import br.gov.sp.fatec.SpecFailData;
import br.gov.sp.fatec.specifications.Specification;

// Valida strings, e acumula erros em uma lista de strings
public interface PasswordSpec extends Specification<String, SpecFailData<PasswordSpec>> {
    String getErrorMessage();

    default PasswordSpec and(PasswordSpec other) {
        PasswordSpec self = this;
        return new PasswordSpec() {
            @Override
            public String getErrorMessage() {
                return self.getErrorMessage() + "; " + other.getErrorMessage();
            }

            @Override
            public SpecFailData<PasswordSpec> test(String s) {
                SpecFailData<PasswordSpec> r1 = self.test(s);
                SpecFailData<PasswordSpec> r2 = other.test(s);
                if (r1 == null) return r2;
                if (r2 == null) return r1;
                return r1.combine(r2);
            }
        };
    }
}
