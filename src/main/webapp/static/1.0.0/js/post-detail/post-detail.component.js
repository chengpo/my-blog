'use strict';

angular.module('postDetail')
       .component('postDetail', {
            templateUrl: Resource.versioningUrl('js/post-detail/post-detail.template.html'),
            controller: ['$routeParams', 'postContent',
                function postDetailController($routeParams, postContent) {
                    var self = this;
                    self.error = "";

                    postContent.get({name:$routeParams.name},
                                     function(detail) {
                                        self.entity = detail.entity;
                                        self.content = detail.content;
                                    }, function(error) {
                                        self.error = "Failed to retrieve post content, status: " + error.status + "!";
                                    });
                }]
       });