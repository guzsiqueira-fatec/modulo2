package br.gov.sp.fatec.specifications;

import java.util.function.BinaryOperator;

// T -> tipo do input a ser checado (eg. string, numero, objeto xpto)
// R -> tipo do resultado da checagem (eg. boolean (e), lista de erros (list concat), objeto resultado (regra personalizada))
public interface Specification<T, R> {
    R test(T t);

    default Specification<T, R> and(Specification<T, R> other, BinaryOperator<R> combine) {
        return t -> combine.apply(this.test(t), other.test(t));
    }
}