# Jumbo MyStore Application
## Features
| Feature | Description|API Endpoint|
|---------|------------|------------|
|Find By System Location| Resolves the closest stores based on the location of the system|`/store-locations/system`|
|Find By Address| Resolves the closest stores based on a given Postal Code and House number|`/store-locations/{postalcode}/{houseNumber}`|
|Find By Location| Resolves the closes stores based on a given latitude and longitude|`/store-locations/{longitude},{latitude}`|

## Setup and Operation
### Prerequisites
- Java 21 SDK installed
- Maven installed
- Code installed and checkedout via GIT
### Running
To run the code you need to do the following steps
1. Open the terminal in the location that the code was checked out into
2. Run the following command: `mvn spring-boot:run`

### Calling Service Endpoints
MyStore comes with swagger installed, to access swagger-ui open a browser and navigate to `http://localhost:8080/swagger-ui/index.html`


## Notes on Implementation
### How System location is determined
The system location is determined by using a service to resolve the external IP of a machine and resolve the location of machine using another service.
#### Apis
|API|Description|
|---|-----------|
|Ipify|Resolves the IP of a machine|
|IPinfo|Resolves information about a specific IP including location in latitude and longitude **Free version does not support IP resolution through VPN**|

**These APIs were selected as they do not require an API key and are free therefore it is straightforward to the assessor to run the application without**

### How the location of the Postal Code is determined.
The longitude and latitude of the postal code are resolved using the [PDOK](https://www.pdok.nl/pdok-locatieserver) service

## Major Architectural decisions
- Java 21 was selected as this is the latest version that is well support for the frameworks in question
- Spring Boot was selected due to it's de-facto nature and that it is therefore well understood in the general community
- Onion Architecture was selected as the software architecture for the application
- Selection of APIs was driven by two factors: They must be free and must be able to operate without the need for an API key thereby complicating the process of running this application
- The general coding approach was to utilize Monads as a general way of handling methods with complex outputs, usually where the method needs to return something or nothing or where there is a success state or error state
- As a challenge the entire applicatin has been coded without a single if statement
- SSL validation was disabled in order to simplify the running of the app so that certificate stores do not need to be configured by the end-user
- Caching is a low hanging fruit performance enhancement given the static nature of the application data


## Architecturally significant components
| Component  | Description                                                                                                                                                                                                                                                  |
|------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| MyStoreAPI | Spring Boot Controller class which provides REST endpoints                                                                                                                                                                                                   |
|SurroundingStoreService| Service which orchestrates the GeoService and StoreRepository in resolving the closest store                                                                                                                                                                 |
|GeoService | Service which resolves a location either using the System IP or an address                                                                                                                                                                                   |
|StoreRepository| Service which wraps the 'stores.json' file and provides methods to query the data                                                                                                                                                                            |
|LatLongPosition| Class which maps a latitude/longitude position, used to determine the distance between two points, current implementation uses **Equi-Rectangular** as this is simple, fast and provides good enough accuracy for the requirements                           |
|Result| A Monad which is used to create an execution pipeline in the service layer, Basically a Result Monad models two states: **OK** for sucessfull calls and **Error** for failures. Using this Monad we can have a standard approach to processing service calls |
