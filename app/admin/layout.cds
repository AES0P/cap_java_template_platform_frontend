using BookService from '../../srv/books-service';


annotate BookService.Books with @(
    UI.HeaderInfo         : {
        TypeName      : '{i18n>HeaderTitle}',
        TypeNamePlural: '{i18n>BookInfo}',
    },
    UI.SelectionFields    : [
        isbn,
        title,
        descr,
        rating,
        status_code,
        currency_code,
    ],
    UI.LineItem           : [
        {
            $Type: 'UI.DataField',
            Value: ID,
        },
        {
            $Type             : 'UI.DataField',
            Value             : isbn,
            @UI.Importance    : #High,
            @HTML5.CssDefaults: {width: '10em'}
        },
        {
            $Type             : 'UI.DataField',
            Value             : status_code,
            @UI.Importance    : #High,
            @HTML5.CssDefaults: {width: '6em'}
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
            @HTML5.CssDefaults: {width: '15em'}
        },
        {
            $Type : 'UI.DataFieldForAnnotation',
            Target: '@UI.DataPoint#rating',
        },
        {
            $Type         : 'UI.DataField',
            Value         : price,
            @UI.Importance: #High,
        },
        {
            $Type: 'UI.DataField',
            Value: stock,
        },
        {
            $Type             : 'UI.DataFieldForAction',
            Action            : 'BookService.addReview',
            Label             : '{i18n>Addreview}',
            Inline            : true,
            InvocationGrouping: #Isolated,
            @UI.Importance    : #Medium,
        },
        {
            $Type             : 'UI.DataFieldForAction',
            Action            : 'BookService.addReview',
            Label             : '{i18n>Addreview}',
            Inline            : false,
            InvocationGrouping: #Isolated,
            @UI.Importance    : #Medium,
        },
    ],
    UI.DataPoint #rating  : {
        Value        : rating,
        Visualization: #Rating,
        TargetValue  : 5
    },
    UI.PresentationVariant: {
        Text          : 'Default',
        SortOrder     : [{
            $Type     : 'Common.SortOrderType',
            Property  : isbn,
            Descending: false
        }],
        GroupBy       : [currency.code],
        Total         : [
            price,
            stock
        ],
        Visualizations: ['@UI.LineItem'],
    },

);
