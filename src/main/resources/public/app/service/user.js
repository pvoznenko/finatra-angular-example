angular.module('shoppingCart.site').factory('User', function () {
    var userId = null;

    var methods = {
         getUserId: function () {
             return userId;
         },
         setUserId: function () {
             userId = new Date().getTime();

             return methods;
         }
     };

    return methods;
});