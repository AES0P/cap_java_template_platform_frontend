namespace com.win.template;

using {sap.common.CodeList} from '@sap/cds/common';
using {com.win.template as temp} from '../index';


type rating_enum : temp.rating enum {
    Best  = 5;
    Good  = 4;
    Avg   = 3;
    Poor  = 2;
    Worst = 1;
}

entity status : CodeList {
    key code : String(1) enum {
            soldOut   = 'O';
            onSelling = 'A';
            Outdated  = 'X';
        } default 'Outdated';
}
