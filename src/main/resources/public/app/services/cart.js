angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/cart/products/:productId', {
        productId: '@productId'
    }, {
        add: {
            method: 'PUT'
        },
        save: {
            method: 'PUT',
            params: {
                quantity: '@quantity'
            },
            url: 'api/cart/products/:productId/quantity/:quantity'
        }
    });
}]);