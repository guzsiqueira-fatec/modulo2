package br.gov.sp.fatec;

import br.gov.sp.fatec.specifications.Specification;

import java.util.ArrayList;
import java.util.List;

public record SpecFailData<S extends Specification<?, ?>>(
        List<S> errors // especificacao que falhou
) {
    public SpecFailData<S> combine(SpecFailData<S> another) {
        if (another == null) {
            return this;
        }
        SpecFailData<S> a = this;
        List<S> combinedErrors = new ArrayList<>(a.errors);
        combinedErrors.addAll(another.errors);
        return new SpecFailData<>(combinedErrors);
    }
}
