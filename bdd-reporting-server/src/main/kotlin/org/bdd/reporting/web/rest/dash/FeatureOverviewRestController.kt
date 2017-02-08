package org.bdd.reporting.web.rest.dash

class DashboardOverview(var totalFeatures : Int = 0,
                        var passedScenarios : Int = 0,
                        var failedScenarios : Int = 0,
                        var ignoredScenarios : Int = 0,
                        var pendingScenarios : Int = 0,
                        var totalScenarios : Int = 0)
