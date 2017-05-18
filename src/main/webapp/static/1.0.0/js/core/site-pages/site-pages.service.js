'use strict';

angular.
  module('core.sitePages').
  factory('sitePages', ['$resource',
    function($resource) {
      return $resource("rest/site-pages/:name", {}, {
        about: {
          method: 'GET',
          params: {name:'2017-0509-0011-site-about-myself.md'},
          isArray: false
        }
      });
    }
  ]);