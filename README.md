# Matrix Calculations #

## Longest Sub-Matrix Calculation (For all 0s or 1s) ##
* To run the test cases, execute **``mvn test``**
* To run the application, execute **``mvn spring-boot:run``**
* Request
  - PATH: PUT /amura-tech/matrix-calculations/longest-sub-matrix
  - HEADER: Authorization=Basic ${app.secret-token}
  - BODY:
  {
    "matrix": ${a-two-dimensional-array},
    "evaluationMode": ${ZEROES | ONES}
  }


## TODO ##
* There can be multiple matrices with same area, so return list of such matrices instead of the first one

