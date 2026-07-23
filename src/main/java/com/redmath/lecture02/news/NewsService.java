package com.redmath.lecture02.news;

import com.redmath.lecture02.news.dto.NewsRequest;
import com.redmath.lecture02.news.dto.NewsResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsService {

  private final NewsRepository repository;

  public NewsService(NewsRepository repository) {
    this.repository = repository;
  }

  @Transactional(readOnly = true)
  public Page<NewsResponse> getAllNews(int page, int size) {
    if (page < 0) {
      page = 0;
    }
    if (size <= 0 || size > 100) {
      size = 100;
    }

    return repository.findAll(
        PageRequest.of(page, size)
    ).map(this::toNewsResponse);

  }

  @Transactional(readOnly = true)
  public NewsResponse getNewsById(Long newsId) {

    validateNewsId(newsId);

    return toNewsResponse(repository.findById(newsId)
        .orElseThrow(() -> new NewsNotFoundException(newsId)));

  }

  @Transactional
  public NewsResponse updateNews(Long newsId, NewsRequest request, String username, boolean isEditor) {

    validateNewsId(newsId);

    validate(request);

    News existingNews = repository.findById(newsId)
        .orElseThrow(() ->
            new NewsNotFoundException(newsId));

    boolean isOwner = Objects.equals(existingNews.getReportedBy(), username);
    if (!isEditor && !isOwner) {
      throw new AccessDeniedException("Reporters can only update their own news");
    }

    existingNews.setTitle(request.title());
    existingNews.setDetails(request.details());
    existingNews.setReportedAt(LocalDateTime.now());

    return toNewsResponse(repository.save(existingNews));
  }

  @Transactional
  public NewsResponse createNews(NewsRequest request, String reporterName) {

    validate(request);

    News news = new News();

    news.setTitle(request.title());
    news.setDetails(request.details());
    news.setReportedBy(reporterName);
    news.setReportedAt(LocalDateTime.now());

    return toNewsResponse(repository.save(news));

  }

  @Transactional
  public void deleteNews(Long newsId) {

    validateNewsId(newsId);

    News existingNews = repository.findById(newsId)
        .orElseThrow(() -> new NewsNotFoundException(newsId));

    repository.delete(existingNews);
  }

  private void validate(NewsRequest request) {

    if (request.title() == null ||
        request.title().isBlank()) {

      throw new InvalidNewsRequestException("Title cannot be empty.");

    }

    if (request.details() == null ||
        request.details().isBlank()) {

      throw new InvalidNewsRequestException("Details cannot be empty.");

    }

  }

  private void validateNewsId(Long newsId) {
    if (newsId == null || newsId <= 0) {
      throw new InvalidNewsIdException("News id must be greater than zero");
    }
  }

  private NewsResponse toNewsResponse(News news) {
    return new NewsResponse(
        news.getNewsId(),
        news.getTitle(),
        news.getDetails(),
        news.getReportedBy(),
        news.getReportedAt()
    );
  }

}
