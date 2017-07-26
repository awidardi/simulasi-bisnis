package com.kkn.masaran.repository;

import com.kkn.masaran.model.Juragan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ARDI on 6/29/2017.
 */
@Repository
public interface JuraganRepository extends JpaRepository<Juragan, Long>{
}
