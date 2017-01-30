(function () {
    'use strict';

    angular.module('bdd-reporting').controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$http', '$state'];

    function DashboardController($scope, $http, $state) {

        var getStats = function() {
            var url = "/api/dashboard/1.0/";
            $http.get(url)
                .then(function(response) {
                    if (response) {
                        $scope.stats = response.data;
                        initChart(response.data);
                    }
                })
        };
        getStats();

        var initChart = function(data) {
            $scope.statsChart = {};
            $scope.statsChart.type = "PieChart";
            $scope.statsChart.data = {
                "cols": [
                    {id: "s", label: "Scenarios", type: "string"},
                    {id: "t", label: "BDDState", type: "number"}
                ],
                "rows": [
                    {c: [
                        {v: "Failed"},
                        {v: data.failedScenarios }
                    ]},
                    {c: [
                        {v: "Passed"},
                        {v: data.passedScenarios }
                    ]},
                    {c: [
                        {v: "Pending"},
                        {v: data.pendingScenarios }
                    ]}
                ]};
            $scope.statsChart.options = {
                // 'is3D': true,
                pieHole: 0.4,
                legend: 'none',
                slices: {
                    0: { color: '#B94A4B' }, //failed
                    1: { color: '#468847' }, //passed
                    2: { color: '#c09853' } // pending
                }
            };
        };


    }
})();