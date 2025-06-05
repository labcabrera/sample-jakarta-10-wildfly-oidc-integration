package com.mcm.samples.security.jwt;

import java.util.List;

import lombok.Data;

@Data
public class JwtUserInfo {

    private String id;
    private String code;
    private List<String> roles;

}
