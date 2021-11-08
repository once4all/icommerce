package com.icommerce.repository;

import com.icommerce.dom.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    public List<Category> findAllByActiveTrue();
}
