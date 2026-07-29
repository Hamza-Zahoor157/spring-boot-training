package com.redmath.lecture02.news;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redmath.lecture02.news.dto.NewsRequest;
import com.redmath.lecture02.news.dto.NewsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback
class NewsServiceTest {

  @Autowired
  private NewsService newsService;

  @Autowired
  private NewsRepository newsRepository;

  @Test
  void createNews_validRequest_createsNews() {
    NewsRequest request = new NewsRequest("Test Title", "Test Details");
    NewsResponse response = newsService.createNews(request, "testuser");

    assertNotNull(response.newsId());
    assertEquals("Test Title", response.title());
    assertEquals("Test Details", response.details());
    assertEquals("testuser", response.reportedBy());
    assertNotNull(response.reportedAt());
  }

  @Test
  void createNews_emptyTitle_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest("", "Test Details");
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void createNews_blankTitle_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest("   ", "Test Details");
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void createNews_nullTitle_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest(null, "Test Details");
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void createNews_emptyDetails_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest("Test Title", "");
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void createNews_blankDetails_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest("Test Title", "   ");
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void createNews_nullDetails_throwsInvalidNewsRequestException() {
    NewsRequest request = new NewsRequest("Test Title", null);
    assertThrows(InvalidNewsRequestException.class,
        () -> newsService.createNews(request, "testuser"));
  }

  @Test
  void getNewsById_existingNews_returnsNews() {
    NewsRequest request = new NewsRequest("Find Me", "Details");
    NewsResponse created = newsService.createNews(request, "reporter");
    Long newsId = created.newsId();

    NewsResponse found = newsService.getNewsById(newsId);
    assertEquals(newsId, found.newsId());
    assertEquals("Find Me", found.title());
  }

  @Test
  void getNewsById_invalidId_throwsInvalidNewsIdException() {
    assertThrows(InvalidNewsIdException.class, () -> newsService.getNewsById(-1L));
  }

  @Test
  void getNewsById_zeroId_throwsInvalidNewsIdException() {
    assertThrows(InvalidNewsIdException.class, () -> newsService.getNewsById(0L));
  }

  @Test
  void getNewsById_nonExistingId_throwsNewsNotFoundException() {
    assertThrows(NewsNotFoundException.class, () -> newsService.getNewsById(99999L));
  }

  @Test
  void updateNews_asOwner_updatesSuccessfully() {
    NewsRequest createRequest = new NewsRequest("Original Title", "Original Details");
    NewsResponse created = newsService.createNews(createRequest, "owneruser");
    Long newsId = created.newsId();

    NewsRequest updateRequest = new NewsRequest("Updated Title", "Updated Details");
    NewsResponse updated = newsService.updateNews(newsId, updateRequest, "owneruser", false);
    assertEquals("Updated Title", updated.title());
    assertEquals("Updated Details", updated.details());
  }

  @Test
  void updateNews_asEditorWithoutOwnership_updatesSuccessfully() {
    NewsRequest createRequest = new NewsRequest("Original Title", "Original Details");
    NewsResponse created = newsService.createNews(createRequest, "reporter");
    Long newsId = created.newsId();

    NewsRequest updateRequest = new NewsRequest("Editor Updated", "New Details");
    NewsResponse updated = newsService.updateNews(newsId, updateRequest, "otheruser", true);
    assertEquals("Editor Updated", updated.title());
  }

  @Test
  void updateNews_asNonOwnerNonEditor_throwsAccessDeniedException() {
    NewsRequest createRequest = new NewsRequest("Original Title", "Original Details");
    NewsResponse created = newsService.createNews(createRequest, "owneruser");
    Long newsId = created.newsId();

    NewsRequest updateRequest = new NewsRequest("Hacked", "Hacked Details");
    assertThrows(AccessDeniedException.class,
        () -> newsService.updateNews(newsId, updateRequest, "otheruser", false));
  }

  @Test
  void updateNews_invalidId_throwsInvalidNewsIdException() {
    NewsRequest request = new NewsRequest("Title", "Details");
    assertThrows(InvalidNewsIdException.class,
        () -> newsService.updateNews(-1L, request, "user", false));
  }

  @Test
  void updateNews_zeroId_throwsInvalidNewsIdException() {
    NewsRequest request = new NewsRequest("Title", "Details");
    assertThrows(InvalidNewsIdException.class,
        () -> newsService.updateNews(0L, request, "user", false));
  }

  @Test
  void updateNews_nonExistingId_throwsNewsNotFoundException() {
    NewsRequest request = new NewsRequest("Title", "Details");
    assertThrows(NewsNotFoundException.class,
        () -> newsService.updateNews(99999L, request, "user", false));
  }

  @Test
  void deleteNews_existingNews_deletesSuccessfully() {
    NewsRequest request = new NewsRequest("To Be Deleted", "Details");
    NewsResponse created = newsService.createNews(request, "reporter");
    Long newsId = created.newsId();

    newsService.deleteNews(newsId);

    assertThrows(NewsNotFoundException.class, () -> newsService.getNewsById(newsId));
  }

  @Test
  void deleteNews_invalidId_throwsInvalidNewsIdException() {
    assertThrows(InvalidNewsIdException.class, () -> newsService.deleteNews(-1L));
  }

  @Test
  void deleteNews_zeroId_throwsInvalidNewsIdException() {
    assertThrows(InvalidNewsIdException.class, () -> newsService.deleteNews(0L));
  }

  @Test
  void deleteNews_nonExistingId_throwsNewsNotFoundException() {
    assertThrows(NewsNotFoundException.class, () -> newsService.deleteNews(99999L));
  }

  @Test
  void getAllNews_returnsPageOfNews() {
    NewsRequest request = new NewsRequest("Page Test", "Details");
    newsService.createNews(request, "reporter");

    var page = newsService.getAllNews(0, 10);
    assertTrue(page.getTotalElements() >= 1);
  }

  @Test
  void getAllNews_negativePage_resetsToZero() {
    var page = newsService.getAllNews(-1, 10);
    assertEquals(0, page.getNumber());
  }

  @Test
  void getAllNews_zeroSize_resetsToDefault() {
    var page = newsService.getAllNews(0, 0);
    assertEquals(100, page.getSize());
  }

  @Test
  void getAllNews_largeSize_resetsToDefault() {
    var page = newsService.getAllNews(0, 101);
    assertEquals(100, page.getSize());
  }

  @Test
  void getAllNews_negativeSize_resetsToDefault() {
    var page = newsService.getAllNews(0, -5);
    assertEquals(100, page.getSize());
  }
}