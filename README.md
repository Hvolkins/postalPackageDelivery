# Package delivery tool
The PackageDelivery program implements work with package from console (input/output data).

## Running the application

* Download the zip or clone the Git repository.
* Unzip the zip file (if you downloaded one)
* Import project over pom.xml (used maven) in your IDE
* Used "mvn package" for jar generation or used jar imported into Git repository
* Jar file is in target folder, for start app enter into console "java -jar postalPackageDelivery-1.0.jar" 
* Copy files fees.txt and input.txt in some folder on your pc

* After starting app you can see help menu in console: 


   Package delivery tool:
  * -n, -new	Enter the new package 
  * -i, -init	Read packages from file
  * -f, -file	Read fees from file
  * -q, -quit	Exit from tool

### Rules:
* For adding new postal package enter the weight of the package and postal code (56.123 41501). 
  Negative number will be automatically converted into positive number. 
  For separator in double type of weight value use .(dot), postal code must have 5 digits.
  

* For import data from files enter the name with path to file on your PC, for example: 
  - "C:/test/input.txt" - for adding postal packages into memory
  - "C:/test/fees.txt" - for adding information about fees related to package weights into memory




