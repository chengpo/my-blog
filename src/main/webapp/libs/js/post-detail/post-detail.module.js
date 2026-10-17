define(['angular', 
    'core/core.module',
    'error-msg/error-msg.component',
    'loading-progress/loading-progress.component'], function() {
    'use strict';

    angular.module('postDetail', [
        'core',
        'errorMsg',
        'loadingProgress',
        'ngRoute'
    ]);

    return angular.module('postDetail');
});

