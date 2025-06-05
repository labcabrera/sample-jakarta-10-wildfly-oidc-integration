package com.mcm.samples.api.users.infrastructure.api;

import com.mcm.demo.users.api.ApiApi;
import com.mcm.demo.users.model.UserInfo;
import com.mcm.samples.api.users.application.repository.UserRepository;
import com.mcm.samples.api.users.domain.exception.UserNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserApiImpl implements ApiApi {

    @Inject
    private UserRepository userRepository;

    @Override
    public UserInfo findCustomerById(String id) {
        UserInfo userInfo = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userInfo;
    }

}
