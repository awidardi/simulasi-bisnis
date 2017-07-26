package com.kkn.masaran.repository;

import com.kkn.masaran.model.Order;
import com.kkn.masaran.model.OrderDetail;
import com.kkn.masaran.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by dhika on 26/11/2016.
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    OrderDetail findByProduct(Product product);

}
