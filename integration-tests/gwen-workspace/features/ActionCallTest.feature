Feature: Action Calling

  Scenario: Validate page load
     Given I run the action call test
      Then the counter should be "0"

  Scenario: Fire increment action
      Given I click the increment button
       Then the counter should be "1"

  Scenario: Fire decrement action
      Given I click the decrement button
       Then the counter should be "0"
