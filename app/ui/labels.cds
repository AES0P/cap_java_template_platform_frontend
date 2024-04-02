using from '../../srv/books-service';

annotate BookService.Books with @title: '{i18n>Books}' {
    ID       @Common.Label            : '{i18n>ID}';
    isbn     @Common.Label            : '{i18n>Isbn}';
    title    @Common.Label            : '{i18n>Title}';
    descr    @Common.Label            : '{i18n>Description}';
    price    @Common.Label            : '{i18n>Price}';
    currency @Common.Label            : '{i18n>Code}';
    stock    @Common.Label            : '{i18n>Stock}';
    rating   @Common.Label            : '{i18n>Rating}';
    status   @Common.Label            : '{i18n>Status}'  @Common.Text  : status.name  @Common.TextArrangement: #TextOnly;
} actions {
    addReview(rating            @title: '{i18n>Rating}',  title  @title: '{i18n>Title}',  descr  @title      : '{i18n>Description}'  )
};

annotate BookService.status with {
    code  @Common.Text: name  @Common.TextArrangement: #TextOnly
}
