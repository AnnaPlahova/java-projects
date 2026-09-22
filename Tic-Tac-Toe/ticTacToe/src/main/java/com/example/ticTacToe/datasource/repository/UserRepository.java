package com.example.ticTacToe.datasource.repository;

import com.example.ticTacToe.datasource.model.UserDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserDto, UUID> {
    Optional<UserDto> findByLogin(String login);
}
