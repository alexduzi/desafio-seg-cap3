package com.devsuperior.bds04.services;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.exceptions.DatabaseException;
import com.devsuperior.bds04.exceptions.ResourceNotFoundException;
import com.devsuperior.bds04.repositories.CityRepository;
import com.devsuperior.bds04.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static java.lang.String.format;

@Service
public class EventService {

    private final EventRepository eventRepository;

    private final CityRepository cityRepository;

    public EventService(EventRepository eventRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        Page<Event> events = eventRepository.findAll(pageable);
        return events.map(EventDTO::new);
    }

    @Transactional(readOnly = true)
    public EventDTO findById(Long id) {
        return eventRepository.findById(id).map(EventDTO::new).orElseThrow(() -> new ResourceNotFoundException(format("Event with id: %s not found", id)));
    }

    @Transactional
    public EventDTO insert(EventDTO dto) {
        Event event = new Event();
        event.setName(dto.getName());
        event.setDate(dto.getDate());
        event.setUrl(dto.getUrl());
        if (dto.getCityId() == null) {
            throw new IllegalArgumentException("City ID cannot be empty");
        }
        City city = cityRepository.findById(dto.getCityId()).orElseThrow(() -> new ResourceNotFoundException(format("City with id: %s not found", dto.getCityId())));
        event.setCity(city);
        event = eventRepository.save(event);
        return new EventDTO(event);
    }

    @Transactional
    public EventDTO update(Long id, EventDTO dto) {
        try {
            Event entity = eventRepository.getReferenceById(id);
            if (dto.getCityId() != null) {
                City city = cityRepository.findById(dto.getCityId()).orElseThrow(() -> new ResourceNotFoundException(format("City with id: %s not found", id)));
                entity.setCity(city);
            }
            entity.setName(dto.getName());
            entity.setUrl(dto.getUrl());
            entity.setDate(dto.getDate());
            entity = eventRepository.save(entity);
            return new EventDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(format("Event with id: %s not found", id));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException(format("Event with id: %s not found", id));
        }
        try {
            eventRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Data integrity violation");
        }
    }
}
