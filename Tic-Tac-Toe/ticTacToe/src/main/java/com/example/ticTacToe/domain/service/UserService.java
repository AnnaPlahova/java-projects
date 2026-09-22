package com.example.ticTacToe.domain.service;

import com.example.ticTacToe.datasource.mapper.UserBaseMapper;
import com.example.ticTacToe.datasource.model.UserDto;
import com.example.ticTacToe.datasource.repository.UserRepository;
import com.example.ticTacToe.domain.exception.UserNotFoundException;
import com.example.ticTacToe.domain.model.Role;
import com.example.ticTacToe.domain.model.SignUpRequest;
import com.example.ticTacToe.domain.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

// Логика регистрации и входа - ядро аутентификации
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserBaseMapper userBaseMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserBaseMapper userBaseMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userBaseMapper = userBaseMapper;
    }

    /**
     * Регистрация пользователя. Возвращает true, если пользователь создан.
     */
    public boolean register(SignUpRequest request) {
        if (userRepository.findByLogin(request.getLogin()).isPresent()) {
            return false; // Пользователь уже существует
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserDto newUser = new UserDto(UUID.randomUUID(), request.getLogin(), hashedPassword,
                Collections.singleton(Role.USER));
        userRepository.save(newUser);
        return true;
    }

    /**
     * Авторизация: проверяет логин/пароль, возвращает UUID пользователя или null
     */
    public UUID authenticate(String login, String password) {
        Optional<UserDto> userOpt = userRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            UserDto user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return user.getId();
            }
        }
        return null; // Не удалось авторизоваться
    }

    /**
     * Получение UserDto по id
     */
    public Users getUserById(UUID id) {
        Optional<UserDto> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userBaseMapper.toDomain(userOptional.get());
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
