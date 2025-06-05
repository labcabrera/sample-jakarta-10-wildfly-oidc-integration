package com.mcm.samples.customer.api.infrastructure.jpa.repository;

import java.util.Optional;

import com.mcm.samples.customer.api.application.repository.CustomerRepository;
import com.mcm.samples.customer.api.domain.entity.Customer;
import com.mcm.samples.customer.api.domain.entity.Page;
import com.mcm.samples.customer.api.infrastructure.jpa.entities.CustomerEntity;
import com.mcm.samples.customer.api.infrastructure.jpa.mapper.CustomerMapper;

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
    public Page<Customer> find(String searchExpression, int page, int size) {
        Page<CustomerEntity> pageEntity = rsqlSearchService.find(searchExpression, page, size, CustomerEntity.class);
        return Page.<Customer>builder()
            .content(pageEntity.getContent().stream()
                .map(customerMapper::toDomain)
                .toList())
            .page(pageEntity.getPage())
            .size(pageEntity.getSize())
            .totalElements(pageEntity.getTotalElements())
            .totalPages(pageEntity.getTotalPages())
            .build();
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
