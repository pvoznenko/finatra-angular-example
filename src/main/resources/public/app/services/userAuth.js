angular.module('shoppingCart.site').factory("UserAuth", ['$resource', function($resource) {
    return $resource('api/users/authentication', null, {
        getAuthToken: {
            method: 'GET'
        }
    });
}]);