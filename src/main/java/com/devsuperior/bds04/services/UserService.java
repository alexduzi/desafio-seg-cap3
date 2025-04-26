package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.dto.UserInsertDTO;
import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.RoleRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.lang.String.format;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        return userRepository.findById(id).map(UserDTO::new)
                .orElseThrow(() -> new ResourceNotFoundException(format("User with id: %s not found", id)));
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User user = new User();

        if (dto.getRoleIds().isEmpty()) {
            throw new IllegalArgumentException("Role IDs cannot be empty");
        }

        Set<Role> roles = Set.copyOf(roleRepository.findAllById(dto.getRoleIds()));

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException(format("Roles not found: %s", dto.getRoleIds()));
        }

        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().addAll(roles);
        user = userRepository.save(user);
        return new UserDTO(user);
    }
}
