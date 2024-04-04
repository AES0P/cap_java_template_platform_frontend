package com.win.template_platform.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cds.ql.Insert;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.win.template_platform.common.MessageKeys;

import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books;
import cds.gen.bookservice.Upload;
import cds.gen.bookservice.Upload_;

@Component
@ServiceName(BookService_.CDS_NAME)
public class UploadingHandler implements EventHandler {

    @Autowired
    private PersistenceService db;

    /**
     * @return the static CSV singleton upload entity
     */
    @On(entity = Upload_.CDS_NAME, event = CqnService.EVENT_READ)
    public Upload getUploadSingleton() {
        return Upload.create();
    }

    /**
     * add books via CSV file
     *
     * @param context CdsUpdateEventContext
     * @param upload  the upload entity
     */
    @On(event = CqnService.EVENT_UPDATE, entity = Upload_.CDS_NAME)
    public void addBooksViaCsv(CdsUpdateEventContext context, Upload upload) {
        InputStream is = upload.getCsv();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                List<String> lines = br.lines().skip(1).collect(Collectors.toList());
                processLines(context, lines);
            } catch (IOException e) {
                throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.BOOK_IMPORT_FAILED, e);
            }
        }
        context.setResult(Arrays.asList(upload));
    }

    /**
     * process lines
     *
     * @param context CdsUpdateEventContext
     * @param lines   each line of csv
     */
    private void processLines(CdsUpdateEventContext context, List<String> lines) {
        lines.forEach((line) -> {
            String[] p = line.split(";");
            if (p.length < 9) {
                throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.BOOK_IMPORT_INVALID_CSV,
                        new IllegalArgumentException("There are no enough fields in the CSV file: " + line));
            }

            // transaction per line
            context.getCdsRuntime().changeSetContext().run(ctx -> {
                insertBook(createBook(p));
            });
        });
    }

    /**
     * create book object from CSV line
     *
     * @param p array of CSV line
     * @return the created book object
     */
    private Books createBook(String[] p) {
        Books book = Books.create();
        book.setId(p[0]);
        book.setTitle(p[1]);
        book.setDescr(p[2]);
        book.setStock(Integer.valueOf(p[3]).intValue());
        book.setPrice(BigDecimal.valueOf(Double.valueOf(p[4])));
        book.setCurrencyCode(p[5]);
        book.setRating(BigDecimal.valueOf(Double.valueOf(p[6])));
        book.setIsbn(p[7]);
        book.setStatusCode(p[8]);
        return book;
    }

    /**
     * insert book to databas
     *
     * @param book the book object
     */
    private void insertBook(Books book) {
        db.run(Insert.into(BookService_.BOOKS).entry(book));
    }

}
