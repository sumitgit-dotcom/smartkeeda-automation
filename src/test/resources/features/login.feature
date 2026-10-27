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
    
    @freemock
  Scenario: Navigate Free Mock, select LIC AAO test, and print the result
    Given I navigate to the Testzone homepage
    When I click the Free Mock button
    And I click on the "Full Length Test" tab
    And I click the first card in the tabs wrapper
    And I click on the LIC AAO Mock Test card
    And I start the first test on the dashboard
    Then I print the test results
    
    
    @speeddrill
  Scenario: Navigate to Sectional, open Speed Drills, and start a drill
    Given I navigate to the Testzone homepage
    When I click on the "Sectional" link in the header
    And I click on the "Speed Drills" link in the left pane
    And I switch to the newly opened tab
    And I click the "Start Drill" button on the drill card
    Then I print the drill page title