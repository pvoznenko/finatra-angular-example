angular.module('shoppingCart.site').controller('product', ['$scope', 'Product', 'Products', 'User', function ($scope, Product, Products, User) {
    $scope['success'] = false;
    $scope['error'] = false;

    $scope['products'] = Products.getProducts();

    $scope['addToCart'] = function(productId) {
        productId = parseInt(productId, 10);

        if (productId <= 0) {
            $scope['error'] = 'Product id should be specified!';
            return false;
        }

        Product.addToCart({"productId": productId, "userId": User.getUserId()}, function() {
            $scope['success'] = 'Product with id "' + productId + '" successfully added to cart!';
        }, function(err) {
            $scope['error'] = err;
        });
    };
}]);