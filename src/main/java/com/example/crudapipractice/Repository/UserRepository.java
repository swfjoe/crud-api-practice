package com.example.crudapipractice.Repository;

import com.example.crudapipractice.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);
}
