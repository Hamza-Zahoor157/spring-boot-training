package com.redmath.lecture02.news;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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