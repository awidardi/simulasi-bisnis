package com.kkn.masaran.repository;

import com.kkn.masaran.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dhika on 15/10/2016.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
