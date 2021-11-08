package com.icommerce.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductFilter {
    private String keyword;
    private List<Long> categories;
    private List<Long> brands;
    private List<Long> colours;
    private Double priceFrom;
    private Double priceTo;

}
