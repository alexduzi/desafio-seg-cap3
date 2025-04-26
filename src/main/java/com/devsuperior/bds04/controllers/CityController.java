package com.devsuperior.bds04.controllers;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.services.CityService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityDTO>> findAll() {
        List<CityDTO> cities = cityService.findAll();
        return ResponseEntity.ok().body(cities);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityDTO> findAll(@PathVariable Long id) {
        CityDTO dto = cityService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CityDTO> insert(@Valid @RequestBody CityDTO dto) {
        dto = cityService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<CityDTO> update(@PathVariable Long id, @Valid @RequestBody CityDTO dto) {
        dto = cityService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
