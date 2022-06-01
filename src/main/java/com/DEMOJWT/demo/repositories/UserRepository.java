package com.DEMOJWT.demo.repositories;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
