angular.module('shoppingCart.site').factory("UserAuth", ['$resource', function($resource) {
    return $resource('api/user/authentication', null, {
        getAuthToken: {
            method: 'GET'
        }
    });
}]);