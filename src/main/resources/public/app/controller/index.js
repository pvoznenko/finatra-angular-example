angular.module('shoppingCart.site').controller('index', ['$scope', '$location', 'User', function ($scope, $location, User) {
    var userId = User.getUserId();

    if (userId === null) {
        userId = User.setUserId().getUserId();
    }

    $scope['userId'] = userId;

    $scope['currentSite'] = $location.path();
    $scope.$on('$routeChangeSuccess', function() {
        $scope['currentSite'] = $location.path();
    });
}]);