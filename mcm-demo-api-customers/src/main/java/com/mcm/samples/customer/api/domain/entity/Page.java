package com.mcm.samples.customer.api.domain.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page<E> {

    private List<E> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

}
