package com.mcm.samples.rest.client.infrastructure.jpa.repository;

import java.util.Optional;

import com.mcm.demo.api.model.Customer;
import com.mcm.demo.api.model.CustomerPage;
import com.mcm.samples.rest.client.application.repository.CustomerRepository;
import com.mcm.samples.rest.client.domain.entity.Page;
import com.mcm.samples.rest.client.infrastructure.jpa.entities.CustomerEntity;
import com.mcm.samples.rest.client.infrastructure.jpa.mapper.CustomerMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class CustomerJpaRepository implements CustomerRepository {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private CustomerMapper customerMapper;

    @Inject
    private RSQLSearchService rsqlSearchService;

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(em.find(CustomerEntity.class, id)).map(customerMapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        CustomerEntity customerEntity = em.createQuery(
            "SELECT c FROM CustomerEntity c WHERE c.contactInfo.email = :email", CustomerEntity.class)
            .setParameter("email", email)
            .getResultStream()
            .findFirst()
            .orElse(null);
        return Optional.ofNullable(customerEntity).map(customerMapper::toDomain);
    }

    @Override
    public CustomerPage find(String searchExpression, int page, int size) {
        Page<CustomerEntity> pageEntity = rsqlSearchService.find(searchExpression, page, size, CustomerEntity.class);

        CustomerPage result = new CustomerPage();
        result.setContent(pageEntity.getContent().stream()
            .map(customerMapper::toDomain)
            .toList());
        result.setPage(page);
        result.setSize(size);
        result.setTotalElements(pageEntity.getTotalElements());
        result.setTotalPages(pageEntity.getTotalPages());
        return result;
    }

    public Customer save(Customer customer) {
        CustomerEntity customerEntity = customerMapper.toEntity(customer);
        em.persist(customerEntity);
        log.debug("Customer entity saved: {}", customerEntity);
        return customerMapper.toDomain(customerEntity);
    }

    public Customer update(Customer customer) {
        CustomerEntity customerEntity = customerMapper.toEntity(customer);
        CustomerEntity updated = em.merge(customerEntity);
        return customerMapper.toDomain(updated);
    }

    public void delete(String id) {
        CustomerEntity customer = em.find(CustomerEntity.class, id);
        if (customer != null) {
            em.remove(customer);
        }
    }

}
