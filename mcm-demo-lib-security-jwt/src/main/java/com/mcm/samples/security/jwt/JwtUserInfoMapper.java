package com.mcm.samples.security.jwt;

import java.util.Optional;

public interface JwtUserInfoMapper {

    Optional<JwtUserInfo> map(String idUser);

}
