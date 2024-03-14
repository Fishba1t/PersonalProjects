package com.example.socialnetworksystem.service;

import com.example.socialnetworksystem.domain.Utilizator;

@FunctionalInterface
public interface ServiceInterface {
    Iterable<Utilizator> getAllUtilizatori();
}
