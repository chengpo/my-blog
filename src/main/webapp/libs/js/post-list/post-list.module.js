define(['angular', 
    'core/core.module', 
    'error-msg/error-msg.component', 
    'loading-progress/loading-progress.component'], function() {
    'use strict';

    angular.module('postList', ['core', 'errorMsg', 'loadingProgress']);

    return angular.module('postList');
});
