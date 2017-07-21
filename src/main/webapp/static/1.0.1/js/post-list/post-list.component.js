'use strict';

angular.module('postList')
       .component('postList', {
           templateUrl: Resource.versioningUrl('js/post-list/post-list.template.html'),
           controller: ['$location', 'posts',
                function PostListController($location, posts) {
                    var self = this;

                    self.disable_forward = true;
                    self.disable_backward = true;
                    self.error = "";

                    self.tag = $location.search().tag;
                    self.offset = $location.search().offset;

                    self.forward = function() {
                        var offset = self.offset - self.posts.length;
                        $location.search('offset', offset > 0 ? offset : null);
                    }

                    self.backward = function() {
                        $location.search('offset', (self.offset + self.posts.length));
                    }

                    posts.all({tag:self.tag, offset:self.offset}, function(postChunk) {
                        self.posts = postChunk.posts;
                        self.offset = postChunk.offset;
                        self.eof = postChunk.eof;

                        self.disable_forward = self.offset <= 0;
                        self.disable_backward = self.eof;
                    }, function(error) {
                        self.error = "Failed to retrieve post list, status: " + error.status + "!";
                    });
                }]
       });