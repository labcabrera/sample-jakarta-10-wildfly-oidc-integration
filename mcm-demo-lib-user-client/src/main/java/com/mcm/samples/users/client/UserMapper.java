package com.mcm.samples.users.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.mcm.samples.users.client.generated.users.api.UsersApi;
import com.mcm.samples.users.client.generated.users.invoker.ApiClient;
import com.mcm.samples.users.client.generated.users.model.UserInfo;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Named("userMapper")
@Slf4j
public class UserMapper implements Function<String, Entry<String, List<String>>> {

    private UsersApi usersApi;

    @Inject
    private UserClientConfig userClientConfig;

    @PostConstruct
    public void init() {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri(userClientConfig.getUserApiUrl());
        usersApi = new UsersApi(apiClient);
    }

    @Override
    public Entry<String, List<String>> apply(String userId) {
        try {
            UserInfo userInfo = usersApi.findCustomerById(userId);
            return Map.entry(userInfo.getCode(), userInfo.getRoles());
        }
        catch (Exception ex) {
            log.warn("Error fetching user info for userId: {} using {}", userId, userClientConfig.getUserApiUrl(), ex);
            return Map.entry(userId, new ArrayList<>());
        }
    }

}
