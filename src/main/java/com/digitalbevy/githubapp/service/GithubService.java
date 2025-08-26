package com.digitalbevy.githubapp.service;

import com.digitalbevy.githubapp.entity.RepositoryEntity;
import com.digitalbevy.githubapp.repository.RepositoryRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class GithubService {
    private final RepositoryRepo repository;

    @Value("${app.github.per-page-default:10}")
    private int defaultPerPage;

    public GithubService(RepositoryRepo repository) {
        this.repository = repository;
    }

    public List<RepositoryEntity> searchAndSaveRepos(String keyword, Integer perPage) {
        int limit = perPage != null && perPage > 0 ? perPage : defaultPerPage;
        String url = "https://api.github.com/search/repositories?q=" + keyword + "&per_page=" + limit;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        Map<String, Object> response;
        try {
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            response = resp.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to call GitHub API: " + e.getMessage(), e);
        }

        List<Map<String, Object>> items = response != null ? (List<Map<String, Object>>) response.get("items") : List.of();
        List<RepositoryEntity> saved = new ArrayList<>();
        for (Map<String, Object> item : items) {
            Map<String, Object> owner = (Map<String, Object>) item.get("owner");
            RepositoryEntity r = RepositoryEntity.builder()
                    .name((String) item.getOrDefault("name", ""))
                    .url((String) item.getOrDefault("html_url", ""))
                    .stars(((Number) item.getOrDefault("stargazers_count", 0)).intValue())
                    .owner(owner != null ? (String) owner.getOrDefault("login", "") : null)
                    .language((String) item.getOrDefault("language", null))
                    .description((String) item.getOrDefault("description", null))
                    .build();
            saved.add(repository.save(r));
        }
        return saved;
    }

    public Page<RepositoryEntity> getReposPaged(int page, int size) {
        return repository.findAll(PageRequest.of(page, size));
    }
}
