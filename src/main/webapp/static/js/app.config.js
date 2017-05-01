angular.
  module('blogApp').
  config(['$locationProvider', '$routeProvider',
    function config($locationProvider, $routeProvider) {
      $locationProvider.hashPrefix('!');

      $routeProvider.
        when('/posts', {
          template: '<post-list></post-list>'
        }).
        when('/posts/:year/:day/:time/:tag/:title', {
          template: '<post-detail></post-detail>'
        }).
        otherwise('/posts');
    }
  ]);