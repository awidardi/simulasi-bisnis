package com.kkn.masaran.repository;


import com.kkn.masaran.model.Order;
import com.kkn.masaran.model.OrderRaw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ARDI on 11/2/2016.
 */

@Repository
public interface OrderRawRepository extends JpaRepository<OrderRaw, Long> {

    List<OrderRaw> findByJuraganUsernameAndStatus(String username, int status);
}
