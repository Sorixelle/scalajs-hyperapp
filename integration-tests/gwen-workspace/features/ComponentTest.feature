Feature: Component rendering

  Scenario: Test component rendering
      Given I run the component test
       Then the message should be "Hello, World!"
        And the component message should be "Test Message"