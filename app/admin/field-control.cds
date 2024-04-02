using BookService from '../../srv/books-service';


annotate BookService.Books with {
  ID       @UI.Hidden  @UI.HiddenFilter;
  isbn     @Common.FieldControl : #ReadOnly;
  descr    @UI.MultiLineText    : true;
  price    @Measures.ISOCurrency: currency.code;
  // currency @UI.Hidden;
  review   @UI.Hidden  @UI.HiddenFilter;
};
