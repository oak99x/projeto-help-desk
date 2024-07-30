package mfreitas.msuser.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import mfreitas.msuser.dtos.UserLoginDTO;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.userMappers.UserMapper;

@Service
public class LoginService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    // public UserLoginDTO getUserByEmail(String email) {
    //     User user = userRepository.findByEmailIgnoreCase(email).get();
    //     if (user == null) {
    //         throw new ObjectNotFoundException(messageSource.getMessage("email.not.found", null, null) + email);
    //     }
    //     return UserMapper.INSTANCE.userOAuthToUserDTO(user);
    // }

    public UserLoginDTO getUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("email.not.found", null, null) + email));

        return UserMapper.INSTANCE.userOAuthToUserDTO(user);
    }
}
