angular.
  module('blogApp').
  filter('trustAsHtml', ['$sce', function($sce) {
    return function(html) {
        return $sce.trustAsHtml(html);
      };
  }]);