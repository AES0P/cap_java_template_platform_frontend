using {com.win.template as temp} from '../index';

extend temp.Books with {
    isbn : temp.ISBN;
}

// input validation
annotate temp.Books with {
    isbn @mandatory;
}
