Feature: Components

  Scenario: Test component evaluation and rendering
      Given I run the component test
       Then the component text should be "Component"
        And the children component text should be "Component With Children"