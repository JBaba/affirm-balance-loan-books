# affirm-balance-loan-books

## Code Execution Details
* I have compiled this using `Java - 16`
* Open project in idea and navigate to `com.affirm.api.App.java'
* Run the "App.java"
* I have already run code for large files so "assignments.csv" and "yields.csv" can be found in root folder

## Questions And Answers

### Q: How long did you spend working on the problem?
~ 4 hours

### Q: What did you find to be the most difficult part?
* Implementing file Parser and convert text data to objects

### Q: How would you modify your data model or code to account for an eventual introduction of new, as-of-yet unknown types of covenants, beyond just maximum default likelihood and state restrictions?

I have implemented `IPredicate` and glimpse of this design can be seen,
`FacilityOverdrawPredicate`

Extend `IPredicate` and add new rule

```
             |--> BannedStatePredicate
IPredicate -----> FacilityOverdrawPredicate
             |--> MaxDefaultPredicate
             |--> XPredicate....
```

### Q: How would you architect your solution as a production service wherein new facilities can be introduced at arbitrary points in time. Assume these facilities become available by the finance team emailing your team and describing the addition with a new set of CSVs.

We should create rest POST endpoint `/add/feedNewFacilityData`
```json
{
  "facilityFilePath" : "x/f.csv",
  "covenantFilePath" : "x/c.csv",
  "bankFilePath" : "x/b.csv"
}
```
From above, we can initialize new set of data. Following things to keep in mind,
* our program will read the files from give network location and initialized data for us
* If we add new facility, then we have sort our facility list based on interest rate again
* covenant is not mandatory, but we can add new convents for existing facility using this endpoint

### Q: Your solution most likely simulates the streaming process by directly calling a method in your code to process the loans inside of a for loop. What would a REST API look like for this same service? Stakeholders using the API will need, at a minimum, to be able to request a loan be assigned to a facility, and read the funding status of a loan, as well as query the capacities remaining in facilities.

I have impl function in class `AssignmentYieldService`. We just have expose this function

```java
/**
 * Process single loan
 * @apiNote this method can be expose using rest endpoint
 * @param loan input
 */
private void processLoan(Loan loan){
        // loan processing
        }
```

For funding status we should introduce `status` field and then modify following code in
method `processLoan` -> `AssignmentYieldService.class`

```java
if (facility.canFacilityFundLoan(loan)) {
        facility.fundLoan(loan);
        // change status of loan
        loan.setStatus(LoanStatusEnum.FUNDED);
        }
```

For funding status I have declared `fundedAmount` var in `Facility.class` this can be
repurposed and let consumers know the remaining balance of facility
`(amount - fundedAmount)`

### Q: How might you improve your assignment algorithm if you were permitted to assign loans in batch rather than streaming? We are not looking for code here, but pseudo code or description of a revised algorithm appreciated.

If we do batch processing then our complexity becomes n^2 because we have to go through all the loans and each loan will go through all the facilities.

And if we have more covenants it becomes quadratic.

idea:
* Create covenant indexes
    * We can also map state to num and create
* Find set using binary search on both covenant indexes using loan values
* Find overlapping sets
* list is valid and all of them can process the loan (should be O(1) because very first item from list can process the loan)
* find indexed facilities and only apply those loans from cheapest to costliest
    * after we sort we should assign sort index
    * then covenant indexes refer to this sorted index values
    * in the end overlapping sets find min max boundary and process 


```
onInit():
    // sort facilities (map index -> facility)
    facilities = sortFacilities()
    // then give facilities sort id from top to bottom by adding additional 
    // field in Facility sortIndex
    assignFacilitySortIndex(facilities)
    // keep covenants caches for
    //    sort using maxDefaultLikelihood (tree index -> facility)
    //    sort using state by assigning index (tree index -> facility)
    allCovenants = mapAllCovenants() (map covenentId -> facility)
    maxLikelihoodHeap = createMaxLikelihoodHeap(allCovenants)
    stateHeap = createStateHeap(allCovenants)

restEndpoint(batch):
    // process beatch
    for loan in batch:
        processLoan(loan)
    
processLoan():
    set1 = binarySerachOnMaxLikelihoodHeap(loan)
    set3 = binarySerachOnStateHeap(loan)
    set4 = commonSetOfFacilites(set1, set2)
    // list is valid and all of them can process the loan
    list = getSortFacilityIndexIds()
    for facility in list:
        if(facility canFundLoan(loan)) {
            fundLoan
            return
        }
```

### Q: Discuss your solution’s runtime complexity.

```
n - number of facilities
l - number of loans (for steaming this will be 1)
c - number of covenant = 2

Sorting facilities = nlog(n)

For single streaming loan:
complexity: 
= O(n*l*c) + nlog(n) 
= O(n*1*2) + nlog(n)  
= O(n) + nlog(n)  --> O(n) < nlog(n) 
= nlog(n) 

For processing entire file using for loop (batch processing):
complexity: 
= O(n*l*c) + nlog(n) 
= O(n*l*2) + nlog(n) --> O(n*l*2) > nlog(n)  
= O(n*l) --> avoid constant 2
```
