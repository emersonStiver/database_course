package com.emerson.unisalle_app.repositories;

import com.emerson.unisalle_app.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, Long> {
    List<Token> findAllByEmail(String email);

    Optional<Token> findByAccessToken(String token);
}
