package com.spring.github.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.github.entity.RepoEntity;

@Repository
public interface RepoRepository extends JpaRepository<RepoEntity, Long> {
}
