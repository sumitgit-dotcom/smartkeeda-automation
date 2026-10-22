Feature: Smartkeeda Authentication

  @login
  Scenario: Navigate to Testzone and open the login modal
    Given I navigate to the Testzone homepage
    When I click on the Log In button
    Then the login modal should appear