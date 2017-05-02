angular.module('postDetail')
       .component('postDetail', {
            templateUrl: "static/js/post-detail/post-detail.template.html",
            controller: ['$routeParams', 'postDetail',
                function postDetailController($routeParams, postDetail) {
                    var self = this;
                    postDetail.get({year:$routeParams.year,
                                     day:$routeParams.day,
                                     time:$routeParams.time,
                                     tag:$routeParams.tag,
                                     title:$routeParams.title},

                                     function(detail) {
                                        self.head = detail.head;
                                        self.content = detail.content;
                                    });
                }
            ]
       });