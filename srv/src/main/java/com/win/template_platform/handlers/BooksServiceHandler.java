package com.win.template_platform.handlers;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.cqn.AnalysisResult;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.ql.cqn.CqnInsert;
import com.sap.cds.ql.cqn.CqnSelect;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.persistence.PersistenceService;

import cds.gen.bookservice.AddReviewContext;
import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books;
import cds.gen.bookservice.Reviews;
import cds.gen.bookservice.Reviews_;

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
}
