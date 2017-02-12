(function () {
    'use strict';

    angular.module('bdd-reporting').controller('FeatureController', FeatureController);

    FeatureController.$inject = ['$scope', '$http', '$stateParams'];

    function FeatureController($scope, $http, $stateParams) {

        $scope.scenarios = [];
        $scope.featureOverview = {};
        $scope.statsOverview = {};
        $scope.search = { name: null };
        $scope.filteredScenarios = [];
        $scope.availableTags = [];
        $scope.selections = {
            selectedLabels: [],
            selectedTags: []
        };

        $scope.$watch('filteredScenarios', function(newVal) {
            initStatsChart(calculateScenarioStats(newVal));
        });
        
        $scope.nameFilter = function(item) {

            if ($scope.search.name === null || $scope.search.name === "") {
                return true;
            }
            var query = $scope.search.name.toLowerCase();
            if (null !== item.name && item.name.toLowerCase().contains(query)) {
                return true;
            }
            return false;

        };

        $scope.labelFilter = function(item) {
            return isOption(item.labels, $scope.selections.selectedLabels)
        };

        var isOption = function(itemTags, selectedTags) {

            if (selectedTags === undefined || selectedTags.length <= 0) {
                return true;
            }
            if (itemTags === null || itemTags.length <= 0) {
                return false;
            }
            for (var index in selectedTags) {
                var tag = selectedTags[index];
                for (var itemLblIndex in itemTags) {
                    var lbl = itemTags[itemLblIndex].name;
                    if (lbl !== "") {
                        if (lbl !== tag) {
                            return false
                        }
                    }
                }

            }
            return true;

        };



        $scope.tagFilter = function(item) {
            return isOption(item.tags, $scope.selections.selectedTags)
        };



        var getFeatureInfo = function(callback) {
            $http.get("/api/feature/1.0/" +$stateParams.featureId)
                .then(function(response) {
                    if (response) {
                        callback(response.data);
                    }
                })
        };

        var getFeatureHistory = function(callback) {
            $http.get("/api/featurehistory/1.0?id=" +$stateParams.featureId)
                .then(function(response) {
                    if (response) {
                        callback(response.data);
                    }
                })
        };

        getFeatureHistory(function(data) {
           $scope.featureHistory = data;
           initScenariosHistory(data);
        });
        

        var updateScenarios = function(data) {
            $scope.scenarios = data;
            initStatsChart(calculateScenarioStats(data));
            updateAvailableTags(data);
            // updateAvailableLabels(data);
        };

        getFeatureInfo(function(data) {
            var timestamp = new Date(data.timestamp);
            data.timestamp = timestamp.toLocaleDateString() + " " + timestamp.toLocaleTimeString();
            $scope.featureInfo = data;
            updateScenarios(data.scenarios)
        });

        function toJSONLocal (date) {
            var local = new Date(date);
            local.setMinutes(date.getMinutes() - date.getTimezoneOffset());
            return local.toJSON().slice(0, 10);
        }


        var updateAvailableTags = function(data) {
            var newTags = {};
            for (var index in data) {
                var scenario = data[index];
                if (scenario.tags !== undefined) {
                    for (var tagIndex in scenario.tags) {
                        var tag = scenario.tags[tagIndex];
                        if (tag !== undefined && tag.name !== "") {
                            var count = newTags[tag.name];
                            if (count !== undefined) {
                                newTags[tag.name] = 1;
                            } else {
                                newTags[tag.name] = newTags[tag.name]+1;
                            }
                        }
                    }
                }
            }
            $scope.availableTags = Object.keys(newTags);
            $scope.tagSet = newTags;
        };

        $scope.calculateOverallState = function(scenario) {
            var state = 0;
            for (var stepIndex in  scenario.steps) {
                var step = scenario.steps[stepIndex];
                if ((step.result == null || step.result === "pending") && state < 1) {
                    state = 1;
                } else if (step.result === "failed" && state < 2) {
                    state = 2;
                }
            }
            if (state === 0) {
                return "passed";
            } else if (state === 1) {
                return "pending";
            } else if (state === 2) {
                return "failed";
            } else {
                return "ignored"
            }
        };


        var calculateScenarioStats = function(data) {
            var statsOverview = {
                "passed": 0,
                "failed": 0,
                "pending": 0,
                "total": 0
            };
            for (var index in data) {
                var scenario = data[index];
                var state = 0;
                for (var stepIndex in  scenario.steps) {
                    var step = scenario.steps[stepIndex];
                    if ((step.result == null || step.result === "pending") && state < 1) {
                        state = 1;
                    } else if (step.result === "failed" && state < 2) {
                        state = 2;
                    }
                }
                if (state === 0) {
                    statsOverview.passed++;
                } else if (state === 1) {
                    statsOverview.pending++;
                } else if (state === 2) {
                    statsOverview.failed++;
                }
                statsOverview.total++;
            }
            $scope.statsOverview = statsOverview;
            return $scope.statsOverview;
        };

        var initStatsChart = function(data) {
            $scope.chart = {};
            $scope.chart.type = "PieChart";
            $scope.chart.data = {
                "cols": [
                    {id: "s", label: "Scenarios", type: "string"},
                    {id: "t", label: "BDDState", type: "number"}
                ],
                "rows": [
                    {c: [
                        {v: "Failed"},
                        {v: data.failed }
                    ]},
                    {c: [
                        {v: "Passed"},
                        {v: data.passed }
                    ]},
                    {c: [
                        {v: "Pending"},
                        {v: data.pending }
                    ]}
                ]};
            $scope.chart.options = {
                'title': 'Breakdown',
                // 'is3D': true,
                pieHole: 0.4,
                slices: {
                    0: { color: '#B94A4B' }, //failed
                    1: { color: '#468847' }, //passed
                    2: { color: '#c09853' } // pending
                }
            };
        };

        var rowTemplate = '<div><a ng-dblclick=\"grid.appScope.onDblClick(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ \'ui-grid-row-header-cell\': col.isRowHeader }\" ui-grid-cell ></div>'

        var tableHeaders = [
            { field: 'name', displayName: 'Scenario', type: 'string', width: 400, enableFiltering: true },
            { field: 'tags', displayName: 'Tags', type: 'string', enableFiltering: true },
            { field: 'stepsTotal', displayName: "Total", type: 'int', enableFiltering: false },
            { field: 'stepsPassed', displayName: "Passed", type: 'int', enableFiltering: false },
            { field: 'stepsFailed', displayName: "Failed", type: 'int', enableFiltering: false },
            { field: 'duration', displayName: "Duration", type: 'int', enableFiltering: false },
            { field: 'overallStatus', displayName: "Status", type: 'string' }
        ];


        $scope.features = {
            enableFiltering: true,
            enableCellEdit: false,
            enableSorting: true,
            columnDefs: tableHeaders,
            enableRowSelection: true,
            enableSelectAll: false,
            noUnselect: true,
            enableRowHeaderSelection: false,
            multiSelect: false,
            rowTemplate: rowTemplate,
            appScopeProvider: {
                onDblClick : function(row) {
                    viewFeature(row.entity)
                }
            }
        };

        var viewFeature = function() {

        };

        $scope.features.data = [];

        var calculateRows = function(data) {

            var result = [];

            data.forEach (function(item, index) {
                result.push(
                    {
                        c: [
                            { v: new Date(item.timestamp) },
                            { v: item.passedScenarios },
                            { v: item.pendingScenarios },
                            { v: item.failedScenarios }
                        ]
                    }

                );
            });
            return result;


        };


        var initScenariosHistory = function(data) {
            $scope.scenariosHistoryChart = {};
            $scope.scenariosHistoryChart.type = "AreaChart";
            var rows = calculateRows(data);
            $scope.scenariosHistoryChart.data = {
                "cols": [
                    {id: "time", label: "Time", "type": 'datetime'},
                    {id: "passed", label: "Pass", type: "number", p: {}},
                    {id: "pending", label: "Pending", type: "number", p: {}},
                    {id: "failed", label: "Fail", type: "number", p: {}}
                ],
                "rows": rows };
            $scope.scenariosHistoryChart.options = {
                'title': 'Run History',
                'isStacked': true,
                'colors': ['#468847','#c09853', '#B94A4B'],
                // 'vAxis': {format: '0'}
            };
        };
        //
        // var getFeatureHistory = function(callback) {
        //     $http.get("/api/1.0/feature/history/" + $stateParams.featureId)
        //         .then(function(response) {
        //             if (response) {
        //                 callback(response.data);
        //             }
        //         });
        //
        // };
        //
        // getFeatureHistory(function(data) {
        //     initScenariosHistory(data)
        // });



    }
})();