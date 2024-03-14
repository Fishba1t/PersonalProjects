package com.example.socialnetworksystem.repository;

import com.example.socialnetworksystem.domain.Entity;
import com.example.socialnetworksystem.validators.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    public Optional<E> save(E entity) {
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return Optional.empty();
        }
        else entities.put(entity.getId(),entity);
        return Optional.of(entity);
    }

    public Optional<E> delete(ID id) {
        entities.remove(id);
        return Optional.empty();
    }

}
