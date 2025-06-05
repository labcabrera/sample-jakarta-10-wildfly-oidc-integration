package com.mcm.samples.api.users.application.service;

import java.util.Arrays;

import com.mcm.demo.users.model.UserInfo;

import jakarta.enterprise.context.RequestScoped;
import lombok.extern.slf4j.Slf4j;

@RequestScoped
@Slf4j
public class UserSearchService {

    public UserInfo findUserById(String userId) {
        //TODO
        log.info("Searching for user with ID: {}", userId);
        UserInfo user = new UserInfo();
        user.setCode(userId);
        user.setRoles(Arrays.asList(null, "ROLE_FOO", "ROLE_BAR"));
        return user;
    }
}
