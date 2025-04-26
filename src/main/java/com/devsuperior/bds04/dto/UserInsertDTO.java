package com.devsuperior.bds04.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Campo requerido")
    private String password;

    private Set<Long> roleIds = new HashSet<>();

    public UserInsertDTO() {
        super();
    }

    public UserInsertDTO(String password, Set<Long> roleIds) {
        this.password = password;
        this.roleIds = roleIds;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
