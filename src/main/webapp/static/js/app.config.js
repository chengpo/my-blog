'use strict';

angular.
  module('blogApp').
  config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');

      $routeProvider.
        when('/posts', {
          template: '<post-list></post-list>'
        }).
        when('/posts/:name', {
          template: '<post-detail></post-detail>'
        }).
        when('/about', {
            template: '<about></about>'
        }).
        otherwise('/posts');
    }
  ]);