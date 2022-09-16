# Getting Started with CoreLib Java
## Introduction
This project contains core logic and the utilities for the APIMatic's Java SDK
## Prerequisites
* The JRE flavor requires `JDK 1.8`.
## Install the maven package
Core lib's Maven group ID is `io.apimatic`, and its artifact ID is `core-lib`.
To add a dependency on core library using Maven, use the following:
```java
<dependency>
    <groupId>io.apimatic</groupId>
    <artifactId>core-lib</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```


## Classes
| Name                                                                    | Description                                                        |
|-------------------------------------------------------------------------|--------------------------------------------------------------------|
| [`ApiCall`](./src/main/java/io/apimatic/core_lib/ApiCall.java)                     | An API call, or API request, is a message sent to a server asking an API to provide a service or information |
| [`Parameter`](./src/main/java/io/apimatic/core_lib/Parameter.java)                     | HTTP parameters consist of a type, a name, and a value. These parameters appear in the header and body of an HTTP request. |
| [`ErrorCase`](./src/main/java/io/apimatic/core_lib/ErrorCase.java)                     | A class is responsible to generate the SDK Exception |
| [`GlobalConfiguration`](./src/main/java/io/apimatic/core_lib/GlobalConfiguration.java)                     | A class which hold the global configuration properties to make a successful Api Call |
| [`HttpRequest`](./src/main/java/io/apimatic/core_lib/HttpRequest.java)                     | An HTTP request is made by a client, to a named host, which is located on a server |
| [`ResponseHandler`](./src/main/java/io/apimatic/core_lib/ResponseHandler.java)                     | Handler that encapsulates the process of generating a response object from a Response |
| [`HeaderAuth`](./src/main/java/io/apimatic/core_lib/authentication/HeaderAuth.java)              | A class supports HTTP authentication through HTTP Headers |
| [`QueryAuth`](./src/main/java/io/apimatic/core_lib/authentication/QueryAuth.java)                | A class supports HTTP authentication through query parameters |
| [`HttpClientConfiguration`](./src/main/java/io/apimatic/core_lib/configurations/http/client/HttpClientConfiguration.java)                     | Class to hold HTTP Client Configuration |
| [`EndpointConfiguration`](./src/main/java/io/apimatic/core_lib/configurations/http/request/EndpointConfiguration.java)                     | The configuration for an endpoint |
| [`AsyncExecutor`](./src/main/java/io/apimatic/core_lib/request/async/AsyncExecutor.java)                     | Executor service for asynchronous HTTP endpoint call |
| [`OptionalNullable`](./src/main/java/io/apimatic/core_lib/types/OptionalNullable.java)                     | Class to encapsulate fields which are Optional as well as Nullable |
| [`BaseModel`](./src/main/java/io/apimatic/core_lib/types/BaseModel.java)                     | Base model for all the models |
| [`ApiException`](./src/main/java/io/apimatic/core_lib/types/ApiException.java)                     | This is the base class for all exceptions that represent an error response from the server |
| [`CoreHelper`](./src/main/java/io/apimatic/core_lib/utilities/CoreHelper.java)                     | This is a Helper class with commonly used utilities for the SDK |
| [`DateHelper`](./src/main/java/io/apimatic/core_lib/utilities/DateHelper.java)                     | This is a utility class for Date operations |
| [`LocalDateTimeHelper`](./src/main/java/io/apimatic/core_lib/utilities/LocalDateTimeHelper.java)                     | This is a utility class for LocalDateTime operations |
| [`ZonedDateTimeHelper`](./src/main/java/io/apimatic/core_lib/utilities/ZonedDateTimeHelper.java)                     | This is a utility class for ZonedDateTime operations |
| [`TestHelper`](./src/main/java/io/apimatic/core_lib/testing/TestHelper.java)                     | Contains utility methods for comparing objects, arrays and files. |

## Interfaces
| Name                                                                    | Description                                                        |
|-------------------------------------------------------------------------|--------------------------------------------------------------------|
| [`AsyncResponseHandler`](./src/main/java/io/apimatic/core_lib/request/async/AsyncResponseHandler.java)                     | A Handler that handles the response asynchronously |
| [`RequestExecutor`](./src/main/java/io/apimatic/core_lib/request/async/RequestExecutor.java)                     | A Request executor that executes request and returns the response asynchronously  |
| [`RequestSupplier`](./src/main/java/io/apimatic/core_lib/request/async/RequestSupplier.java)                     | A Request Supplier that supplies the request |
| [`TypeCombinator`](./src/main/java/io/apimatic/core_lib/annotations/TypeCombinator.java)                     | This is a container of annotations for oneOf/anyOf cases |

## Links
* [Core-interfaces-java](https://github.com/apimatic/core-interfaces-java)
* [OkHttpClient](https://github.com/apimatic/okhttp-client-lib)
