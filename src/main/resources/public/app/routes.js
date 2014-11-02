window.app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'app/templates/products.html',
        controller: 'products'
    })
    .when('/cart', {
        templateUrl: 'app/templates/cart.html',
        controller: 'cart'
    })
    .otherwise({
        redirectTo: '/'
    });
}]);