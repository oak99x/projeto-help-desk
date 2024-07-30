package mfreitas.msuser.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import mfreitas.msuser.dtos.UpdateUserDTO;
import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.exceptions.ObjectAlreadyExistsException;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.userMappers.UserMapper;

@Service
public class UserService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("user.not.found", null, null) + userId));
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    public UserDTO getUserByEmail(String email) throws ObjectNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("user.not.found", null, null) + email));

        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    public UserDTO updateUser(UUID userId, UpdateUserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("user.not.found", null, null) + userId));

        if (userDTO.getEmail() != null) {

            if (userRepository.findByEmailIgnoreCase(userDTO.getEmail()) != null) {
                throw new ObjectAlreadyExistsException(
                        messageSource.getMessage("email.already.exists", null, null) + userDTO.getEmail());
            }

            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getName() != null) {
            user.setName(userDTO.getName());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDTO(user);
    }
}
