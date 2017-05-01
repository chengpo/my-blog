angular.module('postDetail')
       .component('postDetail', {
            templateUrl: "static/js/post-detail/post-detail.template.html",
            controller: ['$routeParams', '$http',
                function postDetailController($routeParams, $http) {
                    var self = this;
                    var postUrl = 'rest/posts/'+
                                  $routeParams.year + '/' +
                                  $routeParams.day + '/' +
                                  $routeParams.time + '/' +
                                  $routeParams.tag + '/' +
                                  $routeParams.title;

                    $http.get(postUrl).then(function(response) {
                        self.head = response.data.head;
                        self.content = response.data.content;
                    });
                }
            ]
       });