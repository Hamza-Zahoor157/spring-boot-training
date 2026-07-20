package com.redmath.Lecture02.news;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class NewsApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllNews() throws Exception {

        mockMvc.perform(get("/api/v1/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void shouldReturnNewsById() throws Exception {

        mockMvc.perform(get("/api/v1/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot 4 Released"));
    }

    @Test
    void shouldCreateNews() throws Exception {

        mockMvc.perform(post("/api/v1/news")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"Testing",
                                  "details":"Unit testing using MockMvc",
                                  "reportedBy":"Hamza"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.newsId").exists())
                .andExpect(jsonPath("$.title").value("Testing"))
                .andExpect(jsonPath("$.details").value("Unit testing using MockMvc"))
                .andExpect(jsonPath("$.reportedBy").value("Hamza"));
    }

    @Test
    void shouldUpdateNews() throws Exception {

        mockMvc.perform(put("/api/v1/news/1")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"Updated Spring Boot Siuuuuu",
                                  "details":"Updated Details",
                                  "reportedBy":"Hamza"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.newsId").value(1))
                .andExpect(jsonPath("$.title").value("Updated Spring Boot Siuuuuu"))
                .andExpect(jsonPath("$.details").value("Updated Details"));
    }

    @Test
    void shouldDeleteNews() throws Exception {

        mockMvc.perform(delete("/api/v1/news/2")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_EDITOR"))))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/news/2"))
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldReturn404WhenUpdatingNonExistingNews() throws Exception {

        String request = """
        {
          "title": "Updated Title",
          "details": "Updated Details",
          "reportedBy": "Hamza"
        }
        """;

        mockMvc.perform(put("/api/v1/news/9999")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("News not found with id: 9999"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/news/9999"));
    }

    @Test
    void shouldReturn400WhenUpdatingWithInvalidNewsId() throws Exception {

        String request = """
        {
          "title": "Updated Title",
          "details": "Updated Details",
          "reportedBy": "Hamza"
        }
        """;

        mockMvc.perform(put("/api/v1/news/-1")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("News id must be greater than zero"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/news/-1"));
    }


    @Test
    void shouldReturn400WhenUpdatingWithInvalidRequestBody() throws Exception {

        String request = """
        {
          "title": "",
          "details": "",
          "reportedBy": ""
        }
        """;

        mockMvc.perform(put("/api/v1/news/1")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .exists())
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/news/1"));
    }
    @Test
    void shouldReturn404WhenNewsDoesNotExist() throws Exception {

        mockMvc.perform(get("/api/v1/news/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectEmptyTitle() throws Exception {

        mockMvc.perform(post("/api/v1/news")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"",
                                  "details":"Some Details",
                                  "reportedBy":"Hamza"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUseAuthenticatedUserAsReportedBy() throws Exception {

        mockMvc.perform(post("/api/v1/news")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"News",
                                  "details":"Some Details",
                                  "reportedBy":""
                                }
                                """))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.reportedBy").value("Hamza"));
    }

    @Test
    void shouldRejectEmptyDetails() throws Exception {

        mockMvc.perform(post("/api/v1/news")
                        .with(jwt().jwt(jwt -> jwt.subject("Hamza"))
                                .authorities(new SimpleGrantedAuthority("ROLE_REPORTER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title":"News",
                                  "details":"",
                                  "reportedBy":"Hamza"
                                }
                                """))
                        .andExpect(status().isBadRequest());
                
    }

}