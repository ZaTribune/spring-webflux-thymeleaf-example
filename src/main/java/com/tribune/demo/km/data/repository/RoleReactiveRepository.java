package com.tribune.demo.km.data.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.tribune.demo.km.data.entity.Role;


public interface RoleReactiveRepository extends ReactiveMongoRepository<Role,String> {
}
