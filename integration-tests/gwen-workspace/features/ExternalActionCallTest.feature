Feature: External Action Calling

  Scenario: Validate page load
      Given I run the external action call test
       Then the counter should be "0"

  Scenario: Fire action externally
      Given I call the external action "incrementCounter"
       Then the counter should be "1"