angular.module('shoppingCart.site').controller('cart', ['$scope', '$location', 'User', 'Cart', function ($scope, $location, User, Cart) {
    $scope['error'] = false;
    $scope['totalPrice'] = 0;
    $scope['totalQuantity'] = 0;
    $scope['quantity'] = [];

    var token = User.getUserToken();

    if (token == null) {
        $location.path('/');
        return;
    }

    var userData = {'token': User.getUserToken()};

    var onError = function(err) {
        $scope['error'] = err;
    };

    Cart.query(userData, function(products) {
        $scope['products'] = products;

        angular.forEach($scope['products'], function(product) {
           $scope['quantity'][product.id] = product.quantity;
           $scope['totalQuantity'] += product.quantity;
        });

        $scope['updateTotal']();
    }, onError);

    $scope['removeFromCart'] = function(productId) {
        var products = [],
            data = userData;

        angular.forEach($scope['products'], function(product) {
            if (product.id == productId) {
                return true;
            }

            this.push(product);
        }, products);

        $scope['products'] = products;

        $scope['updateTotal']();

        data.productId = productId;

        Cart.remove(data, null, onError);
    };

    $scope['updateQuantity'] = function(productId) {
        productId = productId || null;

        var products = [];
        $scope['totalQuantity'] = 0;

        angular.forEach($scope['products'], function(product) {
            var value = parseInt($scope['quantity'][product.id], 10);

            if (isNaN(value)) {
                value = 0;
            }

            product.quantity = value;
            $scope['totalQuantity'] += product.quantity;

            this.push(product);

            if (product.id == productId) {
                userData.product = product;
                Cart.save(userData, null, onError);
            }
        }, products);

        $scope['products'] = products;

        $scope['updateTotal']();
    };

    $scope['updateTotal'] = function() {
        $scope['totalPrice'] = 0;

        angular.forEach($scope['products'], function(product) {
           $scope['totalPrice'] += product.price * product.quantity;
        });
    };
}]);