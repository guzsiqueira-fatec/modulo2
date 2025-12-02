package br.gov.sp.fatec.services;

import br.gov.sp.fatec.entity.User;
import br.gov.sp.fatec.model.Token;
import br.gov.sp.fatec.model.TokenMetadata;

public interface TokenService {
    Token create(User user);
}
