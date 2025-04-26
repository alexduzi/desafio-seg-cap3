package com.devsuperior.bds04.dto;

import java.util.HashSet;
import java.util.Set;

public class UserInsertDTO extends UserDTO {

    private String password;

    private Set<Long> roles = new HashSet<>();

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Long> getRoleIds() {
        return roles;
    }

    public void addRole(Long roleId) {
        roles.add(roleId);
    }

    public UserInsertDTO() {
        super();
    }

    public UserInsertDTO(String password) {
        this.password = password;
    }
}
