using from '../../srv/books-service';

annotate BookService.Books with @title: '{i18n>Books}' {
    isbn     @Common.Label            : '{i18n>Isbn}';
    title    @Common.Label            : '{i18n>Title}';
    descr    @Common.Label            : '{i18n>Description}';
    price    @Common.Label            : '{i18n>Price}';
    currency @Common.Label            : '{i18n>Code}';
    stock    @Common.Label            : '{i18n>Stock}';
    rating   @Common.Label            : '{i18n>Rating}';
};
