(function() {

    String.prototype.contains = function(it) { return this.indexOf(it) != -1; };
    'use strict';

    var app = angular.module('bdd-reporting', [
        // Angular modules
        //'ngRoute',
        'ngSanitize',

        // 3rd Party Modules
        'ui.bootstrap',
        'ui.router',
        'ui.grid',
        'ui.grid.edit',
        'ui.grid.selection',
        'ui.select',
        'ngAnimate',
        'googlechart'

        //'ngStomp'
    ]);

    app.config(function($stateProvider, $urlRouterProvider, $httpProvider) {

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

        $urlRouterProvider.otherwise('/dashboard');

        $stateProvider

        // HOME STATES AND NESTED VIEWS ========================================
            .state('dashboard', {
                url: '/dashboard',
                templateUrl: 'app/dashboard/dashboard.html',
                controller: 'DashboardController'
            })
            .state('features', {
                url: '/features',
                templateUrl: 'app/features/features.html',
                controller: 'FeaturesController'
            })
            .state('feature', {
                url: '/feature/:featureId',
                templateUrl: 'app/feature/feature.html',
                controller: 'FeatureController',
                params: {
                    featureId: null
                }
            })
        ;
    });

    app.run(['$state', function ($state) {
        // Include $route to kick start the router.
    }]);

})();