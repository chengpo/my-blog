<%@page language="java" import="com.monkeyapp.blog.StaticUrl" contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <base href="/">

    <meta charset="UTF-8">
    <meta name="theme-color" content="#202020">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="google-site-verification" content="aBgNRVBo5y1-FwLrwGCbmBXADuB8i5FLxUIKG-ReX1w" />

    <title><%= application.getInitParameter("site-title")%></title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    
    <!-- Global site tag (gtag.js) - Google Analytics -->
      <script async src="https://www.googletagmanager.com/gtag/js?id=G-RPDHYF0752"></script>
     <script>
         window.dataLayer = window.dataLayer || [];
         function gtag(){dataLayer.push(arguments);}
         gtag('js', new Date());
    
         gtag('config', 'G-RPDHYF0752', {send_page_view: false});
     </script>
    
    <style>
    /* center the loader */
    #loader {
      position: absolute;
      left: 53.5%;
      top: 50%;
      z-index: 1;
      margin: -75px 0 0 -75px; 
      border: 10px solid #f3f3f3;
      border-radius: 50%;
      border-top: 10px solid #575757;
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
    
    <link rel="icon" type="image/png" sizes="192x192" href="/external/favicons/android-chrome-192x192.png">
    <link rel="icon" type="image/png" sizes="512x512" href="/external/favicons/android-chrome-512x512.png">
    <link rel="apple-touch-icon" sizes="180x180" href="/external/favicons/apple-touch-icon.png">
    <link rel="icon" type="image/png" sizes="16x16" href="/external/favicons/favicon-16x16.png">
    <link rel="icon" type="image/png" sizes="32x32" href="/external/favicons/favicon-32x32.png">
    <link rel="icon" type="image/x-icon" href="/external/favicons/favicon.ico">
    <link rel="manifest" href="/external/favicons/site.webmanifest">
</head>

<body>
    <div id="loader"></div>

    <sidebar></sidebar>

    <div class="content">
        <div ng-view class="view-frame"></div>
    </div>

    <link rel="stylesheet" href="/external/bootstrap/dist/css/bootstrap.css">
    <link rel="stylesheet" href="/external/ngprogress/ngProgress.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Play&display=swap">

    <!-- syntax highlighter -->
    <link rel="stylesheet" href="/external/highlight.js/9.12.0/androidstudio.min.css">

    <link rel="stylesheet" href="/<%= StaticUrl.of("css/app.css") %>">
    <link rel="stylesheet" href="/<%= StaticUrl.of("css/app.animate.css") %>">

    <script src="/external/requirejs/require.js"></script>
    <script type='text/javascript'>
        var userAgent = navigator.userAgent.toLowerCase();
        var isGooglebot = userAgent.indexOf('googlebot') !== -1;

        if (isGooglebot) {
            document.getElementById('loader').style.display = 'none';
        }

        define('loader-progress', ['jquery'], function($) {
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
                 return '/static/<%= StaticUrl.VERSION %>/' + path;
            };
            var _appendVersion = function(url) {
                return url + '?version=<%= StaticUrl.VERSION%>'
            }

            return {
                of : _of,
                appendVersion : _appendVersion
                };
        });

        requirejs.config({
                        baseUrl: '/<%= StaticUrl.of("js") %>',
                        waitSeconds : 600,
                        paths : {
                            jquery : '/external/jquery/dist/jquery',
                            bootstrap : '/external/bootstrap/dist/js/bootstrap',
                            angular : '/external/angular/angular',
                            'angular-route' : '/external/angular-route/angular-route',
                            'angular-resource' : '/external/angular-resource/angular-resource',
                            'angular-animate' : '/external/angular-animate/angular-animate',
                            'ng-progress' : '/external/ngprogress/build/ngprogress',
                            highlight : '/external/highlight.js/9.12.0/highlight.min'
                        },

                        shim: {
                            'bootstrap' : ['jquery'],
                            'highlight' : {
                                deps : ['jquery'],
                                exports: 'highlight'
                             },
                             'angular-route' : ['angular'],
                             'angular-resource' : ['angular'],
                             'angular-animate' : ['angular'],
                             'ng-progress' : ['angular']
                        }
                    });

        require(['app'], function(app) {
               app.start();
        });
    </script>
</body>

</html>
