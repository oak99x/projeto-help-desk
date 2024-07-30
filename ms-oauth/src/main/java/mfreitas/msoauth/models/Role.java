package mfreitas.msoauth.models;

import java.util.UUID;

import lombok.Data;

@Data
public class Role {
    private UUID id;
    private String roleName;
}