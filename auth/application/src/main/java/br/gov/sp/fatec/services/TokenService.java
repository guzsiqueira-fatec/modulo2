package br.gov.sp.fatec.services;

import br.gov.sp.fatec.entity.User;
import br.gov.sp.fatec.model.Token;

public interface TokenService {
    Token create(User user);
}
