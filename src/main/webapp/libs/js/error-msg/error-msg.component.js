define(['error-msg/error-msg.module'],
    function(errorMsgModule) {
        'use strict';

        errorMsgModule.component('errorMsg', {
            bindings: {
                errorData: '<'
            },

            controller: function() { 
                var self = this;
                self.$onChanges = function(changesObj) {
                    if (changesObj.errorData && changesObj.errorData.currentValue) {
                        var error = changesObj.errorData.currentValue;
                        if (error.status === -1) {
                            error.statusText = "Request times out";
                        }

                        var detail = error.data;
                        if (detail === undefined || detail === null) {
                            detail = "";
                        }

                        self.formattedError = "<hr><p>" + error.title + "</p>" +
                            "<div>" +
                            ("Status:<br>   " + error.status).replace(/ /g, "&nbsp;") + "<br><br>" +
                            ("Status Text:<br>    " + error.statusText).replace(/ /g, "&nbsp;") + "<br>" +
                            detail.replace(/\n/g, "<br>").replace(/ /g, "&nbsp;") +
                            "</div>";
                    } else {
                        self.formattedError = null;
                    }
               }                
            },

            template: `
        <!-- show error message if needed -->
        <div class="row" ng-if="$ctrl.formattedError">
            <div class="col-md-10 col-md-offset-1 alert alert-warning fade in ">
                <strong style="padding-right:0.5em" >Oops!</strong> 
                <div ng-bind-html="$ctrl.formattedError | trustAsHtml"></div>
            </div>
        </div>
            `
        });
});
