angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/cart/products', {
        token: '@token',
        productId: '@productId'
    }, {
        add: {
            method: 'PUT',
            url: 'api/cart/products/:productId'
        }
    });
}]);