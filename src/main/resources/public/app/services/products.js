angular.module('shoppingCart.site').factory("Products", ['$resource', function($resource) {
    return $resource('api/products');
}]);