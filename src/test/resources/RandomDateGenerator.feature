#Author: Sharanya Ravindran

Feature: Test Random date generator application
  
 #Scenario 1
  Scenario Outline: Check the number of dates generated matches with the count given
    Given The user is on the date generator application
    When Enter <count> in How many dates to generate? text box
    Then Verify the number of dates generated. 

    Examples: 
      | count |
      |     5 |
      |     7 |
  
 # Scenario 2 
  Scenario: Check if duplicate dates are not generated
    Given The user is on the date generator application
    When Enter 5 in How many dates to generate? text box
    And Check the dates generated
    Then No duplicate date should be generated
# Scenario 3
   Scenario: Check if date generated is a valid date
    Given The user is on the date generator application
    When Enter 3 in How many dates to generate? text box
    And Check the dates generated
    Then The date generated should be a valid date
 # Scenario 4   
    Scenario Outline: Check if the dates generated are within the range of Start and End date
    Given The user is on the date generator application
    When Enter 3 in How many dates to generate? text box
    And Enter "<Start Date>" and "<End Date>"
    Then The dates generated should be in range of start and end dates
    Examples:
    |Start Date|End Date  |
    |02-02-1600|02-02-2210|
    |02-02-2000|02-02-2200|
    |02-02-1000|02-02-1000|
    |01-01-1900|01-01-1800|
    # Scenario 5 
    #Scenario Outline: Check if the dates generated are in format selected
    #Given The user is on the date generator application
    #When Enter 3 in How many dates to generate? text box
    #And Select "<Format>" in Date Output Format dropdown
    #Then The dates generated should be in the format selected
    #Examples:
    #|Format             |
    #|mm-dd-yyyy|
    #|mm-dd-yyyy-hh-mm-ss|
    
    
    
    