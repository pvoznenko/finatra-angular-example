angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/cart/products/:productId', {
        token: '@token',
        productId: '@productId',
        quantity: '@quantity'
    }, {
        add: {
            method: 'PUT'
        },
        save: {
            method: 'PUT',
            url: 'api/cart/products/:productId/quantity/:quantity'
        }
    });
}]);