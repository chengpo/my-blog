define(['angular', 
    'core/core.module', 
    'error-msg/error-msg.component',
    'loading-progress/loading-progress.component'], function() {
    'use strict';

    angular.module('pageDetail', [
        'core', 
        'errorMsg',
        'loadingProgress',
        'ngRoute']);

    return angular.module('pageDetail');
});
