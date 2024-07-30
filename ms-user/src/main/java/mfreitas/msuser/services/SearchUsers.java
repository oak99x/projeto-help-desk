package mfreitas.msuser.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;
import mfreitas.msuser.dtos.UserFilter;
import mfreitas.msuser.models.Role;
import mfreitas.msuser.models.User;

@Service
public class SearchUsers {

    public Specification<User> filter(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // // Se o filtro 'all' estiver marcado como true, ignora os outros filtros e
            // traz todos os usuários
            // if (filter.isAll()) {
            // return criteriaBuilder.conjunction(); // Retorna sempre verdadeiro
            // }

            // Filtro por e-mail com Containing e IgnoreCase
            if (StringUtils.hasText(filter.getUserEmail())) {
                String emailPattern = "%" + filter.getUserEmail().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), emailPattern));
            }

            // Filtro por área com Containing e IgnoreCase, assumindo que existe um campo
            // 'area'
            if (StringUtils.hasText(filter.getArea())) {
                // String areaPattern = "%" + filter.getArea().toLowerCase() + "%";
                // predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("area")),
                // areaPattern));
            }

            if (StringUtils.hasText(filter.getRole())) {
                // Usando join para filtrar diretamente com a relação entre User e Role
                SetJoin<User, Role> roleJoin = root.joinSet("roles", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(roleJoin.get("roleName")),
                        ("ROLE_" + filter.getRole()).toLowerCase()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
