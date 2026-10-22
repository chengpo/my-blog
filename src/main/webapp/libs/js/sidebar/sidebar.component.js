define(['static-url',
        'sidebar/sidebar.module'],
        function(staticUrl, sidebarModule) {

    'use strict';

    sidebarModule.component('sidebar', {
                             templateUrl: staticUrl.of('js/sidebar/sidebar.template.html'),
                             controller:['$window', '$scope', "$timeout", function($window, $scope, $timeout) {
                                 var self = this;

                                 self.title = 'Monkey Blogger';
                                 self.links = [{url: '#!/pages/about-myself', external: false, name:'About', icon: 'glyphicon-user'},
                                               {url: 'https://github.com/chengpo?tab=overview', external: true, name:'Projects', icon: 'glyphicon-folder-open'},
                                               {url: '/posts/feed/rss.xml', external:true, name:'RSS', icon: 'glyphicon-link'}];
                                
                                 var menuToggle = $('.sidebar .menu-toggle');
                                 var titleBar = $('.title-bar');
                                
                                 self.titleBarHidden = titleBar.css('display') === 'none';
                                 self.titleBarExpanded = false;

                                 var scrollListener = null;
                                 var lastScrollTop = 0;

                                 function scrollCallback() {
                                     var currentScrollTop = $(window).scrollTop();
                                     if (currentScrollTop - lastScrollTop > 10 && self.titleBarExpanded) {
                                         window.removeEventListener('scroll', scrollListener);
                                         scrollListener = null;
                                         $scope.$apply(function () {
                                             self.toggle();
                                         });
                                     } 
                                    
                                     // Reset lastScrollTop when scroll down
                                     if (currentScrollTop < lastScrollTop) {
                                        lastScrollTop = currentScrollTop;
                                     }
                                 }
                                 
                                 self.toggle = function () {
                                     // Ignore toggle even when title bar is hidden
                                     if (self.titleBarHidden) {
                                        return;
                                     }

                                     menuToggle.toggleClass('expanded');

                                     self.titleBarExpanded = menuToggle.hasClass('expanded');

                                     var sidebarContent = $('.sidebar-content');
                                     sidebarContent.slideToggle({ duration: 300 });
                                    
                                     // Wait animation finished and reset scroll listener
                                     $timeout(function () {
                                         if (self.titleBarExpanded && scrollListener == null) {
                                             lastScrollTop = $(window).scrollTop();
                                             scrollListener = scrollCallback;
                                             window.addEventListener('scroll', scrollListener);
                                         }

                                         if (!self.titleBarExpanded && scrollListener != null) {
                                             window.removeEventListener('scroll', scrollListener);
                                             scrollListener = null;
                                         }
                                     }, 400);
                                 }

                                 // Respond to window resize
                                 angular.element($window)
                                 .on('resize', function() {
                                    $scope.$apply(function(){
                                        self.titleBarHidden = titleBar.css('display') === 'none';
                                    });
                                 });
                             }]
                        });

});
