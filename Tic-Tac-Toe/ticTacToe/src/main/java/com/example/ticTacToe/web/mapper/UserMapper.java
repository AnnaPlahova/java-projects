package com.example.ticTacToe.web.mapper;

import com.example.ticTacToe.domain.model.Users;
import com.example.ticTacToe.web.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(Users users) {
        if (users == null) {
            return null;
        }
        return new UserResponse(
                users.getId(),
                users.getLogin(),
                users.getCreatedAt(),
                users.getRoles()
        );
    }
}