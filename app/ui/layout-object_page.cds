using BookService as service from '../../srv/books-service';
using from '../ui/layout-list_report';
using from '../ui/field-control';

annotate service.Books with @(UI.HeaderInfo: {
    TypeName      : '{i18n>HeaderTitle}',
    TypeNamePlural: '{i18n>BookInfo}',
    Title         : {
        $Type: 'UI.DataField',
        Value: title,
    },
    TypeImageUrl  : 'sap-icon://education',
});

annotate service.Books with @(UI.Identification: [{
    $Type : 'UI.DataFieldForAction',
    Action: 'BookService.addReview',
    Label : '{i18n>Addreview}'
}, ]);

annotate service.Books with @(
    UI.HeaderFacets    : [{
        $Type : 'UI.ReferenceFacet',
        Label : '{i18n>BookOverview}',
        ID    : 'Header',
        Target: '@UI.FieldGroup#Header1',
    }, ],
    UI.FieldGroup #Test: {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: title,
            },
            {
                $Type: 'UI.DataField',
                Value: isbn,
            },
        ],
    }
);

annotate service.Books with @(UI.FieldGroup #Header1: {
    $Type: 'UI.FieldGroupType',
    Data : [
        {
            $Type: 'UI.DataField',
            Value: title,
        },
        {
            $Type: 'UI.DataField',
            Value: isbn,
        },
        {
            $Type: 'UI.DataField',
            Value: descr,
        },
    ],
});

annotate service.Books with @(
    UI.Facets            : [
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Header}',
            ID    : 'Header',
            Target: '@UI.FieldGroup#Header',
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>General}',
            ID    : 'General',
            Target: '@UI.FieldGroup#General',
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : '{i18n>Details}',
            ID    : 'Details',
            Target: '@UI.FieldGroup#Details',
        },
        {
            $Type : 'UI.ReferenceFacet',
            Label : 'Administrative',
            ID    : 'Administrative',
            Target: '@UI.FieldGroup#Administrative',
        },
    ],
    UI.FieldGroup #Header: {
        $Type: 'UI.FieldGroupType',
        Data : [
            {
                $Type: 'UI.DataField',
                Value: title,
            },
            {
                $Type: 'UI.DataField',
                Value: isbn,
            },
        ],
    }
);

annotate service.Books with {
    isbn @Common.FieldControl: #Optional
};

annotate service.Books with @(UI.FieldGroup #General: {
    $Type: 'UI.FieldGroupType',
    Data : [
        {
            $Type: 'UI.DataField',
            Value: descr,
        },
        {
            $Type: 'UI.DataField',
            Value: status_code,
        },
    ],
});

annotate service.Books with @(UI.FieldGroup #Details: {
    $Type: 'UI.FieldGroupType',
    Data : [
        {
            $Type: 'UI.DataField',
            Value: stock,
        },
        {
            $Type: 'UI.DataField',
            Value: price,
        },
        {
            $Type: 'UI.DataField',
            Value: rating,
        },
    ],
});

annotate service.Books with @(UI.FieldGroup #Administrative: {
    $Type: 'UI.FieldGroupType',
    Data : [
        {
            $Type: 'UI.DataField',
            Value: createdAt,
            Label: 'createdAt',
        },
        {
            $Type: 'UI.DataField',
            Value: createdBy,
            Label: 'createdBy',
        },
        {
            $Type: 'UI.DataField',
            Value: modifiedAt,
            Label: 'modifiedAt',
        },
        {
            $Type: 'UI.DataField',
            Value: modifiedBy,
            Label: 'modifiedBy',
        },
    ],
});
