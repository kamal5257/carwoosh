package com.carwoosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.carwoosh.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * from users where username =:userName", nativeQuery = true)
	User findByUserName(@Param("userName") String userName);

	User findByEmail(String email);
}
