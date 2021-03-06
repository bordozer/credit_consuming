# CREDIT CONCLUSION

**Spring integration** and **Spring reactor** sample application

-- **Requirement:**
- Java 8
- Gradle build tool

-- **Tech stack:**
- Spring boot
- Spring integration
- Spring reactor


Just an simple test application that emulates credit conclusion for an applicant and his co-applicant.
The same functionality realized twice using **Spring integration** and **Spring reactor**.

# Description

-- **Characters:**
- Applicant
- Co-applicant
- Bank
- Police

-- **Scenario:**

**Bank** gets inquiry request for a loan from an *applicant* and/or *co-applicant* (optional). 
Bank calls to **Police** to get information about a criminal background of the applicants.
If at least one applicant is recognized by the Police as a criminal (presents in police DB), 
the loan application cannot be satisfied.
The credit is given if both applicant are respectable citizens. 

-- **Known criminals**
- Julico Banditto
- Ben Laden
- Axl Rose

#SAMPLE JSON DATA

-- Sample inquiry request:
```
{
  "applicant": {
        "name": "Chack Norris"
    },
    "coApplicant": {
        "name": "Ben Laden"
    }
}
```
or with an empty co-applicant
```
{
  "applicant": {
        "name": "Chuck Berry"
    }
}
```
-- Positive response:
```
{
  "applicant": {
    "name": "Chack Norris"
  },
  "creditAction": "CREDIT_CONFIRMED"
}
```
-- Negative response:
```
{
  "applicant": {
    "name": "Ben Laden"
  },
  "creditAction": "REFUSE"
}
```





