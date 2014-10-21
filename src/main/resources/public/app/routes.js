window.app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'app/template/product.html',
        controller: 'product'
    })
    .when('/cart', {
        templateUrl: 'app/template/cart.html',
        controller: 'cart'
    })
    .otherwise({
        redirectTo: '/'
    });
}]);