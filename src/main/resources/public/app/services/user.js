angular.module('shoppingCart.site').factory('User', ['$http', function ($http) {
    var userToken = null;

    var methods = {
         getUserToken: function () {
             return userToken;
         },
         setUserToken: function (token) {
             userToken = token;

             $http.defaults.headers.common['token'] = userToken;

             return methods;
         }
     };

    return methods;
}]);