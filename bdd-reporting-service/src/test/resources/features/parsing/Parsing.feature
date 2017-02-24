Feature: Parsing various source files

  Scenario: Parse a Cucumber JSON file
    Given a cucumber json report file
    When the cucumber report file is uploaded
    Then we should receive a positive acknowledgement from the system


  Scenario: Parse a Pickles JSON file
    Given a pickles json report file
    When the pickles report file is uploaded
    Then we should receive a positive acknowledgement from the system
