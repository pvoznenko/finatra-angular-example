angular.module('shoppingCart.site').factory("Cart", ['$resource', function(resource) {
    return resource('api/server/:name', {
        name: '@name'
    }, {
        save: {
            url: 'api/server/',
            method: 'POST'
        },
        update: {
            method: 'PUT'
        },
        migrations: {
            url: 'api/server/:name/migrations',
            method: 'PUT'
        },
        translations: {
            url: 'api/server/translations',
            method: 'POST'
        },
        getConfig: {
            url: 'api/server/:name/config',
            method: 'GET'
        },
        updateConfig: {
            url: 'api/server/:name/config',
            method: 'PUT'
        },
        getPhpLog: {
            url: 'api/server/:name/log/php',
            method: 'GET'
        }
    });
}]);