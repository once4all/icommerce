package com.icommerce.repository.impl;


import com.icommerce.dom.ProductStatus;
import com.icommerce.filter.ProductFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Repository
public class ProductRepositoryImpl {

    @PersistenceContext
    EntityManager em;

    public List<Object> findAllWithCondition(ProductStatus status, ProductFilter filter) {
        String queryString = "select distinct a.id  from product a " +
                " left join product_brand b on a.id = b.product_id" +
                " left join product_category c on a.id = c.product_id" +
                " left join product_colour d on a.id = d.product_id" +
                " where a.status = :status ";
        Map<String, Object> params = new HashMap<>();
        params.put("status", status.name());
        if (!isNullOrEmpty(filter.getBrands())) {
            queryString += " and  b.brand_id in :brands";
            params.put("brands", filter.getBrands());
        }
        if (!isNullOrEmpty(filter.getCategories())) {
            queryString += " and  c.category_id in :categories";
            params.put("categories", filter.getCategories());
        }
        if (!isNullOrEmpty(filter.getColours())) {
            queryString += " and  d.colour_id in (:colours)";
            params.put("colours", filter.getColours());
        }
        if (!isNullOrEmpty(filter.getPriceFrom())) {
            queryString += " and  a.price >= :priceFrom";
            params.put("priceFrom", filter.getPriceFrom());
        }
        if (!isNullOrEmpty(filter.getColours())) {
            queryString += " and   a.price <= :priceTo";
            params.put("priceTo", filter.getPriceTo());
        }
        if (!isNullOrEmpty(filter.getKeyword())) {
            queryString += " and  lower(name) like :keyword";
            params.put("keyword", "%" + filter.getKeyword().trim().toLowerCase() + "%");
        }
        Query query = em.createNativeQuery(queryString);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    private boolean isNullOrEmpty(Object value) {
        if (Objects.isNull(value)) {
            return true;
        }
        if (value instanceof Collection) {
            return ((Collection) value).isEmpty();
        } else if (value instanceof String) {
            return "".equals(value.toString().trim());
        }
        return false;
    }
}
