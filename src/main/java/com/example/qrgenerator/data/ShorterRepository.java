package com.example.qrgenerator.data;

import org.springframework.data.repository.CrudRepository;

public interface ShorterRepository extends CrudRepository<Shorter, Long> {
    Shorter findByHash(String hash);
}