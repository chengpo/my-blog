define(['static-url',
        'sidebar/sidebar.module'],
        function(staticUrl, sidebarModule) {

    'use strict';

    sidebarModule.component('sidebar', {
                             templateUrl: staticUrl.of('js/sidebar/sidebar.template.html'),
                             controller:['$window', '$scope', function($windows, $scope) {
                                 var self = this;

                                 self.title = 'Monkey Blogger';
                                 self.links = [{url: '#!/pages/about-myself', external: false, name:'About'},
                                               {url: 'https://github.com/chengpo?tab=overview', external: true, name:'Projects'},
                                               {url: '/posts/feed/rss.xml', external:true, name:'RSS'}];
                                
                                 var menuToggle = $('.sidebar .menu-toggle');
                                 var titleBar = $('.title-bar');
                                
                                 self.titleBarHidden = titleBar.css('display') === 'none';
                                 self.titleBarExpanded = false;

                                 self.toggle = function () {
                                     menuToggle.toggleClass('expanded');

                                     self.titleBarExpanded = menuToggle.hasClass('expanded');

                                     var sidebarContent = $('.sidebar-content');
                                     sidebarContent.slideToggle({ duration: 300 });
                                 }

                                 // Respond to window resize
                                 angular.element($windows).on('resize', function() {
                                    $scope.$apply(function(){
                                        self.titleBarHidden = titleBar.css('display') === 'none';
                                    });
                                 });
                             }]
                        });

});
