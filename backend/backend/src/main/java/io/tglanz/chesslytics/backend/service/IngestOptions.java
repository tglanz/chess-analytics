package io.tglanz.chesslytics.backend.service;

/**
 * Configuration options for data ingestion.
 *
 * @param username Chess.com username to ingest
 * @param order Order of ingestion: ASC (oldest first) or DESC (newest first)
 * @param limit Maximum number of games to ingest (null = unlimited)
 */
public record IngestOptions(String username, Order order, Integer limit) {

  public enum Order {
    ASC,
    DESC
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String username;
    private Order order = Order.DESC; // Default: newest first
    private Integer limit = null; // Default: unlimited

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder order(Order order) {
      this.order = order;
      return this;
    }

    public Builder order(String order) {
      this.order = Order.valueOf(order.toUpperCase());
      return this;
    }

    public Builder limit(Integer limit) {
      this.limit = limit;
      return this;
    }

    public IngestOptions build() {
      if (username == null || username.isBlank()) {
        throw new IllegalArgumentException("Username is required");
      }
      if (limit != null && limit <= 0) {
        throw new IllegalArgumentException("Limit must be positive");
      }
      return new IngestOptions(username, order, limit);
    }
  }
}
