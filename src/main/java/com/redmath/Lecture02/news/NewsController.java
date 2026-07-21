package com.redmath.Lecture02.news;

import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping
    @PermitAll
    public Map<String, Object> getAllNews(

            @RequestParam(required = false, defaultValue = "0")
            int page,

            @RequestParam(required = false, defaultValue = "100")
            int size){

        Page<News> news = service.getAllNews(page, size);

        return Map.of(

                "content", news.getContent(),

                "page", news.getNumber(),

                "size", news.getSize()

        );

    }

    @GetMapping("/{newsId}")
    @PermitAll
    public News getNewsById(
            @PathVariable Long newsId){

        return service.getNewsById(newsId);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
        @PreAuthorize("hasAnyAuthority('SCOPE_EDITOR', 'SCOPE_REPORTER')")
    public News createNews(
            Authentication authentication,
            @RequestBody News news) {

        return service.createNews(news, authentication);

    }

    @PutMapping("/{newsId}")
        @PreAuthorize("hasAnyAuthority('SCOPE_EDITOR', 'SCOPE_REPORTER')")
    public ResponseEntity<News> updateNews(
            @PathVariable Long newsId,
            Authentication authentication,
            @RequestBody News news) {

        News updatedNews = service.updateNews(newsId, news, authentication);

        return ResponseEntity.ok(updatedNews);
    }

    @DeleteMapping("/{newsId}")
        @PreAuthorize("hasAuthority('SCOPE_EDITOR')")
    public ResponseEntity<Void> deleteNews(
            @PathVariable Long newsId) {

        service.deleteNews(newsId);

        return ResponseEntity.noContent().build();
    }

}
