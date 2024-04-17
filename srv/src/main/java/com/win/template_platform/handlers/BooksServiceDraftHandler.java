package com.win.template_platform.handlers;

import static cds.gen.bookservice.BookService_.BOOKS;

import java.math.BigDecimal;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sap.cds.ql.Select;
import com.sap.cds.ql.Update;
import com.sap.cds.ql.cqn.CqnAnalyzer;
import com.sap.cds.reflect.CdsModel;
import com.sap.cds.services.ErrorStatuses;
import com.sap.cds.services.ServiceException;
import com.sap.cds.services.draft.DraftCancelEventContext;
import com.sap.cds.services.draft.DraftCreateEventContext;
import com.sap.cds.services.draft.DraftEditEventContext;
import com.sap.cds.services.draft.DraftNewEventContext;
import com.sap.cds.services.draft.DraftPatchEventContext;
import com.sap.cds.services.draft.DraftPrepareEventContext;
import com.sap.cds.services.draft.DraftReadEventContext;
import com.sap.cds.services.draft.DraftSaveEventContext;
import com.sap.cds.services.draft.DraftService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.Before;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.win.template_platform.common.MessageKeys;

import cds.gen.bookservice.BookService;
import cds.gen.bookservice.BookService_;
import cds.gen.bookservice.Books;

@Component
@ServiceName(BookService_.CDS_NAME)
public class BooksServiceDraftHandler implements EventHandler {

    @Autowired
    private CdsModel model;

    @Autowired
    private BookService.Draft bookDraft;

    @Before(event = DraftService.EVENT_DRAFT_NEW)
    public void prefillBookInfo(DraftNewEventContext context, Books book) {
        System.out.println("prefillBookInfo" + book.toJson());
        book.setIsReviewable(false);
    }

    @Before(event = DraftService.EVENT_DRAFT_CREATE)
    public void validateBookInfo(DraftCreateEventContext context, Books book) {
        System.out.println("validateBookInfo" + book.toJson());
    }

    @Before(event = DraftService.EVENT_DRAFT_PATCH)
    public void calculateData(DraftPatchEventContext context, Books book) {
        System.out.println("calculateData" + book.toJson());
        if (book.getStock() != null) {
            Integer stock = book.getStock();
            if (stock == 0) {
                book.setPrice(BigDecimal.valueOf(0));
            }
            book.setStatusCode(stock == 0 ? "O" : "A");
        }
    }

    @Before(event = DraftService.EVENT_DRAFT_SAVE)
    public void saveDraft(DraftSaveEventContext context) {
        Books book = getBookDraft(getBookId(context), false);
        System.out.println("saveDraft" + book.toJson());
        updateBookDraft(book, false, Books.DESCR, "saveDraft" + book.getDescr());
    }

    @Before(event = DraftService.EVENT_DRAFT_CANCEL)
    public void deleteDraft(DraftCancelEventContext context) {
        System.out.println("deleteDraft" + getBookDraft(getBookId(context), false).toJson());
    }

    @Before(event = DraftService.EVENT_DRAFT_EDIT)
    public void editDraft(DraftEditEventContext context) {
        Books book = getBookDraft(getBookId(context), true);
        System.out.println("editDraft" + book.toJson());
        if (book.getDescr().contains("winter")) {
            throw new ServiceException(ErrorStatuses.FORBIDDEN, "You cannot edit win's book!");
        }
    }

    @Before(event = DraftService.EVENT_DRAFT_READ)
    public void readDraft(DraftReadEventContext context) {
        System.out.println("readDraft");
    }

    // @Before(event = DraftService.EVENT_DRAFT_PREPARE)
    public void prepareDraft(DraftPrepareEventContext context) {
        System.out.println("prepareDraft");
    }

    private String getBookId(DraftSaveEventContext context) {
        return (String) CqnAnalyzer.create(model).analyze(context.getCqn()).targetKeys().get(Books.ID);
    }

    private String getBookId(DraftCancelEventContext context) {
        return (String) CqnAnalyzer.create(model).analyze(context.getCqn()).targetKeys().get(Books.ID);
    }

    private String getBookId(DraftEditEventContext context) {
        return (String) CqnAnalyzer.create(model).analyze(context.getCqn()).targetKeys().get(Books.ID);
    }

    private Books getBookDraft(String bookId, Boolean isActiveEntity) {
        return bookDraft
                .run(Select.from(BOOKS).where(o -> o.ID().eq(bookId).and(o.IsActiveEntity().eq(isActiveEntity))))
                .first(Books.class).orElseThrow(notFound(MessageKeys.BOOK_MISSING));
    }

    private Books updateBookDraft(Books book, Boolean isActiveEntity, String column, Object value) {
        bookDraft.patchDraft(Update.entity(BOOKS)
                .where(o -> o.ID().eq(book.getId()).and(o.IsActiveEntity().eq(isActiveEntity)))
                .data(column, value));
        return book;
    }

    private Supplier<ServiceException> notFound(String message) {
        return () -> new ServiceException(ErrorStatuses.NOT_FOUND, message);
    }
}
