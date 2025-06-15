package com.mcm.samples.users.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcm.samples.users.client.generated.users.api.UsersApi;
import com.mcm.samples.users.client.generated.users.invoker.ApiClient;
import com.mcm.samples.users.client.generated.users.invoker.ApiException;
import com.mcm.samples.users.client.generated.users.model.UserInfo;

class UserMapperTest {

    private UsersApi usersApi;

    @BeforeEach
    void setUp() throws NoSuchFieldException, SecurityException {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri("http://localhost:8082/api-users/api");
        usersApi = new UsersApi(apiClient);
    }

    @Test
    void test() throws ApiException {
        UserInfo userInfo = usersApi.findCustomerById("customer-manager");
        assertNotNull(userInfo);
    }

}