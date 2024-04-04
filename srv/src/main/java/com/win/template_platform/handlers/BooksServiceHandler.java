package com.win.template_platform.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.cds.CqnService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;
import com.win.template_platform.common.RatingCalculator;

import cds.gen.bookservice.AddReviewContext;
import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books_;
import cds.gen.bookservice.Reviews;
import cds.gen.bookservice.Reviews_;
import cds.gen.com.win.template.Books;

@Component
@ServiceName(BookService_.CDS_NAME)
public class BooksServiceHandler implements EventHandler {

    @Autowired
    private CdsModel model;

    @Autowired
    private PersistenceService db;

    @Autowired
    private RatingCalculator ratingCalculator;

    @On(event = AddReviewContext.CDS_NAME)
    public void onAddReview(AddReviewContext context) {

        String bookId = (String) CqnAnalyzer.create(model).analyze(context.getCqn()).targetKeys().get(Books.ID);

        Reviews review = Reviews.create();
        review.setBookId(bookId);
        review.setRating(context.getRating());
        review.setTitle(context.getTitle());
        review.setDescr(context.getDescr());

        context.setResult(db.run(Insert.into(Reviews_.CDS_NAME).entry(review)).single(Reviews.class));
    }

    @After(event = AddReviewContext.CDS_NAME)
    public void afterAddedReview(AddReviewContext context) {
        String bookId = context.getResult().getBookId();
        ratingCalculator.setBookRating(bookId);
        db.run(Update.entity(BookService_.BOOKS, b -> b.matching(Books.create(bookId))).data(Books.IS_REVIEWABLE,
                false));
    }

    @Before(event = CqnService.EVENT_READ, entity = Books_.CDS_NAME)
    public void ininBooksBeforeRead() {
        ratingCalculator.initBookRatings();
    }

    @Before(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
    public void initBookBeforeCreate(Books book) {
        book.setStatusCode("A");
        book.setIsbn(getNextIsbn());
    }

    private String getNextIsbn() {
        String isbnPrefix = "Win-";
        String isbnSuffix = "1000000000";
        return isbnPrefix + isbnSuffix;
    }

    @On(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
    public void changeBookOnCreate(Books book) {
        if (book.getStock() == 0) {
            book.setStatusCode("O");
        }
    }

    @On(event = CqnService.EVENT_UPDATE, entity = Books_.CDS_NAME)
    public void changeBookOnUpdate(Books book) {
        book.setStatusCode(book.getStock() == 0 ? "O" : "A");
    }

}
