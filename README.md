# status-tracker-api
API to display cross domain information
Team Blasters – Cross Domain solution of Entity data with opportunities.

**Challenge:** 
Although beta.sam.gov has consolidated the 10 different systems, but we do not have a cross domain search and reporting system. 
For Entity, you have to select sam.gov functionality/API, for awards, you have to go for fpds.gov and for new contracts you have to go for fbo.gov. 
Team Blasters have taken this challenge to consolidate the available API and created an AWS lambda function which is auto scalable serverless architecture to support the user request. We have used duns as the starting parameter that the user will pass to use this solution.

**Used API's:**
Entity API : entity-information/v1/api/entities?api_key=xxxxxx&ueiDUNS=835130485
Exclusion API: entity-information/v1/api/exclusions?api_key=xxxxxxxx&ueiDUNS=835130485
Awards API: contract-data/v1/contracts?api_key=xxxxxxxx&ueiDUNS=835130485&dateSigned=[07/24/2019,07/24/2020]&dollarsObligated=[0,]

**Contract Opportunities API:**
/opportunities/v1/search?api_key=xxxxxxxxxx&limit=100&postedFrom=07/23/2019&postedTo=07/24/2020&ncode=541512

**Lambda function gateway api proxy URL :**
https://0deumoyrnl.execute-api.us-east-1.amazonaws.com/prod/statusTracker?ueiDUNS=835130485

**Usage:**
By only one duns, we can display the entity  details, exclusion details, 
Amount of awards that were received in last year and based on the previous awards that already worked, what are the current opportunities are available with same product.
