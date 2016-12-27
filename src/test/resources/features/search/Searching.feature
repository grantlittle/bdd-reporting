# Created by grant at 27/12/16
Feature: Search
  The ability to search is important for various roles so Scenarios can be confirmed to denied to be working and
  covered in the feature list

  Scenario: Search for a feature that has been uploaded
    Given some reports have been uploaded
    When I search for a term
    Then I should see all items related to that term in the search results