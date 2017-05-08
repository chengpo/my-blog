angular.
  module('core.posts').
  factory('posts', ['$resource',
    function($resource) {
      return $resource("rest/posts/?tag=:tag&offset=:offset", {}, {
        all: {
          method: 'GET',
          params: {tag:'', offset:'0'},
          isArray: false
        }
      });
    }
  ]).
  factory('postDetail', ['$resource',
    function($resource) {
        return $resource("rest/posts/:name", {}, {});
    }
  ]);