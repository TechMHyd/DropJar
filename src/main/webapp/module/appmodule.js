var module = angular.module('dropJar', ['ngRoute']);
module.config(['$routeProvider', function ($routeProvider) {
    $routeProvider
    .when('/', {
        templateUrl: 'partials/home.html'
        , controller: 'homeController'
    })
    .otherwise({
        redirectTo: '/'
    });
}]);