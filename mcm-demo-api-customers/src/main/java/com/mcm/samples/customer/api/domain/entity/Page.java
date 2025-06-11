package com.mcm.samples.customer.api.domain.entity;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Page<E> {

    @Schema(description = "Content of the page", implementation = List.class)
    private List<E> content;

    @Schema(description = "Current page number", example = "0")
    private int page;

    @Schema(description = "Size of the page", example = "10")
    private int size;

    @Schema(description = "Total number of elements in the entire dataset", example = "100")
    private long totalElements;

    @Schema(description = "Total number of pages available", example = "12")
    private int totalPages;

}
