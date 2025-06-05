package com.mcm.samples.api.users.infrastructure.jpa;

import java.util.Arrays;
import java.util.Optional;

import com.mcm.demo.users.model.UserInfo;
import com.mcm.samples.api.users.application.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JpaUserRepository implements UserRepository {

    //TODO demo code
    @Override
    public Optional<UserInfo> findById(String id) {
        UserInfo userInfo = new UserInfo();
        userInfo.setCode("zjd0001");
        userInfo.setRoles(Arrays.asList("foo", "bar"));
        return Optional.of(userInfo);
    }

}
