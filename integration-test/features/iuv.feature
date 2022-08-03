Feature: Create a valid IUV

  # Health check: The service is up and running
  Background:
    Given IUV Generator Function running

  # Case OK
  Scenario: An organization requests the generation of an IUV
    Given an organization
    When the organization ask for a valid IUV
    Then the organization gets the status code 201
    And the length of the iuv is 17 digits
  
  # Case KO: 400 BAD REQUEST  
  Scenario: An organization requests the generation of an IUV with bad body request
    Given an organization
    When the organization asks for a valid IUV with incorrect body request
    Then the organization gets the status code 400
