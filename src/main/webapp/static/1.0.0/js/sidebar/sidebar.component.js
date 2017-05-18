angular.module('sidebar')
       .component('sidebar', {
            templateUrl: Resource.versioningUrl('js/sidebar/sidebar.template.html'),
            controller: function() {
                var self = this;

                self.title = 'Monkey Blogger';
                self.links = [{url: '/#!about/', name:'About'},
                              {url: '/', name:'Projects'}];
            }
       });