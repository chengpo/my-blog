angular.module('postList')
       .component('postList', {
           templateUrl: 'static/js/post-list/post-list.template.html',
           controller: ['$http',
                function PostListController($http) {
                    var self = this;
                    $http.get('rest/posts').then(function(response) {
                        self.posts = response.data;
                    });
                }]
       });