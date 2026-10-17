define(['static-url',
    'post-list/post-list.module',
    'highlight'],
    function (staticUrl, postListModule, highlight) {

        'use strict';

        postListModule.component('postList', {
            templateUrl: staticUrl.of('js/post-list/post-list.template.html'),
            controller: ['$scope', '$location', 'posts',
                function PostListController($scope, $location, posts) {
                    var self = this;

                    self.error = null;
                    self.page_size = 5;

                    self.disable_forward = true;
                    self.disable_backward = true;

                    function updatePageState() {
                        self.tag = $location.search().tag || '';
                        self.offset = parseInt($location.search().offset, 10) || 0;
                        self.disable_forward = self.offset == 0;

                        var totalPost = self.posts.filter(post => self.tagFilter(post)).length
                        self.disable_backward = totalPost <= self.offset + self.page_size;
                    }

                    $scope.$on('$routeUpdate', function () {
                        updatePageState();
                    });


                    self.tagFilter = function (post) {
                        if (self.tag == '') return true;
                        return self.tag == post.metadata.tag;
                    }

                    self.selectTag = function (tag) {
                        $location.search({
                            'tag': tag,
                            'offset': 0
                        });
                    }

                    self.forward = function () {
                        var offset = self.offset - self.page_size;
                        if (offset < 0) {
                            offset = 0;
                        }

                        $location.search('offset', offset);
                    }

                    self.backward = function () {
                        var offset = self.offset + self.page_size;
                        $location.search('offset', offset);
                    }

                    self.inProgress = true;

                    posts.all({}, function (postChunk) {
                        self.inProgress = false;

                        self.posts = postChunk.posts;

                        updatePageState();

                        // reload syntax highlighter
                        setTimeout(function () {
                            $('pre code').each(function (i, block) {
                                highlight.highlightBlock(block);
                            });
                        }, 100);

                    },
                        function (error) {
                            self.inProgress = false;
                            
                            self.error = {
                                title: "Failed to retrieve post list!",
                                status: error.status,
                                statusText: error.statusText,
                                data: error.data
                            };
                        });

                }]
        });
    });
