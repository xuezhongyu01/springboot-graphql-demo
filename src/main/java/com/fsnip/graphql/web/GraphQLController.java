package com.fsnip.graphql.web;

import com.fsnip.graphql.model.User;
import com.fsnip.graphql.service.UserService;
import graphql.GraphQL;
import graphql.schema.*;
import static graphql.schema.GraphQLObjectType.newObject;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;

@RestController
public class GraphQLController {


    @Autowired
    private UserService userService;


    @GetMapping("/testGetGraphQL")
    public Object testGetGraphQL(String query) {
        // 查询全部（主子表 这里是一张user表，一张book表）
        List<User> list = userService.findAllUserAndBook();

        // 创建book查询type
        GraphQLObjectType bookType = newObject().name("book")

                .field(newFieldDefinition().name("bid").type(GraphQLInt))

                .field(newFieldDefinition().name("userid").type(GraphQLInt))

                .field(newFieldDefinition().name("bookname").type(GraphQLString))

                .field(newFieldDefinition().name("bookprice").type(GraphQLInt))

                .build();

        // 创建user查询type
        GraphQLObjectType userType = newObject().name("user")

                .field(newFieldDefinition().name("id").type(GraphQLInt))

                .field(newFieldDefinition().name("username").type(GraphQLString))

                .field(newFieldDefinition().name("password").type(GraphQLString))

                .field(newFieldDefinition().name("book") // 这里是子表的type name
                        .type(new GraphQLList(bookType))) // 这里是字表的type

                .build();

        // 获取请求参数中id
        GraphQLFieldDefinition personDefinition = GraphQLFieldDefinition
                .newFieldDefinition()
                .name("user")
                .type(userType)
                .argument(
                        GraphQLArgument.newArgument().name("id")
                                .type(GraphQLInt))
                .dataFetcher(new DataFetcher() {
                    public Object get(
                            DataFetchingEnvironment dataFetchingEnvironment) {
                        int id = dataFetchingEnvironment.getArgument("id");
                        for (User user : list) {
                            if (user.getId() == id) {
                                return user;
                            }
                        }
                        return null;
                    }
                }).build();

        // 创建schema，用于执行查询
        GraphQLSchema schema = GraphQLSchema
                .newSchema()
                .query(newObject().name("userQuery").field(personDefinition)
                        .build()).build();

        // 传入schema，执行查询
        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        // 返回查询结果集
        Object data = graphQL.execute(query).getData();

        return data;
    }
}
