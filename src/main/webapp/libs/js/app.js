define(['loader-progress',
    'angular',
    'bootstrap',
    'app.config'],
    function (loaderProgress) {
        'use strict';

        var _start = function () {
            loaderProgress.hide();

            angular.element(function () {
                angular.bootstrap(document, ['blogApp']);
            });
        };

        return {
            start: _start
        };
    });