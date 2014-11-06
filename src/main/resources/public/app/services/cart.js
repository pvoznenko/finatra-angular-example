angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/v3/cart/products/:productId', {
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
            url: 'api/v3/cart/products/:productId/quantity/:quantity'
        }
    });
}]);