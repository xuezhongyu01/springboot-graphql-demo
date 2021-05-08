package com.fsnip.graphql.service.impl;

import com.fsnip.graphql.model.User;
import com.fsnip.graphql.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> findAllUserAndBook() {
        List<User> users = new ArrayList<User>();

        for (int i = 0; i < 100; i++) {
            User user = new User(i, "name" + i, 10 + i, "man");
            users.add(user);
        }
        return users;
    }
}
