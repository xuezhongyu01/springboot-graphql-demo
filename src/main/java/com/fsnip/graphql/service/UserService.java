package com.fsnip.graphql.service;

import com.fsnip.graphql.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUserAndBook();
}
