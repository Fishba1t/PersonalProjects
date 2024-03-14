package com.example.socialnetworksystem.repository;

import com.example.socialnetworksystem.domain.Entity;
import com.example.socialnetworksystem.validators.Validator;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    protected String url;
    protected String username;
    protected String password;
    protected String tableName;

    public AbstractDatabaseRepository(String url, String username, String password, String tableName, Validator<E> validator) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        this.tableName = tableName;
        loadData();
    }

    public abstract void loadData();

    protected abstract Iterable<E> findAll_DB();

}