package com.domo.coupon.repository;

import com.domo.coupon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByidAndPassword(Long id, String password);
}
