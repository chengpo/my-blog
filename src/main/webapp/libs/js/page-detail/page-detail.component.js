define(['static-url',
    'page-detail/page-detail.module'],
    function (staticUrl, pageDetailModule) {
        'use strict';

        pageDetailModule.component('pageDetail', {
            templateUrl: staticUrl.of('js/page-detail/page-detail.template.html'),
            controller: ['$routeParams', 'pageContent',
                function PageDetailController($routeParams, pageContent) {
                    var self = this;

                    self.error = null;
                    self.inProgress = true;

                    pageContent.get(
                        {
                            title: $routeParams.title
                        },

                        function (page) {
                            self.inProgress = false;

                            self.metadata = page.metadata;
                            self.content = page.content;
                        },

                        function (error) {
                            self.inProgress = false;

                            self.error = {title: "Failed to retrieve page content!",
                                          status: error.status,
                                          statusText: error.statusText,
                                          data: error.data };
                        });
                }]
        });
    });
