package com.omate.liuqu.repository;

import com.omate.liuqu.model.Activity;
import com.omate.liuqu.model.Partner;
import com.omate.liuqu.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findById(Long id);

    User findByUserTel(String userTel);

    @Query("SELECT u.favoriteActivities FROM User u WHERE u.id = :userId")
    Set<Activity> findFavoriteActivitiesByUserId(Long userId);

    @Query("SELECT u.followedPartners FROM User u WHERE u.id = :userId")
    Set<Partner> findFollowedPartnersByUserId(Long userId);
}