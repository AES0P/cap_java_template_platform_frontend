using {com.win.template as temp} from '../db/index';

@path: 'browser'
service BrowserService {

  @readonly
  entity Books   as
    projection on temp.Books
    excluding {
      createdBy,
      modifiedBy
    };

  @readonly
  entity Reviews as projection on temp.Reviews;

}
