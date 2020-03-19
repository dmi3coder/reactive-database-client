package tech.donau;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.mysqlclient.MySQLPool;
import io.vertx.mutiny.sqlclient.Row;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/book")
public class BookResource {
    @Inject
    MySQLPool pool;

    public void onStart(@Observes StartupEvent event) {
        pool.query("DROP TABLE IF EXISTS books")
                .then(it -> pool.query("CREATE TABLE books(title text,pages int)"))
                .then(it -> pool.query("INSERT INTO books values('test book', 200)"))
                .then(it -> pool.query("INSERT INTO books values('another test book', 300)"))
                .subscribeAsCompletionStage()
                .join();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public CompletionStage<List<Book>> hello() {
        return pool.query("SELECT * FROM books")
                .map(rows -> {
                    final List<Book> books = new ArrayList<>();
                    for (Row row : rows) {
                        books.add(Book.from(row));
                    }
                    return books;
                }).subscribeAsCompletionStage();
    }
}