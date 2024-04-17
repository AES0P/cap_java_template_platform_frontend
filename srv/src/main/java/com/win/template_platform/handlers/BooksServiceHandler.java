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
import cds.gen.bookservice.Books;
import cds.gen.bookservice.Books_;
import cds.gen.bookservice.Reviews;
import cds.gen.bookservice.Reviews_;

@Component
@ServiceName(BookService_.CDS_NAME)
public class BooksServiceHandler implements EventHandler {

    @Autowired
    private CdsModel model;

    @Autowired
    private PersistenceService db;

    @Autowired
    private RatingCalculator ratingCalculator;

    @Before(event = CqnService.EVENT_READ, entity = Books_.CDS_NAME)
    public void ininBooksBeforeRead() {
        ratingCalculator.initBookRatings();
    }

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
        setBookUnreviewable(bookId, false);
    }

    private void setBookUnreviewable(String bookId, Boolean reviewable) {
        Books book = Books.create();
        book.setId(bookId);
        db.run(Update.entity(BookService_.BOOKS, b -> b.matching(book)).data(Books.IS_REVIEWABLE,
                reviewable));
    }

    @Before(event = CqnService.EVENT_CREATE, entity = Books_.CDS_NAME)
    public void initBookBeforeCreate(Books book) {
        book.setIsbn(getNextIsbn());
    }

    private String getNextIsbn() {
        String isbnPrefix = "Win-";
        String isbnSuffix = "1000000000";
        return isbnPrefix + isbnSuffix;
    }

    @On(event = { CqnService.EVENT_CREATE, CqnService.EVENT_UPDATE }, entity = Books_.CDS_NAME)
    public void changeBookOnUpdate(Books book) {
        book.setStatusCode(book.getStock() == 0 ? "O" : "A");
    }
}
