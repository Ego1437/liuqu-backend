package com.omate.liuqu.repository;

import com.omate.liuqu.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findById(Long id);

    User findByUserTel(String userTel);

}