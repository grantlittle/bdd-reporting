Feature: Parsing various source files

  Scenario: Parse a Cucumber JSON file
    Given a cucumber json report file
    When the cucumber report file is parsed
    Then we should receive a acknowledgement from the system
