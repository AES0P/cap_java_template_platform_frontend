using {com.win.template as temp} from '../db/index';

@path: 'browser'
service BrowserService {

  @readonly
  entity Books   as
    projection on temp.Books
    excluding {
      createdBy,
      modifiedBy
    } actions {
      action addReview(rating : temp.rating_enum, title : temp.title, descr : temp.description) returns Reviews;
    };

  @readonly
  entity Reviews as projection on temp.Reviews;

}
