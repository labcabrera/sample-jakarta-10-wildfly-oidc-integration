package com.mcm.samples.rest.client.infrastructure.jpa.repository;

import java.util.List;

import com.mcm.samples.rest.client.domain.entity.Page;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ApplicationScoped
public class RSQLSearchService {

    @PersistenceContext
    private EntityManager em;

    public <E> Page<E> find(String searchExpression, int page, int size, Class<E> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<E> query = cb.createQuery(entityClass);
        Root<E> root = query.from(entityClass);

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<E> rootCount = countQuery.from(entityClass);
        countQuery.select(cb.count(rootCount));

        if (searchExpression != null && !searchExpression.isBlank()) {
            Node rootNode = new RSQLParser().parse(searchExpression);

            RSQLCriteriaVisitor<E> visitor = new RSQLCriteriaVisitor<>(cb, root);
            Predicate predicate = rootNode.accept(visitor, root);
            query.where(predicate);

            RSQLCriteriaVisitor<E> countVisitor = new RSQLCriteriaVisitor<>(cb, rootCount);
            Predicate predicateCount = rootNode.accept(countVisitor, rootCount);
            countQuery.where(predicateCount);
        }
        List<E> customerEntities = em.createQuery(query.select(root))
            .setFirstResult(page * size)
            .setMaxResults(size)
            .getResultList();
        long total = em.createQuery(countQuery).getSingleResult();
        return Page.<E>builder()
            .content(customerEntities)
            .page(page)
            .size(size)
            .totalElements(total)
            .totalPages((int) Math.ceil((double) total / size))
            .build();
    }
}
