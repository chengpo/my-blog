<!--
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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
-->

<%@page language="java" import="com.monkeyapp.blog.StaticUrl" contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/">

    <meta charset="UTF-8">
    <meta name="theme-color" content="#202020">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <title>Coding Is Fun</title>

    <style>
    /* center the loader */
    #loader {
      position: absolute;
      left: 50%;
      top: 50%;
      z-index: 1;
      margin: -75px 0 0 -75px;
      border: 12px solid #f3f3f3;
      border-radius: 50%;
      border-top: 12px solid #3498db;
      width: 120px;
      height: 120px;
      -webkit-animation: spin 2s linear infinite;
      animation: spin 2s linear infinite;
    }

    @-webkit-keyframes spin {
      0% { -webkit-transform: rotate(0deg); }
      100% { -webkit-transform: rotate(360deg); }
    }

    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
    </style>
</head>

<body>
    <div id="loader"></div>

    <sidebar></sidebar>

    <div class="content">
        <div ng-view class="view-frame"></div>
    </div>

    <link rel="stylesheet" href="external/bootstrap/dist/css/bootstrap.css">
    <link rel="stylesheet" href="external/ngprogress/ngProgress.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Play">

    <script src="external/jquery/dist/jquery.js"></script>
    <script src="external/bootstrap/dist/js/bootstrap.js"></script>
    <script src="external/angular/angular.js"></script>
    <script src="external/angular-route/angular-route.js"></script>
    <script src="external/angular-resource/angular-resource.js"></script>
    <script src="external/angular-animate/angular-animate.js"></script>
    <script src="external/ngprogress/build/ngprogress.js"></script>

    <!-- syntax highlighter -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.12.0/styles/androidstudio.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/9.12.0/highlight.min.js"></script>

    <link rel="stylesheet" href="<%= StaticUrl.of("css/app.css") %>">
    <link rel="stylesheet" href="<%= StaticUrl.of("css/app.animate.css") %>">

    <script src="external/requirejs/require.js"></script>
    <script type='text/javascript'>
        define('loader-progress', function() {
            'use strict';
            var _hide = function() {
                 $('#loader').hide();
            }

            return {
                hide : _hide
            };
        });

        define('static-url', function() {
            'use strict';

            var _of = function(path) {
                 return 'static/<%= StaticUrl.VERSION %>/' + path;
            };

            return {
                of : _of
                };
        });

        requirejs.config({
                        baseUrl: '<%= StaticUrl.of("js") %>'
                    });

        require(['loader-progress',
                 'app.config'],
                 function(loaderProgress, appConfig) {

               loaderProgress.hide();

               angular.element(function() {
                    angular.bootstrap(document, ['blogApp']);
               });
        });
    </script>

</body>

</html>