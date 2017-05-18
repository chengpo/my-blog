angular.module('about')
       .component('about', {
            templateUrl: Resource.versioningUrl('js/about/about.template.html'),
            controller: ['sitePages',
                    function AboutController(sitePages) {
                        var self = this;
                        sitePages.about(
                            function(detail) {
                                self.entity = detail.entity;
                                self.content = detail.content;
                            });
                    }]
   });
