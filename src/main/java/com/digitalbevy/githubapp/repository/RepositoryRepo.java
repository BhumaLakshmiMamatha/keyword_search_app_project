package com.digitalbevy.githubapp.repository;

import com.digitalbevy.githubapp.entity.RepositoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryRepo extends JpaRepository<RepositoryEntity, Long> {
    Page<RepositoryEntity> findAll(Pageable pageable);
}
