angular.module('postList')
       .component('postList', {
           templateUrl: 'static/js/post-list/post-list.template.html',
           controller: ['$location', 'posts',
                function PostListController($location, posts) {
                    var self = this;
                    var search = $location.search();

                    posts.all({tag:search.tag, offset:search.offset}, function(postChunk) {
                        self.posts = postChunk.posts;
                        self.offset = postChunk.offset;
                        self.eof = postChunk.eof;
                    });
                }]
       });