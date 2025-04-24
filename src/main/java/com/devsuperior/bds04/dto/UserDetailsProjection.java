package com.devsuperior.bds04.dto;

public interface UserDetailsProjection {
    String getUsername();

    String getPassword();

    Long getRoleId();

    String getAuthority();
}
