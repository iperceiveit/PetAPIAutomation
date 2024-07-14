Feature: Pet API for adding, updating, deleting and fetching a pet

  Scenario: Add a new pet
    Given I have a new pet with name "Jack", age 3, avatarUrl "http://example.com/Jackddy.jpg" and category "Dog"
    When I add the pet
    Then the pet is added successfully

  Scenario: Find a pet by ID
    Given I retrieve the pet by the ID from the previous scenario
    Then the pet details are returned

  Scenario: Update an existing pet
    Given I retrieve the pet by the ID from the previous scenario
    When I update the pet with name "Max", age 4, avatarUrl "http://example.com/Max.jpg" and category "cat"
    Then the pet is updated successfully

  Scenario: Delete a pet
    Given I retrieve the pet by the ID from the previous scenario
    When I delete the pet
    Then the pet is deleted successfully

  Scenario: Add a new pet only with mandatory details
    Given I have a new pet with name "Max" and age 4
    When I add the pet
    Then the pet is added successfully

@negative
Scenario Outline: Try to <operation> a pet with invalid pet id or details
  Given I retrieve the pet by the ID from the previous scenario
  When I "<operation>" the pet
  Then the pet is not "<operation>" without "<details>"

  Examples:
    | operation  | | details |
    | update     | | name    |
    | delete     | | invalidid |
    | retrieve   | | invalidid |

  @negative
  Scenario: Try adding a new pet without a mandatory field
    Given I "add" the pet
    Then a validation error occurs

  @negative
  Scenario: Try update the pet without a mandatory field
    Given I retrieve the pet by the ID from the previous scenario
    When Send a update request with out Age parameter
    Then the pet is not "update" without "age"