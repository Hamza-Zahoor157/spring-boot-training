package com.redmath.lecture02.news;

import com.redmath.lecture02.news.dto.NewsRequest;
import com.redmath.lecture02.news.dto.NewsResponse;
import jakarta.annotation.security.PermitAll;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
      int size) {

    Page<NewsResponse> news = service.getAllNews(page, size);

    return Map.of(

        "content", news.getContent(),

        "page", news.getNumber(),

        "size", news.getSize()

    );

  }

  @GetMapping("/{newsId}")
  @PermitAll
  public NewsResponse getNewsById(
      @PathVariable Long newsId) {

    return service.getNewsById(newsId);

  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasAnyAuthority('SCOPE_EDITOR', 'SCOPE_REPORTER')")
  public NewsResponse createNews(
      Authentication authentication,
      @RequestBody NewsRequest request) {

    String reporterName = authentication.getName();

    return service.createNews(request, reporterName);

  }

  @PutMapping("/{newsId}")
  @PreAuthorize("hasAnyAuthority('SCOPE_EDITOR', 'SCOPE_REPORTER')")
  public ResponseEntity<NewsResponse> updateNews(
      @PathVariable Long newsId,
      Authentication authentication,
      @RequestBody NewsRequest request) {

    String username = authentication.getName();
    boolean isEditor = authentication.getAuthorities().stream()
        .anyMatch(authority -> "SCOPE_EDITOR".equals( authority.getAuthority()));

    NewsResponse updatedNews = service.updateNews(newsId, request, username, isEditor);

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
