angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/cart/products/:productId', {
        token: '@token',
        productId: '@productId'
    }, {
        add: {
            method: 'PUT'
        }
    });
}]);