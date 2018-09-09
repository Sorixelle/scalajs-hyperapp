Feature: Lazy Components

  Scenario: Check component children
      Given I run the lazy component test
       Then the lazy counter text should be "0"
        And the lazy children counter text should be "0"
        And the lazy children counter heading should be "Lazy Counter with Children"


  Scenario: Fire actions from lazy component
      Given I click the lazy counter button
       Then the lazy counter text should be "1"

  Scenario: Fire actions from lazy component with children
      Given I click the lazy children counter button
       Then the lazy children counter text should be "1"