using BookService as service from '../../srv/books-service';
using from '../ui/layout-list_report';

annotate service.Books with @(
    UI.HeaderInfo                : {
        TypeName      : '{i18n>HeaderTitle}',
        TypeNamePlural: '{i18n>BookInfo}',
        Title         : {Value: title},
        Description   : {Value: isbn},
        TypeImageUrl  : 'sap-icon://education'
    },
    UI.Identification            : [
        {Value: title},
        {
            $Type : 'UI.DataFieldForAction',
            Action: 'BookService.addReview',
            Label : '{i18n>Addreview}'
        }
    ],
    UI.HeaderFacets              : [
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>BookOverview}',
            ID    : 'Header',
            Target: '@UI.FieldGroup#Basic_info'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Target: '@UI.DataPoint#rating'
        }
    ],
    UI.FieldGroup #Basic_info    : {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: title
            },
            {
                $Type: 'UI.DataField',
                Value: isbn
            },
            {
                $Type: 'UI.DataField',
                Value: descr
            }
        ]
    },
    UI.Facets                    : [
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Header}',
            ID    : 'Header',
            Target: '@UI.FieldGroup#Header'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>General}',
            ID    : 'General',
            Target: '@UI.FieldGroup#General'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Translations}',
            Target: 'texts/@UI.LineItem'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Details}',
            ID    : 'Details',
            Target: '@UI.FieldGroup#Details'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : 'Administrative',
            ID    : 'Administrative',
            Target: '@UI.FieldGroup#Administrative'
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Reviews}',
            Target: 'reviews/@UI.LineItem'
        }
    ],
    UI.FieldGroup #Header        : {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: title
            },
            {
                $Type: 'UI.DataField',
                Value: isbn
            }
        ]
    },
    UI.FieldGroup #General       : {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: descr
            },
            {
                $Type: 'UI.DataField',
                Value: status_code
            }
        ]
    },
    UI.FieldGroup #Details       : {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: stock
            },
            {
                $Type: 'UI.DataField',
                Value: price
            },
            {
                $Type : 'UI.DataFieldForAnnotation',
                Target: '@UI.DataPoint#rating'
            }
        ]
    },
    UI.FieldGroup #Administrative: {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: createdAt,
                Label: '{i18n>Createdat}'
            },
            {
                $Type: 'UI.DataField',
                Value: createdBy,
                Label: '{i18n>Createdby}'
            },
            {
                $Type: 'UI.DataField',
                Value: modifiedAt,
                Label: '{i18n>Modifiedat}'
            },
            {
                $Type: 'UI.DataField',
                Value: modifiedBy,
                Label: '{i18n>Modifiedby}'
            }
        ]
    }
);


annotate service.Books.texts with @(UI: {
    Identification : [{Value: title}],
    SelectionFields: [
        locale,
        title
    ],
    LineItem       : [
        {
            Value: locale,
            Label: 'Locale'
        },
        {
            Value: title,
            Label: 'Title'
        },
        {
            Value: descr,
            Label: 'Description'
        },
    ]
});


annotate service.Reviews with @(UI: {
    PresentationVariant        : {
        $Type    : 'UI.PresentationVariantType',
        SortOrder: [{
            $Type     : 'Common.SortOrderType',
            Property  : modifiedAt,
            Descending: true
        }, ],
    },
    LineItem                   : [
        {
            $Type             : 'UI.DataFieldForAnnotation',
            Label             : '{i18n>Rating}',
            Target            : '@UI.DataPoint#rating',
            @HTML5.CssDefaults: {width: '10em'}
        },
        {
            $Type             : 'UI.DataFieldForAnnotation',
            Label             : '{i18n>User}',
            Target            : '@UI.FieldGroup#ReviewerAndDate',
            @HTML5.CssDefaults: {width: '15em'}
        },
        {
            Value             : title,
            Label             : '{i18n>Title}',
            @HTML5.CssDefaults: {width: '15em'}
        },
        {
            Value: descr,
            Label: '{i18n>Description}'
        },
    ],
    DataPoint #rating          : {
        Value        : rating,
        Visualization: #Rating,
        MinimumValue : 0,
        MaximumValue : 5
    },
    FieldGroup #ReviewerAndDate: {Data: [
        {Value: createdBy},
        {Value: modifiedAt}
    ]}
});
