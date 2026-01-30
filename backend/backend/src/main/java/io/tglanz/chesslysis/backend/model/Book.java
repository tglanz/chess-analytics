package io.tglanz.chesslysis.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {
  @Id private String isbn;

  @NotNull private String title;

  Book() {}

  public Book(String isbn, String title) {
    this.isbn = isbn;
    this.title = title;
  }

  public String getTitle() {
    return title;
  }
}
