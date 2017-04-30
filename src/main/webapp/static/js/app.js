var blogApp = angular.module('blogApp', []);

blogApp.controller('PostListController', function PostListController($scope) {
    $scope.posts = [
        {title : "Hello World"}, {title:"Hello World, again"}
    ];
});