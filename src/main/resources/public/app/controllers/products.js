angular.module('shoppingCart.site').controller('products', ['$scope', 'Products', 'User', 'Cart', function ($scope, Products, User, Cart) {
    $scope['success'] = false;
    $scope['error'] = false;

    $scope['products'] = Products.query();

    $scope['addToCart'] = function(productId) {
        productId = parseInt(productId, 10);

        Cart.add({"productId": productId, "token": User.getUserToken()}, function() {
            $scope['success'] = 'Product with id "' + productId + '" successfully added to cart!';
        }, function(err) {
            $scope['error'] = err;
        });
    };
}]);