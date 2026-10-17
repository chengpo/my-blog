define(['static-url',
    'highlight',
    'post-detail/post-detail.module'],
    function (staticUrl, highlight, postDetailModule) {
        'use strict';

        postDetailModule.component('postDetail', {
            templateUrl: staticUrl.of('js/post-detail/post-detail.template.html'),
            controller: ['$routeParams', 'postContent',
                function postDetailController($routeParams, postContent) {
                    var self = this;
                    self.error = null;
                    self.inProgress = true;

                    postContent.get({
                        year: $routeParams.year,
                        monthday: $routeParams.monthday,
                        title: $routeParams.title
                    },

                    function (post) {
                        self.inProgress = false;

                        self.metadata = post.metadata;
                        self.content = post.content;

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
                            title: "Failed to retrieve post content!",
                            status: error.status,
                            statusText: error.statusText,
                            data: error.data
                        };
                    });
                }]
        });
    });
