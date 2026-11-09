package com.example.J_Tictactoe.datasource.mapper;

import com.example.J_Tictactoe.datasource.model.UserEntity;
import com.example.J_Tictactoe.domain.model.User;

public class UserDatasourceMapper {

    public static UserEntity toEntity(User user) {
        UserEntity userEntity = new UserEntity();

        userEntity.setId(user.getId());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(user.getPassword());
        userEntity.setRoles(user.getRoles());

        return userEntity;
    }

    public static User toDomain(UserEntity entity) {
        return new User(entity.getId(), entity.getLogin(), entity.getPassword(), entity.getRoles());
    }
}
