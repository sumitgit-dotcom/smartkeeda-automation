Feature: Smartkeeda Authentication

  @login
  Scenario: Navigate to Testzone and open the login modal
    Given I navigate to the Testzone homepage
    When I click on the Log In button
    Then the login modal should appear
    
    
    @signup
  Scenario: Register a new user on Testzone
    Given I navigate to the Testzone homepage
    When I click on the Sign In button
    And I click on the SignUp Now button
    And I fill in the signup details:
      | name     | Sumit Rawat         |
      | email    | sumit.rawat@test.com|
      | password | Sumitrawat@123      |
      | confirm  | Sumitrawat@123      |
    And I click the final signup submit button
    Then I should be successfully registered or see a validation message