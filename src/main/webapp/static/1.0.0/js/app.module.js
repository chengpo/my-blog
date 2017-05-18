'use strict';

angular.module('blogApp', [
            'ngAnimate',
            'core',
            'sidebar',
            'about',
            'postList',
            'postDetail'])
        .run(['$animate', function($animate) {
            $animate.enabled(true);
          }]);
