package com.DEMOJWT.demo.repositories;

import com.DEMOJWT.demo.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    public User findByUserAndPwd(String user, String pwd);
}
