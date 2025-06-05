package com.mcm.samples.api.users.infrastructure.jpa;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.mcm.samples.api.users.application.repository.UserRepository;
import com.mcm.samples.api.users.generated.users.model.UserInfo;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class JpaUserRepository implements UserRepository {

    //TODO demo code
    private final Map<String, UserInfo> userMap;

    public JpaUserRepository() {
        userMap = new HashMap<>();

        UserInfo demo01 = new UserInfo();
        demo01.setId("johndoe");
        demo01.setCode("zjd0001");
        demo01.setRoles(Arrays.asList("foo_role", "bar_role"));

        UserInfo demo02 = new UserInfo();
        demo02.setId("customer-manager");
        demo02.setCode("zcm0001");
        demo02.setRoles(Arrays.asList("foo_role", "bar_role", "customer_manager_extended"));

        UserInfo demo03 = new UserInfo();
        demo03.setId("customer-viewer");
        demo03.setCode("zjd0001");
        demo03.setRoles(Arrays.asList("foo_role", "bar_role", "customer_viewer_extended"));

        List<UserInfo> demoUsers = Arrays.asList(demo01, demo02, demo03);
        for (UserInfo user : demoUsers) {
            userMap.put(user.getId(), user);
        }
    }

    @Override
    public Optional<UserInfo> findById(String idCustomer) {
        log.info("User info find by id << {}", idCustomer);
        return Optional.ofNullable(userMap.get(idCustomer));
    }

}
