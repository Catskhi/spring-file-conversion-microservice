package com.catskhi.user.mapper;

import com.catskhi.user.domain.UserModel;
import com.catskhi.user.dto.UserDto;

public class UserMapper {

    public static UserDto toDto(UserModel user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static UserModel toModel(UserDto dto) {
        if (dto == null) {
            return null;
        }
        UserModel user = new UserModel();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        return user;
    }
}
