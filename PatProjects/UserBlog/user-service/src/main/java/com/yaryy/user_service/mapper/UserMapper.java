package com.yaryy.user_service.mapper;

import com.yaryy.user_service.Model.User;
import com.yaryy.user_service.Model.UserRequest;
import com.yaryy.user_service.Model.UserResponse;

public class UserMapper {
    public static UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setPostIds(user.getPostIds());
        dto.setUserName(user.getUserName());
        return dto;
    }

    public static User toEntity(UserRequest dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        return user;
    }
}
