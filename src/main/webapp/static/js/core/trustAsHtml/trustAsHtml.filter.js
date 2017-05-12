'use strict';

angular.
  module('core')
  .filter('trustAsHtml', ['$sce', function($sce) {
    return function(html) {
        return $sce.trustAsHtml(html);
      };
  }]);