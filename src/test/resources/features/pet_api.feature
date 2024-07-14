Feature: Pet API for adding, updating, deleting and fetching a pet

  Scenario: Add a new pet
    Given I have a new pet with name "Buddy", age 3, avatarUrl "http://example.com/buddy.jpg" and category "Dog"
    When I add the pet
    Then the pet is added successfully

  Scenario: Add a new pet only with mandatory details
    Given I have a new pet with name "Peter" and age 4
    When I add the pet
    Then the pet is added successfully

  Scenario: Find a pet by ID
    Given a pet exists with ID 2
    When I retrieve the pet by ID
    Then the pet details are returned

  Scenario: Update an existing pet
    Given a pet exists with ID 1
    When I update the pet with name "Buddy Updated", age 4, avatarUrl "http://example.com/buddy_updated.jpg" and category "Dog"
    Then the pet is updated successfully

  Scenario: Delete a pet
    Given a pet exists with ID 1
    When I delete the pet
    Then the pet is deleted successfully

  @negative
  Scenario: Try adding a new pet without a mandatory field
    Given I have a new pet with name "John"
    When I add the pet
    Then a validation error occurs

  @negative
  Scenario: Try to update a pet with invalid petid
    Given a pet exists with ID 978
    When I update the pet with name "Update", age 4, avatarUrl "https://example.com" and category "Dog"
    Then the pet is not updated

  @negative
  Scenario: Try to delete a pet with invalid pet id
    Given a pet exists with ID 978
    When I delete the pet
    Then the pet is not deleted

  @negative
  Scenario: Try to fetch a pet with invalid petid
    Given a pet exists with ID 978
    When I retrieve the pet by ID
    Then the pet is not retrieved