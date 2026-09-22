package com.example.ticTacToe.datasource.mapper;

import com.example.ticTacToe.datasource.model.UserDto;
import com.example.ticTacToe.domain.model.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserBaseMapper {

    // Преобразование из DTO в доменную модель
    public Users toDomain(UserDto userDto) {
        if (userDto == null) return null;
        return new Users(userDto.getId(), userDto.getLogin(), userDto.getCreatedAt(),
                new ArrayList<>(userDto.getRoles()));
    }

//    // Преобразование из доменной модели в DTO
//    public UserDto toDto(Users user) {
//        if (user == null) return null;
//        return new UserDto(user.getId(), user.getLogin(), user.getCreatedAt(), user.getRoles()); // passwordHash не передаем
//    }
}
