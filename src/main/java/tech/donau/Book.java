package tech.donau;

import io.vertx.axle.sqlclient.Row;

public class Book {
    public String title;
    public Integer pages;

    public Book() {
    }

    public static Book from(Row row) {
        final Book book = new Book();
        book.title = row.getString("title");
        book.pages = row.getInteger("pages");
        return book;
    }
}
