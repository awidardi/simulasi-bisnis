package com.kkn.masaran.repository;

import com.kkn.masaran.model.OrderDetail;
import com.kkn.masaran.model.OrderRawDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface OrderRawDetailRepository extends JpaRepository<OrderRawDetail, Long> {
}
