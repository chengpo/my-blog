define(['static-url',
        'sidebar/sidebar.module'],
        function(staticUrl, sidebarModule) {

    'use strict';

    sidebarModule.component('sidebar', {
                             templateUrl: staticUrl.of('js/sidebar/sidebar.template.html'),
                             controller: function() {
                                 var self = this;

                                 self.title = 'Monkey Blogger';
                                 self.links = [{url: '#!/pages/about-myself', external: false, name:'About'},
                                               {url: 'https://github.com/chengpo?tab=overview', external: true, name:'Projects'},
                                               {url: '/posts/feed/rss.xml', external:true, name:'RSS'}];

                                 self.toggle = function() {
                                         var menuToggle = $('.sidebar .menu-toggle');
                                         menuToggle.toggleClass('change');

                                         var menuList = $('.sidebar-menu');
                                         menuList.slideToggle({duration:300});
                                 }
                             }
                        });

});
