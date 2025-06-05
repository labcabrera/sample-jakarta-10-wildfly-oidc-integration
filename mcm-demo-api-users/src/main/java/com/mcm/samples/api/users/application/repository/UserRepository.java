package com.mcm.samples.api.users.application.repository;

import java.util.Optional;

import com.mcm.samples.api.users.generated.users.model.UserInfo;

public interface UserRepository {

    Optional<UserInfo> findById(String id);

}
