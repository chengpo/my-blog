angular.module('about')
       .component('about', {
            templateUrl: 'static/js/about/about.template.html',
                       controller: ['postDetail',
                            function AboutController(postDetail) {
                            }]
       });
