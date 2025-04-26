package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.exceptions.DatabaseException;
import com.devsuperior.bds04.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public List<CityDTO> getAll() {
        return cityRepository.findAll().stream().map(CityDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CityDTO getById(Long id) {
        return cityRepository.findById(id).map(CityDTO::new).orElseThrow(() -> new ResourceNotFoundException(format("City with id: %s not found", id)));
    }

    @Transactional
    public CityDTO insert(CityDTO dto) {
        City city = new City();
        city.setName(dto.getName());
        city = cityRepository.save(city);
        return new CityDTO(city);
    }

    @Transactional
    public CityDTO update(Long id, CityDTO dto) {
        try {
            City entity = cityRepository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = cityRepository.save(entity);
            return new CityDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(format("City with id: %s not found", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException(format("City with id: %s not found", id));
        }
        try {
            cityRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }
}
