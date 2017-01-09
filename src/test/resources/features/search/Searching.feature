# Created by grant at 27/12/16
Feature: Search
  The ability to search is important for various roles so Scenarios can be confirmed to denied to be working and
  covered in the feature list

  Scenario: Search for a feature by name

    Given some reports have been uploaded
    When I search by name Feature1
    Then I should see all items related to that term in the search results

  Scenario: Search for a feature by tag

    Given the default test set has been uploaded
    When I search by tag @tag1
    Then I should get the feature Feature1 returned