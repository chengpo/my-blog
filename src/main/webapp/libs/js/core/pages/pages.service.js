define(['core/pages/pages.module', 'static-url'], function (pagesModule, staticUrl) {
  'use strict';

  pagesModule.factory('pageContent', ['$resource',
    function ($resource) {
      return $resource(staticUrl.appendVersion("rest/pages/:title/"), {}, { timeout: 60*1000 });
    }
  ]);
});
