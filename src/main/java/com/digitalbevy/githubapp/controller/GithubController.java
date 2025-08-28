package com.digitalbevy.githubapp.controller;

import com.digitalbevy.githubapp.entity.RepositoryEntity;
import com.digitalbevy.githubapp.service.GithubService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/repos")
/*@CrossOrigin(origins = "http://localhost:5173"*/
@CrossOrigin(origins = "*")

public class GithubController {
    private final GithubService service;

    public GithubController(GithubService service) {
        this.service = service;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchRepos(@RequestParam String keyword,
                                         @RequestParam(required = false) Integer perPage) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "keyword is required"));
        }
        try {
            List<RepositoryEntity> res = service.searchAndSaveRepos(keyword.trim(), perPage);
            return ResponseEntity.ok(res);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Upstream API error", "details", ex.getMessage()));
        }
    }

    @GetMapping
    public Page<RepositoryEntity> getRepos(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        return service.getReposPaged(page, size);
    }
}
