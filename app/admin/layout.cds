using from '../../srv/books-service';

annotate BookService.Books with @(
    UI.HeaderInfo: {
        TypeName      : '{i18n>HeaderTitle}',
        TypeNamePlural: '{i18n>BookInfo}',
    },
    UI.SelectionFields     : [
        isbn,
    ],
    UI.LineItem  : [
        {
            $Type             : 'UI.DataField',
            Value             : isbn,
            @UI.Importance    : #High,
            @HTML5.CssDefaults: {width: '10em'}
        },
        {
            $Type             : 'UI.DataField',
            Value             : title,
            @UI.Importance    : #High,
            @HTML5.CssDefaults: {width: '10em'}
        },
        {
            $Type         : 'UI.DataField',
            Value         : descr,
            @UI.Importance: #Medium,
        },
        {
            $Type         : 'UI.DataField',
            Value         : price,
            @UI.Importance: #High,
        },
        {
            $Type         : 'UI.DataField',
            Value         : currency_code,
            @UI.Importance: #High,
        },
        {
            $Type: 'UI.DataField',
            Value: stock,
        },
        {
            $Type: 'UI.DataField',
            Value: rating,
        },
    ],

);
