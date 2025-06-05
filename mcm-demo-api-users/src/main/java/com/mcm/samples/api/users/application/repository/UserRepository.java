package com.mcm.samples.api.users.application.repository;

import java.util.Optional;

import com.mcm.samples.generated.users.model.UserInfo;

public interface UserRepository {

    Optional<UserInfo> findById(String id);

}
