'use strict';

angular.module('postDetail')
       .component('postDetail', {
            templateUrl: Resource.versioningUrl('js/post-detail/post-detail.template.html'),
            controller: ['$routeParams', 'postContent',
                function postDetailController($routeParams, postContent) {
                    var self = this;
                    postContent.get({name:$routeParams.name},
                                     function(detail) {
                                        self.entity = detail.entity;
                                        self.content = detail.content;
                                    });
                }
            ]
       });