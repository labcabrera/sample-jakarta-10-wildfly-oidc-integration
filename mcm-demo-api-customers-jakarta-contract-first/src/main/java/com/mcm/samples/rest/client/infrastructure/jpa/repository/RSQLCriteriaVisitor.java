package com.mcm.samples.rest.client.infrastructure.jpa.repository;

import java.util.List;
import java.util.function.BinaryOperator;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RSQLCriteriaVisitor<T> implements RSQLVisitor<Predicate, Root<T>> {

    private final CriteriaBuilder cb;
    private final Root<T> root;

    public RSQLCriteriaVisitor(CriteriaBuilder cb, Root<T> root) {
        this.cb = cb;
        this.root = root;
    }

    @Override
    public Predicate visit(AndNode node, Root<T> param) {
        return combinePredicates(node.getChildren(), cb::and);
    }

    @Override
    public Predicate visit(OrNode node, Root<T> param) {
        return combinePredicates(node.getChildren(), cb::or);
    }

    @Override
    public Predicate visit(ComparisonNode node, Root<T> param) {
        String field = node.getSelector();
        String value = node.getArguments().get(0);
        Path<String> path = root.get(field);

        switch (node.getOperator().getSymbol()) {
        case "==":
            return cb.equal(path, value);
        case "!=":
            return cb.notEqual(path, value);
        case "=gt=":
            return cb.greaterThan(path, value);
        case "=lt=":
            return cb.lessThan(path, value);
        case "=ge=":
            return cb.greaterThanOrEqualTo(path, value);
        case "=le=":
            return cb.lessThanOrEqualTo(path, value);
        default:
            throw new UnsupportedOperationException("Operador no soportado: " + node.getOperator());
        }
    }

    private Predicate combinePredicates(List<Node> nodes, BinaryOperator<Predicate> combiner) {
        return nodes.stream()
            .map(n -> n.accept(this, root))
            .reduce(combiner)
            .orElse(cb.conjunction());
    }
}