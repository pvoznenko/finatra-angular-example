angular.module('shoppingCart.site').factory("UserAuth", ['$resource', function($resource) {
    return $resource('api/v3/users/authentication', null, {
        getAuthToken: {
            method: 'POST'
        }
    });
}]);