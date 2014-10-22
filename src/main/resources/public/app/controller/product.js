angular.module('shoppingCart.site').controller('product', ['$scope', 'Product', 'User', 'Cart', function ($scope, Product, User, Cart) {
    $scope['success'] = false;
    $scope['error'] = false;

    $scope['products'] = Product.query();

    $scope['addToCart'] = function(productId) {
        productId = parseInt(productId, 10);

        if (productId <= 0) {
            $scope['error'] = 'Product id should be specified!';
            return false;
        }

        Cart.add({"productId": productId, "token": User.getUserToken()}, function() {
            $scope['success'] = 'Product with id "' + productId + '" successfully added to cart!';
        }, function(err) {
            $scope['error'] = err;
        });
    };
}]);