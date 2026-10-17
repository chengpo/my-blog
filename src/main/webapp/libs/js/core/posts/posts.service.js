define(['core/posts/posts.module', 'static-url'], function(postsModule, staticUrl) {
    'use strict';

    postsModule.factory('posts', ['$resource', 
        function($resource) {
          return $resource(staticUrl.appendVersion("rest/posts/"), {}, {
            all: {
              method: 'GET',
              isArray: false,
              timeout: 60*1000
            }
          });
        }
      ])
      .factory('postContent', ['$resource', 
        function($resource) {
            return $resource(staticUrl.appendVersion("rest/posts/:year/:monthday/:title/"), {}, {timeout: 60*1000});
        }
      ]);
});
