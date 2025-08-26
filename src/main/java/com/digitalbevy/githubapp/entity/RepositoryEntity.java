package com.digitalbevy.githubapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "repository_info")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class RepositoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String url;

    private int stars;

    @Column(length = 128)
    private String owner;

    @Column(length = 128)
    private String language;

    @Column(columnDefinition = "TEXT")
    private String description;
}
