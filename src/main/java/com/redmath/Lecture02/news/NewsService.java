package com.redmath.Lecture02.news;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NewsService {

    private final NewsRepository repository;

    public NewsService(NewsRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    public Page<News> getAllNews(int page, int size) {
        if(page < 0){
            page = 0;
        }
        if(size <= 0 || size > 100){
            size = 100;
        }

        return repository.findAll(
                PageRequest.of(page, size)
        );

    }
    @Transactional(readOnly = true)
    public News getNewsById(Long newsId) {

        validateNewsId(newsId);

        return repository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

    }
    @Transactional
    public News updateNews(Long newsId, News updatedNews) {

        validateNewsId(newsId);

        validate(updatedNews);

        News existingNews = repository.findById(newsId)
                .orElseThrow(() ->
                new NewsNotFoundException(newsId));

        existingNews.setTitle(updatedNews.getTitle());
        existingNews.setDetails(updatedNews.getDetails());
        existingNews.setReportedBy(updatedNews.getReportedBy());
        existingNews.setReportedAt(LocalDateTime.now());

        return repository.save(existingNews);
    }
    @Transactional
    public News createNews(News news) {

        validate(news);
        news.setReportedAt(LocalDateTime.now());
        return repository.save(news);

    }
    @Transactional
    public void deleteNews(Long newsId) {

        validateNewsId(newsId);

        News existingNews = repository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException(newsId));

        repository.delete(existingNews);
    }

    private void validate(News news) {

        if (news.getTitle() == null ||
                news.getTitle().isBlank()) {

            throw new InvalidNewsRequestException("Title cannot be empty.");

        }

        if (news.getReportedBy() == null ||
                news.getReportedBy().isBlank()) {

            throw new InvalidNewsRequestException("Reported By cannot be empty.");

        }

        if (news.getDetails() == null ||
                news.getDetails().isBlank()) {

            throw new InvalidNewsRequestException("Details cannot be empty.");

        }

    }

    private void validateNewsId(Long newsId) {
        if (newsId == null || newsId <= 0) {
            throw new InvalidNewsIdException("NewsId must be valid.");
        }
    }

}