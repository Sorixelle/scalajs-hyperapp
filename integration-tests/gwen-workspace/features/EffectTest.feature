Feature: Effect Firing

  Scenario: Validate page load
      Given I run the effect test
       Then the message should be "Ping"

  Scenario: Fire effect
      Given I click the update button
        And I wait 5 seconds
       Then the message should be "Pong"