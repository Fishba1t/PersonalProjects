package com.example.socialnetworksystem.repository;

import com.example.socialnetworksystem.domain.Entity;

@FunctionalInterface
public interface Repository<ID, E extends Entity<ID>> {
    Iterable<E> findAll();

}
