package mfreitas.msuser.resources.userMappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.models.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO userToUserDTO(User user);

    UserLoginDTO userOAuthToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    List<UserDTO> ListUserToUserDTO(List<User> list);
}
