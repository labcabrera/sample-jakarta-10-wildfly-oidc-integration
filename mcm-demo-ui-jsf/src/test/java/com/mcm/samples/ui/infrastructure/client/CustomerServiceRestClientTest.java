package com.mcm.samples.ui.infrastructure.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mcm.demo.api.client.CustomersApi;
import com.mcm.demo.api.client.model.CreateCustomerCmd;
import com.mcm.demo.api.client.model.Customer;

class CustomerServiceRestClientTest {

    private CustomerServiceRestClient service;
    private CustomersApi customersApiMock;

    @BeforeEach
    void setUp() throws Exception {
        service = new CustomerServiceRestClient();
        service.init();
        customersApiMock = mock(CustomersApi.class);

        var field = CustomerServiceRestClient.class.getDeclaredField("customersApi");
        field.setAccessible(true);
        field.set(service, customersApiMock);
    }

    @Test
    void testCreateSearchAndDelete() {
        String rnd = String.valueOf(new Random().nextInt(10000));
        CreateCustomerCmd cmd = new CreateCustomerCmd();
        cmd.setFirstName("john-" + rnd);
        cmd.setLastName("doe-" + rnd);
        cmd.setEmail(String.format("john-doe-%s@test.com", rnd));
        Customer customerCreated = service.create(cmd);

        assertNotNull(customerCreated);
        assertNotNull(customerCreated.getId());

        Customer customerSearchById = service.findById(customerCreated.getId());
        assertNotNull(customerSearchById);

    }

    // @Test
    // void testGetCustomers() throws ApiException {
    //     CustomerPage mockPage = new CustomerPage();
    //     when(customersApiMock.apiCustomersGet(null, null, null)).thenReturn(mockPage);

    //     CustomerPage result = service.getCustomers();
    //     assertNotNull(result);
    //     verify(customersApiMock).apiCustomersGet(null, null, null);
    // }

    // @Test
    // void testCreateCustomer() throws ApiException {
    //     CreateCustomerCmd cmd = new CreateCustomerCmd();
    //     Customer mockCustomer = new Customer();
    //     when(customersApiMock.apiCustomersPost(cmd)).thenReturn(mockCustomer);

    //     Customer result = service.createCustomer(cmd);
    //     assertNotNull(result);
    //     verify(customersApiMock).apiCustomersPost(cmd);
    // }

    // @Test
    // void testGetCustomersThrowsRuntimeException() throws ApiException {
    //     when(customersApiMock.apiCustomersGet(null, null, null)).thenThrow(new ApiException("error"));
    //     assertThrows(RuntimeException.class, () -> service.getCustomers());
    // }

    // @Test
    // void testCreateCustomerThrowsRuntimeException() throws ApiException {
    //     CreateCustomerCmd cmd = new CreateCustomerCmd();
    //     when(customersApiMock.apiCustomersPost(cmd)).thenThrow(new ApiException("error"));
    //     assertThrows(RuntimeException.class, () -> service.createCustomer(cmd));
    // }
}