define([
    'angular',
    'core/posts/posts.service',
    'core/pages/pages.service',
    'core/trust-as-html/trust-as-html.filter'], function () {
        'use strict';

        angular.module('core', ['core.posts', 'core.pages', 'core.trustAsHtml']);
        return angular.module('core');
    });
