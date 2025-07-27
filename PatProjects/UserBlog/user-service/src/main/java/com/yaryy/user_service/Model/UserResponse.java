package com.yaryy.user_service.Model;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private String userName;
    private List<Integer> postIds;
}
