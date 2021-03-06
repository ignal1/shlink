package com.backend.repository;

import com.backend.entity.User;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
  Optional<User> findByEmail(String email);
}