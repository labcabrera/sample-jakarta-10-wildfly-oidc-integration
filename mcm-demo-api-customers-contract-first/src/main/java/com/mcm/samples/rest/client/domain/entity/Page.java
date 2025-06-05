package com.mcm.samples.rest.client.domain.entity;

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

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private List<E> content;

}
