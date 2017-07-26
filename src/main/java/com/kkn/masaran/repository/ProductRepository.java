package com.kkn.masaran.repository;

/**
 * Created by ARDI on 10/6/2016.
 */
import java.util.List;

import com.kkn.masaran.model.Businessman;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kkn.masaran.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByName(String name);
    List<Product> findByIsSoldOrderByName(boolean isSold);
    List<Product> findByIsSold(boolean isSold);
}
