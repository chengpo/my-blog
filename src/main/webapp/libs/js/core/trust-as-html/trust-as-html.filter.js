define(['core/trust-as-html/trust-as-html.module'], function (trustAsHtmlModule) {
    'use strict';
    // Explicitly trust the html conent by using sce ( strict content escaping )
    trustAsHtmlModule.filter('trustAsHtml', ['$sce', function ($sce) {
        return function (html) {
            return $sce.trustAsHtml(html);
        };
    }]);
});
