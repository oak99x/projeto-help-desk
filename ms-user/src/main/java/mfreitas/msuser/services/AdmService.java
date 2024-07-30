package mfreitas.msuser.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mfreitas.msuser.dtos.UserDTO;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.exceptions.ObjectAlreadyExistsException;
import mfreitas.msuser.exceptions.ObjectNotFoundException;
import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;
import mfreitas.msuser.repositories.RoleRepository;
import mfreitas.msuser.repositories.UserRepository;
import mfreitas.msuser.resources.enums.RoleEnum;
import mfreitas.msuser.resources.userMappers.UserMapper;

@Service
public class AdmService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SearchUsers searchUsers;

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.INSTANCE.ListUserToUserDTO(users);
    }

    public List<UserDTO> getUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return UserMapper.INSTANCE.ListUserToUserDTO(users);
    }

    public List<UserDTO> getUsersByEmail(String email) {
        List<User> users = userRepository.findByEmailContainingIgnoreCase(email);
        return UserMapper.INSTANCE.ListUserToUserDTO(users);
    }

    public List<UserDTO> getUsersByRole(String roleName) {
        // Busca usuários pelo nome do role. Isso requer um novo método no
        // userRepository.
        List<User> users = userRepository.findByRoles_RoleNameIgnoreCase(roleName);
        // Converte os usuários encontrados para DTOs.
        return UserMapper.INSTANCE.ListUserToUserDTO(users);
    }

    // // Usuario novo
    public UserDTO addUser(UserDTO userDto) {

        if (userRepository.findByEmailIgnoreCase(userDto.getEmail()).isPresent()) {
            throw new ObjectAlreadyExistsException(
                    messageSource.getMessage("email.already.exists", null, null) + userDto.getEmail());
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());

        // Define o nome do usuário usando o operador ternário para simplificar a lógica
        String name = userDto.getName() == null
                ? userDto.getEmail().substring(0, userDto.getEmail().indexOf('@'))
                : userDto.getName();
        newUser.setName(name);

        // Define a senha inicial como a parte local do e-mail
        String initialPassword = passwordEncoder
                .encode(userDto.getEmail().substring(0, userDto.getEmail().indexOf('@')));
        newUser.setPassword(initialPassword);

        // Define o papel padrão como "USER"
        Role userRole = roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_USER.toString())
                .orElseThrow(() -> new ObjectNotFoundException(messageSource.getMessage("role.not.found", null, null)));

        newUser.addRole(userRole);

        // Salva o novo usuário no repositório
        userRepository.save(newUser);

        // Retorna a representação DTO do novo usuário
        return UserMapper.INSTANCE.userToUserDTO(newUser);
    }

    public UserDTO promoteToAdmin(String email) throws Exception {
        User user = findUserByEmail(email);
        checkIfUserIsAlreadyAdmin(user);

        Role adminRole = findAdminRole();
        user.addRole(adminRole);
        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    private User findUserByEmail(String email) throws ObjectNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("user.not.found", null, null) + email));
    }

    private void checkIfUserIsAlreadyAdmin(User user) throws ObjectAlreadyExistsException {
        if (user.hasRole(RoleEnum.ROLE_ADMIN.toString())) {
            throw new ObjectAlreadyExistsException(
                    messageSource.getMessage("user.is.already.an.administrator", null, null) + user.getEmail());
        }
    }

    private Role findAdminRole() throws ObjectNotFoundException {
        return roleRepository.findByRoleNameIgnoreCase(RoleEnum.ROLE_ADMIN.toString())
                .orElseThrow(() -> new ObjectNotFoundException(
                        messageSource.getMessage("role.not.found", null, null)));
    }

    public UserDTO deleteRoleAdmin(String email) {
        User user = findUserByEmail(email);

        // Verifica se o usuário tem o role "Admin"
        if (user.hasRole("ROLE_ADMIN")) {
            // Encontrar e remover o role "Admin" do conjunto de roles do usuário
            user.setRoles(user.getRoles().stream()
                    .filter(role -> !role.getRoleName().equals("ROLE_ADMIN"))
                    .collect(Collectors.toSet()));
        } else {
            throw new ObjectNotFoundException(
                    messageSource.getMessage("User does not have the role Admin:", null, null) + email);
        }

        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    
    @SuppressWarnings("unchecked")
    public List<UserDTO> filterUsers(UserFilter userFilter) {
        Specification<User> spec = searchUsers.filter(userFilter);
        List<User> users = userRepository.findAll(spec);
        return UserMapper.INSTANCE.ListUserToUserDTO(users);
    }

}
