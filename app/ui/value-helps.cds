using from '../../srv/books-service';

annotate BookService.Books with {
    status @Common.ValueListWithFixedValues;
    isbn   @(
        Common.ValueList               : {
            $Type                       : 'Common.ValueListType',
            CollectionPath              : 'Books',
            Label                       : 'ISBN',
            PresentationVariantQualifier: 'vh_Books_isbn',
            Parameters                  : [
                {
                    $Type            : 'Common.ValueListParameterInOut',
                    LocalDataProperty: isbn,
                    ValueListProperty: 'isbn',
                },
                {
                    $Type            : 'Common.ValueListParameterOut',
                    ValueListProperty: 'title',
                    LocalDataProperty: title,
                },
                {
                    $Type            : 'Common.ValueListParameterOut',
                    ValueListProperty: 'descr',
                    LocalDataProperty: descr,
                },
            ],
        },
        Common.ValueListWithFixedValues: false
    );
    rating @(
        Common.ValueList               : {
            $Type                       : 'Common.ValueListType',
            CollectionPath              : 'Books',
            Label                       : 'Rating',
            PresentationVariantQualifier: 'vh_Books_rating',
            Parameters                  : [
                {
                    $Type            : 'Common.ValueListParameterInOut',
                    LocalDataProperty: rating,
                    ValueListProperty: 'rating',
                },
                {
                    $Type            : 'Common.ValueListParameterOut',
                    ValueListProperty: 'title',
                    LocalDataProperty: title,
                },
                {
                    $Type            : 'Common.ValueListParameterOut',
                    ValueListProperty: 'stock',
                    LocalDataProperty: stock,
                },
                {
                    $Type            : 'Common.ValueListParameterOut',
                    ValueListProperty: 'price',
                    LocalDataProperty: price,
                },
            ],
        },
        Common.ValueListWithFixedValues: true
    )
};

annotate BookService.Books with  @(UI.PresentationVariant #vh_Books_isbn: {
    $Type    : 'UI.PresentationVariantType',
    SortOrder: [{
        $Type     : 'Common.SortOrderType',
        Property  : isbn,
        Descending: false,
    }, ],
}, )  @(UI.PresentationVariant #vh_Books_rating: {
    $Type    : 'UI.PresentationVariantType',
    SortOrder: [{
        $Type     : 'Common.SortOrderType',
        Property  : rating,
        Descending: true,
    }, ],
}, );
