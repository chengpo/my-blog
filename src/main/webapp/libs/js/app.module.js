define(['angular',
        'sidebar/sidebar.component',
        'page-detail/page-detail.component',
        'post-list/post-list.component',
        'post-detail/post-detail.component',
        'angular-animate',
        'ng-progress'], function() {
    'use strict';

    angular.module('blogApp', [
            'ngAnimate',
            'ngProgress',
            'sidebar',
            'pageDetail',
            'postList',
            'postDetail'])
        .run(['$animate', function($animate) {
            $animate.enabled(true);
          }])
        .run(['$rootScope', '$location', 'ngProgressFactory',
             function ($rootScope, $location, ngProgressFactory) {

             var sendPageViewEvent = function() {
                var path = $location.path()
                var segments = path.split('/').filter(Boolean)
                var title = segments.length > 0 ? segments[segments.length - 1] : "/"
                title = title.replace(/[-_]/g, ' ').replace(/\b\w/g, (char) => char.toUpperCase());

                // update location for google analytics events
                gtag('event', 'page_view', {
                    'page_path': path, 
                    'page_title': title});
             }

            // first create instance when app starts
            $rootScope.progressbar = ngProgressFactory.createInstance();
            $rootScope.progressbar.setColor('#575757');

            $rootScope.$on("$routeChangeStart", function () {
                $rootScope.progressbar.start();
            });

            $rootScope.$on("$routeChangeSuccess", function () {
                $rootScope.progressbar.complete();
                sendPageViewEvent();
            });

            sendPageViewEvent();
        }]);

    return angular.module('blogApp');
});
