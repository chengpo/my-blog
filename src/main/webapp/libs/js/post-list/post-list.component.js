/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

define(['static-url',
        'post-list/post-list.module',
        'highlight'],
        function(staticUrl, postListModule, highlight) {

    'use strict';

    postListModule.component('postList', {
                                templateUrl: staticUrl.of('js/post-list/post-list.template.html'),
                                controller: ['$scope', '$location', 'posts',
                                     function PostListController($scope, $location, posts) {
                                         var self = this;
                                        
                                         self.error = "";
                                         self.page_size = 5;

                                         self.tag = $location.search().tag || '';
                                         self.offset = $location.search().offset || 0;
                                        
                                         self.disable_forward = true;
                                         self.disable_backward = true;

                                         function updatePageState() {
                                            self.tag = $location.search().tag || '';
                                            self.offset = $location.search().offset || 0;
                                            self.disable_forward = self.offset == 0;
                                            
                                            var totalPost = self.posts.filter(post => self.tagFilter(post)).length
                                            self.disable_backward = totalPost <= self.offset + self.page_size;
                                         }

                                         $scope.$on('$routeUpdate', function() {
                                           updatePageState(); 
                                         });

                                         
                                         self.tagFilter = function(post) {
                                            if (self.tag == '') return true;
                                            return self.tag == post.metadata.tag;
                                         }

                                         self.selectTag = function(tag) {
                                            $location.search({
                                                'tag': tag,
                                                'offset': 0
                                            });
                                         }

                                         self.forward = function() {
                                             var offset = self.offset - self.page_size;
                                             if (offset < 0) {
                                                offset = 0;
                                             }

                                             $location.search('offset', offset);
                                         }

                                         self.backward = function() {
                                             var offset = self.offset + self.page_size;
                                             $location.search('offset', offset);
                                         }

                                         posts.all({}, function(postChunk) {
                                             self.posts = postChunk.posts;
                                            
                                             updatePageState();
                                             
                                             // reload syntax highlighter
                                             setTimeout(function () {
                                                             $('pre code').each(function(i, block) {
                                                                 highlight.highlightBlock(block);
                                                             });
                                                           }, 100);

                                         }, function(error) {
                                             self.error = "Failed to retrieve post list, status: " + error.status + "!";
                                         });
                                     }]
                            });
});
