package mfreitas.msuser.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mfreitas.msuser.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor {
    // Admin
    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByEmailContainingIgnoreCase(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    // Ajusta a query para considerar que as roles estão salvas em maiúsculo no banco de dados
    @Query("SELECT u FROM User u JOIN u.roles r WHERE UPPER(r.roleName) = UPPER(:roleName)")
    List<User> findByRoles_RoleNameIgnoreCase(@Param("roleName") String roleName);
}
