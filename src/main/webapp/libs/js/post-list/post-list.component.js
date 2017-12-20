/*
MIT License

Copyright (c) 2017 Po Cheng

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
                        var offset = self.offset - self.capacity;
                        $location.search('offset', offset > 0 ? offset : null);
                    }

                    self.backward = function() {
                        $location.search('offset', (self.offset + self.posts.length));
                    }

                    posts.all({tag:self.tag, offset:self.offset}, function(postChunk) {
                        self.posts = postChunk.papers;
                        self.offset = postChunk.offset;
                        self.capacity = postChunk.capacity;
                        self.eof = postChunk.eof;

                        self.disable_forward = self.offset <= 0;
                        self.disable_backward = self.eof;

                        // reload syntax highlighter
                        setTimeout(function () {
                                        $('pre code').each(function(i, block) {
                                            hljs.highlightBlock(block);
                                        });
                                      }, 100);

                    }, function(error) {
                        self.error = "Failed to retrieve post list, status: " + error.status + "!";
                    });
                }]
       });