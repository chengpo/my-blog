'use strict';

angular.module('blogApp', [
            'ngAnimate',
            'ngProgress',
            'core',
            'sidebar',
            'about',
            'postList',
            'postDetail'])
        .run(['$animate', function($animate) {
            $animate.enabled(true);
          }])
        .run(function ($rootScope, ngProgressFactory) {
            // first create instance when app starts
            $rootScope.progressbar = ngProgressFactory.createInstance();
            $rootScope.progressbar.setColor('#808080');

            $rootScope.$on("$routeChangeStart", function () {
                $rootScope.progressbar.start();
            });

            $rootScope.$on("$routeChangeSuccess", function () {
                $rootScope.progressbar.complete();
            });
        });