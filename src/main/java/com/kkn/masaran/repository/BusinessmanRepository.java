package com.kkn.masaran.repository;

import com.kkn.masaran.model.Businessman;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ARDI on 6/22/2017.
 */

@Repository
public interface BusinessmanRepository extends JpaRepository<Businessman, Long> {
}
