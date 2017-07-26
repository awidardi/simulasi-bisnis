package com.kkn.masaran.repository;


import com.kkn.masaran.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ARDI on 11/2/2016.
 */

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBusinessmanUsernameAndStatus(String username, int status);

}
