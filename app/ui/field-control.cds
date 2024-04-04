using from '../../srv/books-service';
using from '../ui/labels';

annotate BookService.Books with {
  ID    @UI.Hidden  @UI.HiddenFilter;
  isbn  @Common.FieldControl : #ReadOnly;
  descr @UI.MultiLineText    : true;
  price @Measures.ISOCurrency: currency_code;
  rating @readonly;
} actions {
  @(
    Common.SideEffects             : {
      TargetProperties: ['_it/rating'],
      TargetEntities  : [
        _it,
        _it.reviews
      ]
    },
    cds.odata.bindingparameter.name: '_it',
    Core.OperationAvailable        : _it.isReviewable
  )
  addReview(rating  @mandatory,  title  @mandatory,  descr  @mandatory  )
};

annotate BookService.Reviews with {
  descr @UI.MultiLineText: true;
};

annotate BookService.Books.texts with {
  descr @UI.MultiLineText: true;
};
