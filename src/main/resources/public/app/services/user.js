angular.module('shoppingCart.site').factory('User', function () {
    var userToken = null;

    var methods = {
         getUserToken: function () {
             return userToken;
         },
         setUserToken: function (token) {
             userToken = token;

             return methods;
         }
     };

    return methods;
});