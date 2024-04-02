sap.ui.define([
    "sap/m/MessageToast"
], function (MessageToast) {
    'use strict';

    return {
        import: function (oEvent) {
            MessageToast.show("Custom handler invoked.");
        }
    };
});
