package com.kkn.masaran.repository;

/**
 * Created by ARDI on 10/6/2016.
 */

import com.kkn.masaran.model.Product;
import com.kkn.masaran.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    List<RawMaterial> findByName(String name);
    List<RawMaterial> findAllByOrderByName();
    List<RawMaterial> findByIsDiscountedOrderByName(boolean isDiscounted);
    List<RawMaterial> findByStockExist(boolean stockExist);
}
