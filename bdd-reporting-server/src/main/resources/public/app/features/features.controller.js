(function () {
    'use strict';

    angular.module('bdd-reporting').controller('FeaturesController', FeaturesController);

    FeaturesController.$inject = ['$scope', '$http', '$state'];

    function FeaturesController($scope, $http, $state) {

        $scope.search = { name: null, label: null, tag: null };

        $scope.selections = {
            selectedLabels: [],
            selectedTags: []
        };

        $scope.features = [];
        $scope.filteredFeatures = [];
        $scope.$watch('filteredFeatures', function(newVal) {
            // alert('hey, myVar has changed!');
            // console.log(newVal.length);
            initChart(calculateOverviewStats(newVal));
        });

        $scope.getOverallStatus = function(overview) {

        };


        $scope.featureNameFilter = function(item) {

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
            return isLabelOption(item.labels, $scope.selections.selectedLabels)
        };

        var isTagOption = function(itemTags, selectedTags) {

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

        var isLabelOption = function(itemTags, selectedTags) {

            if (selectedTags === undefined || selectedTags.length <= 0) {
                return true;
            }
            if (itemTags === null || itemTags.length <= 0) {
                return false;
            }
            for (var index in selectedTags) {
                var tag = selectedTags[index];
                for (var itemLblIndex in itemTags) {
                    var lbl = itemTags[itemLblIndex];
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
            return isTagOption(item.tags, $scope.selections.selectedTags)
        };



        var getFeatures = function() {
            var url = "api/featureoverviews/1.0/?";
            if ($scope.selections.selectedLabels) {
                url += "properties=" + $scope.selections.selectedLabels
            }
            if ($scope.selections.selectedTags) {
                url += "&tags=" + $scope.selections.selectedTags
            }
            $http.get(url)
                .then(function(response) {
                    if (response) {
                        $scope.features = response.data;
                        $scope.features.data = response.data;
                        updateAvailableLabels(response.data);
                        updateAvailableTags(response.data);
                    }
                })
        };
        getFeatures();

        var calculateOverviewStats = function(data) {
            var statsOverview = {
                "passed": 0,
                "failed": 0,
                "pending": 0,
                "ignored" : 0,
                "total": 0,
                "features": 0
            };
            if (data.length > 0) {
                for (var index in data) {
                    var feature = data[index];
                    statsOverview.features = statsOverview.features + 1;
                    statsOverview.passed += feature.passedScenarios;
                    statsOverview.pending += feature.pendingScenarios;
                    statsOverview.failed += feature.failedScenarios;
                    statsOverview.ignored += feature.ignoredScenarios;
                    statsOverview.total += (feature.totalScenarios);
                }
            }
            $scope.statsOverview = statsOverview;
            return $scope.statsOverview;
        };


        $scope.go = function(featureId) {
            $state.go('feature', {featureId: featureId});
        };
        // var getAvailableLabels = function() {
        //     $http.get("/api/1.0/properties")
        //         .then(function(response) {
        //             if (response) {
        //                 $scope.availableLabels = response.data
        //             }
        //         })
        // };
        // getAvailableLabels();
        //
        var updateAvailableTags = function(data) {
            var newTags = {};
            for (var index in data) {
                var feature = data[index];
                if (feature.tags !== undefined) {
                    for (var tagIndex in feature.tags) {
                        var tag = feature.tags[tagIndex];
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

        var updateAvailableLabels = function(data) {
            var newLabels = {};
            for (var index in data) {
                var feature = data[index];
                if (feature.labels !== undefined) {
                    for (var labelIndex in feature.labels) {
                        var label = feature.labels[labelIndex];
                        if (label !== undefined && label !== "") {
                            var count = newLabels[label];
                            if (count !== undefined) {
                                newLabels[label] = 1;
                            } else {
                                newLabels[label] = newLabels[label]+1;
                            }
                        }
                    }
                }
            }
            $scope.availableLabels = Object.keys(newLabels);
            $scope.labelSet = newLabels;
        };




        // var rowTemplate = '<div><a ng-dblclick=\"grid.appScope.onDblClick(row)\" ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns track by col.colDef.name\" class=\"ui-grid-cell\" ng-class=\"{ \'ui-grid-row-header-cell\': col.isRowHeader }\" ui-grid-cell ></div>'
        //
        // var tableHeaders = [
        //     { field: 'name', displayName: 'Feature', type: 'string', width: 400 },
        //     { field: 'scenariosTotal', displayName: 'Total', type: 'int', enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum },
        //     { field: 'scenariosPassed', displayName: "Passed", type: 'int', enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum },
        //     { field: 'scenariosFailed', displayName: "Failed", type: 'int', enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum },
        //     { field: 'scenariosPending', displayName: "Pending", type: 'int', enableFiltering: false, aggregationType: uiGridConstants.aggregationTypes.sum },
        //     { field: 'overallStatus', displayName: "Status", type: 'string', enableFiltering: false }
        // ];
        //
        // var viewFeature = function(featureOverview) {
        //     $state.go('feature', {featureId: featureOverview.featureId});
        // };
        //
        // $scope.features = {
        //     showGridFooter: true,
        //     showColumnFooter: true,
        //     enableFiltering: true,
        //     enableCellEdit: false,
        //     enableSorting: true,
        //     columnDefs: tableHeaders,
        //     enableRowSelection: true,
        //     enableSelectAll: false,
        //     noUnselect: true,
        //     enableRowHeaderSelection: false,
        //     multiSelect: false,
        //     rowTemplate: rowTemplate,
        //     onRegisterApi: function (gridApi){
        //         $scope.gridApi = gridApi;
        //     },
        //     appScopeProvider: {
        //         onDblClick : function(row) {
        //             viewFeature(row.entity)
        //         }
        //     }
        // };
        //
        // $scope.gridHandlers = {
        //
        // };
        //
        // $scope.cucumberFeatures.data = [];
        //
        var initChart = function(data) {
            $scope.statsOverview = data;
            $scope.featuresChart = {};
            $scope.featuresChart.type = "PieChart";
            $scope.featuresChart.data = {
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
            $scope.featuresChart.options = {
                'title': 'Features',
                // 'is3D': true,
                pieHole: 0.4,
                slices: {
                    0: { color: '#B94A4B' }, //failed
                    1: { color: '#468847' }, //passed
                    2: { color: '#c09853' } // pending
                }
            };
        }

        // $scope.selected = {};
        // Any function returning a promise object can be used to load values asynchronously

    }
})();