angular.module('postList')
       .component('postList', {
           templateUrl: 'static/js/post-list/post-list.template.html',
           controller: ['posts',
                function PostListController(posts) {
                    this.posts = posts.all();
                }]
       });