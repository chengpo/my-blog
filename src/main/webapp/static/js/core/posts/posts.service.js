angular.
  module('core.posts').
  factory('posts', ['$resource',
    function($resource) {
      return $resource("rest/posts/?tag=:tag", {}, {
        all: {
          method: 'GET',
          params: {tag:''},
          isArray: true
        }
      });
    }
  ]).
  factory('postDetail', ['$resource',
    function($resource) {
        return $resource("rest/posts/:year/:day/:time/:tag/:title", {}, {});
    }
  ]);