angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/cart', {
        token: '@token'
    }, {
        add: {method: 'PUT'}
    });
}]);