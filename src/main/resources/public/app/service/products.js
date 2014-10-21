angular.module('shoppingCart.site').factory('Products', function () {
    var products = [
        {"name": "Mac Book Retina 15'", "id": 1},
        {"name": "Nexus 6", "id": 2},
        {"name": "Surface", "id": 3}
    ];

    var methods = {
         getProducts: function () {
             return products;
         }
     };

    return methods;
});