package com.redmath.Lecture02.news;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;
    private String title;
    private String details;
    private String reportedBy;
    private LocalDateTime reportedAt;

}