define(['app.module','angular-route'], function(appModule) {
    'use strict';

     appModule.config(['$locationProvider', '$routeProvider',
        function config($locationProvider, $routeProvider) {
          // $locationProvider.html5Mode({
          //  enabled: true,
          //  requireBase: true
          // });
          
          // fallback to hash prefix to use #!
          $locationProvider.hashPrefix('!');

          $routeProvider.
            when('/posts', {
                template: '<post-list></post-list>',
                reloadOnSearch: false
            }).
            when('/posts/:year/:monthday/:title', {
                template: '<post-detail></post-detail>'
            }).
            when('/pages/:title', {
                template: '<page-detail></page-detail>'
            }).
            otherwise('/posts');
        }
      ]);
});