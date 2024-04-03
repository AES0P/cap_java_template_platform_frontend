using {com.win.template as temp} from '../db/index';
using {sap.common.Languages as CommonLanguages} from '@sap/cds/common';

@path: 'books'
service BookService {
  entity Books     as projection on temp.Books actions {
                        action addReview(rating : temp.rating_enum, title : temp.title, descr : temp.description) returns Reviews;
                      };

  entity Reviews   as projection on temp.Reviews;

  @cds.persistence.skip
  entity Upload       @odata.singleton {
    csv : LargeBinary @Core.MediaType: 'text/csv';
  }

  entity Languages as projection on CommonLanguages;
}
