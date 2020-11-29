# SpringBootNumberGeneratorApp
Spring boot application that generates a sequence of numbers in the decreasing order till 0. Simulate that the function to generate a number takes a random time – say between 10 to 30 seconds. Inputs: step, goal.
The following APIs are supported:
  1. POST /api/bulkGenerate
  2. POST /api/generate
  3. GET /api/tasks/{UUID of the task}?action=get_numlist
  4. GET /api/tasks/{UUID of the task}/status

Along with this, project supports the following:
  1. Validation of Requests.
  2. JUNIT Tests with report generation capabilities.
  3. Configurable Threading options.
  4. Swagger Documentation and UI.


## Usage and Swagger
1. Checkout code, go to the main project directory and build using the following command:
```bash
mvn clean install -Dmaven.test.skip=true
```

2. Once build is successful, the Spring boot app can be run using the following command:
```bash
java -jar target\numbergenerator-0.0.1-SNAPSHOT.jar
```

3. Once the Embedded Tomcat and the application starts, the swagger UI can be accessed here:
```bash
http://localhost:8080/swagger-ui/index.html#/
```

4. APIs also will be accessible.


## Running Tests

To run JUNIT tests, under the main project directory, run the following command. This will generate reports under <Project Main Dir>\target\surefire-reports
```bash
mvn surefire-report:report
```

Following test Cases are covered both manually as well as part of unit tests:
1. GET Status with a wrong UUID
2. GET Result with a wrong UUID
3. POST generate with missing step input
4. POST generate where goal input is renamed
5. POST generate with a string value for step
6. POST generate with a negative value for goal
7. POST generate with step value > goal
8. POST generate with values Goal 10 and Step 2
9. GET Status with UUID returned from #9. Expect IN_PROGRESS first and then wait till SUCCESS
10. GET Result with UUID and Request Param. Match the data obtained
11. POST generate with values Goal 8 and Step 8
12. GET Status with UUID returned from #12 in loop. Expect IN_PROGRESS first and then SUCCESS
13. GET Result with UUID and Request Param. Match the data obtained
14. POST generate bulk (two) with one missing step input
15. POST generate bulk (three) with two negative values for goal
16. POST generate bulk (two) with one value step > goal
17. POST generate bulk (single) with one value step > goal
18. POST generate bulk (three) with values Goal 10 and Step 3, Goal 8 and Step 7, Goal 9 and Step 5
19. GET Status with UUID returned from #19. Expect IN_PROGRESS first and then wait till SUCCESS
20. GET Result with UUID and Request Param. Match the data obtained

## Contributing
NONE

## License
NONE


