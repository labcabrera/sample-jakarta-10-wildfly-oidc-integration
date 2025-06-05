package com.mcm.samples.api.users.infrastructure.api;

import com.mcm.samples.api.users.application.repository.UserRepository;
import com.mcm.samples.api.users.domain.exception.UserNotFoundException;
import com.mcm.samples.api.users.generated.users.api.UsersApi;
import com.mcm.samples.api.users.generated.users.model.UserInfo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class UserApiImpl implements UsersApi {

    @Inject
    private UserRepository userRepository;

    @Override
    public UserInfo findCustomerById(String idUser) {
        log.info("Customer find by id << {}", idUser);
        UserInfo userInfo = userRepository.findById(idUser).orElseThrow(UserNotFoundException::new);
        return userInfo;
    }

}
