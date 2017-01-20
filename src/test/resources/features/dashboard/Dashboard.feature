Feature: BDD Reporting Dashboard

  A dashboard is required that can represent various pieces of data.

  Scenario: Dashboard should show the current number of unique tests in their various states.

    Given the default test set has been uploaded
    When the dashboard is displayed
    Then the following data should be displayed
    | scenariosPassed  | 1       |
    | scenariosFailed  | 1       |
    | scenariosPending | 1       |