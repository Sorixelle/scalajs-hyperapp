Feature: Subscriptions to global events

  Scenario: Validate page load
      Given I run the subscription test
       Then the message should be "<empty>"

  Scenario: Fire global event
      Given I type "Testing" in the input field
       Then the message should be "Testing"

  Scenario: Cancel subscription
      Given I click the toggle button
        And I clear the input field
        And I type "asdf" in the input field
       Then the message should be "Testing"