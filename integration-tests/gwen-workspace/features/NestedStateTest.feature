Feature: Actions Modifying Nested State

  Scenario: Validate page load
      Given I run the nested state test
       Then the counter text should be "0"

  Scenario: Fire action on nested state
      Given I click the counter button
       Then the counter text should be "1"