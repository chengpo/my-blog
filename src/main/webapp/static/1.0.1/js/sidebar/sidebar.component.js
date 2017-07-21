angular.module('sidebar')
       .component('sidebar', {
            templateUrl: Resource.versioningUrl('js/sidebar/sidebar.template.html'),
            controller: function() {
                var self = this;

                self.title = 'Monkey Blogger';
                self.links = [{url: '/about/', external: false, name:'About'},
                              {url: 'https://github.com/chengpo?tab=repositories', external: true, name:'Projects'}];
            }
       });