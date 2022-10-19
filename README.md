# APIMatic Core Library for JAVA
[![Maven Central][maven-badge]][maven-url]
[![Tests][test-badge]][test-url]
[![Licence][license-badge]][license-url]

## Introduction
This project contains core logic and the utilities for the APIMatic's Java SDK
## Prerequisites
* The JRE flavor requires `JDK 1.8`.
## Install the maven package
Core lib's Maven group ID is `io.apimatic`, and its artifact ID is `core`.
To add a dependency on core library using Maven, use the following:
```java
<dependency>
    <groupId>io.apimatic</groupId>
    <artifactId>core</artifactId>
    <version>0.2.0</version>
</dependency>
```


## Classes
| Name                                                                    | Description                                                        |
|-------------------------------------------------------------------------|--------------------------------------------------------------------|
| [`ApiCall`](./src/main/java/io/apimatic/core/ApiCall.java)                     | An API call, or API request, is a message sent to a server asking an API to provide a service or information |
| [`Parameter`](./src/main/java/io/apimatic/core/Parameter.java)                     | HTTP parameters consist of a type, a name, and a value. These parameters appear in the header and body of an HTTP request. |
| [`ErrorCase`](./src/main/java/io/apimatic/core/ErrorCase.java)                     | A class is responsible to generate the SDK Exception |
| [`GlobalConfiguration`](./src/main/java/io/apimatic/core/GlobalConfiguration.java)                     | A class which hold the global configuration properties to make a successful Api Call |
| [`HttpRequest`](./src/main/java/io/apimatic/core/HttpRequest.java)                     | An HTTP request is made by a client, to a named host, which is located on a server |
| [`ResponseHandler`](./src/main/java/io/apimatic/core/ResponseHandler.java)                     | Handler that encapsulates the process of generating a response object from a Response |
| [`HeaderAuth`](./src/main/java/io/apimatic/core/authentication/HeaderAuth.java)              | A class supports HTTP authentication through HTTP Headers |
| [`QueryAuth`](./src/main/java/io/apimatic/core/authentication/QueryAuth.java)                | A class supports HTTP authentication through query parameters |
| [`CoreHttpClientConfiguration`](./src/main/java/io/apimatic/core/configurations/http/client/CoreHttpClientConfiguration.java)                     | To hold HTTP Client Configuration |
| [`ApiLoggingConfiguration`](./src/main/java/io/apimatic/core/configurations/http/client/ApiLoggingConfiguration.java)                     | To hold logging configuration |
| [`EndpointConfiguration`](./src/main/java/io/apimatic/core/configurations/http/request/EndpointConfiguration.java)                     | The configuration for an endpoint |
| [`AsyncExecutor`](./src/main/java/io/apimatic/core/request/async/AsyncExecutor.java)                     | Executor service for asynchronous HTTP endpoint call |
| [`OptionalNullable`](./src/main/java/io/apimatic/core/types/OptionalNullable.java)                     | Class to encapsulate fields which are Optional as well as Nullable |
| [`BaseModel`](./src/main/java/io/apimatic/core/types/BaseModel.java)                     | Base model for all the models |
| [`CoreApiException`](./src/main/java/io/apimatic/core/types/CoreApiException.java)                     | This is the base class for all exceptions that represent an error response from the server |
| [`MultipartFileWrapper`](./src/main/java/io/apimatic/core/types/http/request/MultipartFileWrapper.java)                     | To wrap file and headers to be sent as part of a multipart request |
| [`MultipartWrapper`](./src/main/java/io/apimatic/core/types/http/request/MultipartWrapper.java)                     | To wrap byteArray and headers to be sent as part of a multipart request |
| [`CoreHelper`](./src/main/java/io/apimatic/core/utilities/CoreHelper.java)                     | This is a Helper class with commonly used utilities for the SDK |
| [`DateHelper`](./src/main/java/io/apimatic/core/utilities/DateHelper.java)                     | This is a utility class for LocalDate operations |
| [`LocalDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/LocalDateTimeHelper.java)                     | This is a utility class for LocalDateTime operations |
| [`ZonedDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/ZonedDateTimeHelper.java)                     | This is a utility class for ZonedDateTime operations |
| [`XMLDateHelper`](./src/main/java/io/apimatic/core/utilities/XMLDateHelper.java)                     | This is a utility class for XML LocalDate operations |
| [`XMLLocalDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/XmlLocalDateTimeHelper.java)                     | This is a utility class for XML LocalDateTime operations |
| [`XMLZonedDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/XmlZonedDateTimeHelper.java)                     | This is a utility class for XML ZonedDateTime operations |
| [`JsonObject`](./src/main/java/io/apimatic/core/utilities/JsonObject.java)                     | Wrapper class for JSON object |
| [`JsonValue`](./src/main/java/io/apimatic/core/utilities/JsonValue.java)                     | Wrapper class for JSON value |
| [`TestHelper`](./src/main/java/io/apimatic/core/testing/TestHelper.java)                     | Contains utility methods for comparing objects, arrays and files |

## Interfaces
| Name                                                                    | Description                                                        |
|-------------------------------------------------------------------------|--------------------------------------------------------------------|
| [`AsyncResponseHandler`](./src/main/java/io/apimatic/core/request/async/AsyncResponseHandler.java)                     | A Handler that handles the response asynchronously |
| [`RequestExecutor`](./src/main/java/io/apimatic/core/request/async/RequestExecutor.java)                     | A Request executor that executes request and returns the response asynchronously  |
| [`RequestSupplier`](./src/main/java/io/apimatic/core/request/async/RequestSupplier.java)                     | A Request Supplier that supplies the request |
| [`TypeCombinator`](./src/main/java/io/apimatic/core/annotations/TypeCombinator.java)                     | This is a container of annotations for oneOf/anyOf cases |

## Links
* [Core-interfaces-java](https://github.com/apimatic/core-interfaces-java)
* [OkHttpClient](https://github.com/apimatic/okhttp-client-lib)

[license-badge]: https://img.shields.io/badge/licence-APIMATIC-blue
[license-url]: LICENSE
[maven-badge]: https://img.shields.io/maven-central/v/io.apimatic/core?color=green
[maven-url]: https://central.sonatype.dev/artifact/io.apimatic/core/0.2.0
[test-badge]: https://github.com/apimatic/core-lib-java/actions/workflows/build-and-test.yml/badge.svg
[test-url]: https://github.com/apimatic/core-lib-java/actions/workflows/build-and-test.yml