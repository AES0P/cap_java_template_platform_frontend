package com.win.template_platform.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.cqn.AnalysisResult;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.cds.CdsUpdateEventContext;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.win.template_platform.common.MessageKeys;

import cds.gen.bookservice.AddReviewContext;
import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books;
import cds.gen.bookservice.Books_;
import cds.gen.bookservice.Reviews;
import cds.gen.bookservice.Reviews_;
import cds.gen.bookservice.Upload;
import cds.gen.bookservice.Upload_;

@Component
@ServiceName(BookService_.CDS_NAME)
public class BooksServiceHandler implements EventHandler {

    private final CqnAnalyzer analyzer;
    private final PersistenceService db;

    BooksServiceHandler(PersistenceService db, CdsModel model) {
        this.db = db;
        this.analyzer = CqnAnalyzer.create(model);
    }

    @On(event = AddReviewContext.CDS_NAME)
    public void addReview(AddReviewContext context) {

        System.out.println("test result from here：/");

        CqnSelect select = context.getCqn();
        AnalysisResult result = analyzer.analyze(select);
        Map<String, Object> targetKeys = result.targetKeys();
        String bookId = (String) targetKeys.get(Books.ID);

        Reviews review = Reviews.create();
        review.setBookId(bookId);
        review.setRating(context.getRating());
        review.setTitle(context.getTitle());
        review.setDescr(context.getDescr());

        // CqnSelect select2 = Select.from(Reviews_.CDS_NAME).byId(bookId);
        CqnInsert insert = Insert.into(Reviews_.CDS_NAME).entry(review);
        Result dbResult = db.run(insert);
        Reviews newReview = dbResult.single(Reviews.class);

        context.setResult(newReview);
    }

    @On(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
    public void changeUrgencyDueToSubject(Books book) {
        book.setStatusCode("A");
        if (book.getStock() == 0) {
            book.setStatusCode("O");
        }
    }

    /**
     * @return the static CSV singleton upload entity
     */
    @On(entity = Upload_.CDS_NAME, event = CqnService.EVENT_READ)
    public Upload getUploadSingleton() {
        return Upload.create();
    }

    @On
    public void addBooksViaCsv(CdsUpdateEventContext context, Upload upload) {
        InputStream is = upload.getCsv();
        if (is != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                br.lines().skip(1).forEach((line) -> {
                    String[] p = line.split(";");
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

                    // separate transaction per line
                    context.getCdsRuntime().changeSetContext().run(ctx -> {
                        db.run(Insert.into(BookService_.BOOKS).entry(book));
                    });
                });
            } catch (IOException e) {
                throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.BOOK_IMPORT_FAILED, e);
            } catch (IndexOutOfBoundsException e) {
                throw new ServiceException(ErrorStatuses.SERVER_ERROR, MessageKeys.BOOK_IMPORT_INVALID_CSV, e);
            }
        }
        context.setResult(Arrays.asList(upload));
    }
}
