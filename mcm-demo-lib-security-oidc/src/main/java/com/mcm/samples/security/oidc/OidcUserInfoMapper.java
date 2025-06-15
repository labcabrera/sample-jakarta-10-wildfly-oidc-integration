package com.mcm.samples.security.oidc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class OidcUserInfoMapper {

    @Inject
    @Named("userMapper")
    private Function<String, Entry<String, List<String>>> userMapper;

    public Entry<String, List<String>> map(String userId) {
        if (userMapper == null) {
            log.warn("Undefined bean userMapper");
            return Map.entry(userId, new ArrayList<>());
        }
        try {
            log.info("Using mapper {}", userMapper.getClass().getName());
            Entry<String, List<String>> result = userMapper.apply(userId);
            log.info("Mapped userId '{}' to {}:{}", userId, result.getKey(), result.getValue());
            return result;
        }
        catch (Exception ex) {
            log.error("Error mapping user info", ex);
            return Map.entry(userId, new ArrayList<>());
        }
    }

}
