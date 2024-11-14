# APIMatic Core Library for JAVA

[![Maven Central][maven-badge]][maven-url]
[![Tests][test-badge]][test-url]
[![Lint Code][lint-badge]][lint-url]
[![Test Coverage][test-coverage-url]][code-climate-url]
[![Licence][license-badge]][license-url]

## Introduction

This project contains core logic and the utilities for the APIMatic's Java SDK

## Prerequisites

* The JRE flavor requires `JDK 1.8`.

## Install the maven package

Core lib's Maven group ID is `io.apimatic`, and its artifact ID is `core`.

## Classes

| Name                                                                                                                             | Description                                                                                                                                                                                                                                           |
|----------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [`ApiCall`](./src/main/java/io/apimatic/core/ApiCall.java)                                                                       | An API call, or API request, is a message sent to a server asking an API to provide a service or information                                                                                                                                          |
| [`Parameter`](./src/main/java/io/apimatic/core/Parameter.java)                                                                   | HTTP parameters consist of a type, a name, and a value. These parameters appear in the header and body of an HTTP request.                                                                                                                            |
| [`ErrorCase`](./src/main/java/io/apimatic/core/ErrorCase.java)                                                                   | A class which is responsible to generate the SDK Exception                                                                                                                                                                                            |
| [`GlobalConfiguration`](./src/main/java/io/apimatic/core/GlobalConfiguration.java)                                               | A class which hold the global configuration properties to make a successful Api Call                                                                                                                                                                  |
| [`HttpRequest`](./src/main/java/io/apimatic/core/HttpRequest.java)                                                               | An HTTP request is made by a client, to a named host, which is located on a server                                                                                                                                                                    |
| [`ResponseHandler`](./src/main/java/io/apimatic/core/ResponseHandler.java)                                                       | Handler that encapsulates the process of generating a response object from a Response                                                                                                                                                                 |
| [`SdkLogger`](./src/main/java/io/apimatic/core/logger/SdkLogger.java)                                                            | A class to log Request and Response.                                                                                                                                                                                                                  |
| [`NullSdkLogger`](./src/main/java/io/apimatic/core/logger/NullSdkLogger.java)                                                    | Null SdkLogger implementation where logger is not configured.                                                                                                                                                                                         |
| [`Slf4jLogger`](./src/main/java/io/apimatic/core/logger/Slf4jLogger.java)                                                        | Logger implementation supporting Slf4j Facade.                                                                                                                                                                                                        |
| [`ConsoleLogger`](./src/main/java/io/apimatic/core/logger/ConsoleLogger.java)                                                    | Default Logger implementation if logging is enabled.                                                                                                                                                                                                  |
| [`SdkLoggerFactory`](./src/main/java/io/apimatic/core/logger/SdkLoggerFactory.java)                                              | Returns an instance of ApiLogger based on the provided configuration.                                                                                                                                                                                 |
| [`LoggerUtilities`](./src/main/java/io/apimatic/core/logger/LoggerUtilities.java)                                                | Utilitiy methods for Logger.                                                                                                                                                                                                                          |
| [`SdkLoggingConfiguration`](./src/main/java/io/apimatic/core/logger/configurations/SdkLoggingConfiguration.java)                 | To hold logging configuration                                                                                                                                                                                                                         |
| [`SdkBaseHttpLoggingConfiguration`](./src/main/java/io/apimatic/core/logger/configurations/SdkBaseHttpLoggingConfiguration.java) | Represents base configuration for http logging.                                                                                                                                                                                                       |
| [`SdkRequestLoggingConfiuration`](./src/main/java/io/apimatic/core/logger/configurations/SdkRequestLoggingConfiguration.java)    | Represents configuration for logging requests.                                                                                                                                                                                                        |
| [`SdkResponseLoggingConfiguration`](./src/main/java/io/apimatic/core/logger/configurations/SdkResponseLoggingConfiguration.java) | Represents configuration for logging responses.                                                                                                                                                                                                       |
| [`AuthBuilder`](./src/main/java/io/apimatic/core/authentication/AuthBuilder.java)                                                | A class to build and validate provided combination of auth schemes.                                                                                                                                                                                   |
| [`AuthCredential`](./src/main/java/io/apimatic/core/authentication/AuthCredential.java)                                          | A parent class of [`HeaderAuth`](./src/main/java/io/apimatic/core/authentication/HeaderAuth.java) and [`QueryAuth`](./src/main/java/io/apimatic/core/authentication/QueryAuth.java) to hold the common implementation for header and query parameters |
| [`HeaderAuth`](./src/main/java/io/apimatic/core/authentication/HeaderAuth.java)                                                  | A class supports HTTP authentication through HTTP Headers                                                                                                                                                                                             |
| [`QueryAuth`](./src/main/java/io/apimatic/core/authentication/QueryAuth.java)                                                    | A class supports HTTP authentication through query parameters                                                                                                                                                                                         |
| [`AuthGroup`](./src/main/java/io/apimatic/core/authentication/multiple/AuthGroup.java)                                           | A parent class of [`And`](./src/main/java/io/apimatic/core/authentication/multiple/And.java) and [`Or`](./src/main/java/io/apimatic/core/authentication/multiple/Or.java) to hold the common functionality of multiple auth                           |
| [`And`](./src/main/java/io/apimatic/core/authentication/multiple/And.java)                                                       | A class to hold the algorithm for `And` combination of auth schemes                                                                                                                                                                                   |
| [`Or`](./src/main/java/io/apimatic/core/authentication/multiple/Or.java)                                                         | A class to hold the algorithm for `Or` combination of auth schemes                                                                                                                                                                                    |
| [`Single`](./src/main/java/io/apimatic/core/authentication/multiple/Single.java)                                                 | A class to hold the logic for single auth scheme, it is used as leaf node for auth combination or it could be used directly to apply one auth only to the http request                                                                                |
| [`CoreHttpClientConfiguration`](./src/main/java/io/apimatic/core/configurations/http/client/CoreHttpClientConfiguration.java)    | To hold HTTP Client Configuration                                                                                                                                                                                                                     |
| [`EndpointConfiguration`](./src/main/java/io/apimatic/core/configurations/http/request/EndpointConfiguration.java)               | The configuration for an endpoint                                                                                                                                                                                                                     |
| [`AsyncExecutor`](./src/main/java/io/apimatic/core/request/async/AsyncExecutor.java)                                             | Executor service for asynchronous HTTP endpoint call                                                                                                                                                                                                  |
| [`OptionalNullable`](./src/main/java/io/apimatic/core/types/OptionalNullable.java)                                               | Class to encapsulate fields which are Optional as well as Nullable                                                                                                                                                                                    |
| [`BaseModel`](./src/main/java/io/apimatic/core/types/BaseModel.java)                                                             | Base model for all the models                                                                                                                                                                                                                         |
| [`CoreApiException`](./src/main/java/io/apimatic/core/types/CoreApiException.java)                                               | This is the base class for all exceptions that represent an error response from the server                                                                                                                                                            |
| [`MultipartFileWrapper`](./src/main/java/io/apimatic/core/types/http/request/MultipartFileWrapper.java)                          | To wrap file and headers to be sent as part of a multipart request                                                                                                                                                                                    |
| [`MultipartWrapper`](./src/main/java/io/apimatic/core/types/http/request/MultipartWrapper.java)                                  | To wrap byteArray and headers to be sent as part of a multipart request                                                                                                                                                                               |
| [`CoreHelper`](./src/main/java/io/apimatic/core/utilities/CoreHelper.java)                                                       | This is a Helper class with commonly used utilities for the SDK                                                                                                                                                                                       |
| [`DateHelper`](./src/main/java/io/apimatic/core/utilities/DateHelper.java)                                                       | This is a utility class for LocalDate operations                                                                                                                                                                                                      |
| [`LocalDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/LocalDateTimeHelper.java)                                     | This is a utility class for LocalDateTime operations                                                                                                                                                                                                  |
| [`ZonedDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/ZonedDateTimeHelper.java)                                     | This is a utility class for ZonedDateTime operations                                                                                                                                                                                                  |
| [`XMLDateHelper`](./src/main/java/io/apimatic/core/utilities/XMLDateHelper.java)                                                 | This is a utility class for XML LocalDate operations                                                                                                                                                                                                  |
| [`XMLLocalDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/XmlLocalDateTimeHelper.java)                               | This is a utility class for XML LocalDateTime operations                                                                                                                                                                                              |
| [`XMLZonedDateTimeHelper`](./src/main/java/io/apimatic/core/utilities/XmlZonedDateTimeHelper.java)                               | This is a utility class for XML ZonedDateTime operations                                                                                                                                                                                              |
| [`CoreJsonObject`](./src/main/java/io/apimatic/core/utilities/CoreJsonObject.java)                                               | Wrapper class for JSON object                                                                                                                                                                                                                         |
| [`CoreJsonValue`](./src/main/java/io/apimatic/core/utilities/CoreJsonValue.java)                                                 | Wrapper class for JSON value                                                                                                                                                                                                                          |
| [`TestHelper`](./src/main/java/io/apimatic/core/utilities/TestHelper.java)                                                       | Contains utility methods for comparing objects, arrays and files                                                                                                                                                                                      |
| [`ConversionHelper`](./src/main/java/io/apimatic/core/utilities/ConversionHelper.java)                                           | A Helper class for the coversion of type (provided as function) for all structures (array, map, array of map, n-dimensional arrays etc) supported in the SDK.                                                                                         |

## Interfaces

| Name                                                                                               | Description                                                                      |
|----------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------|
| [`AsyncResponseHandler`](./src/main/java/io/apimatic/core/request/async/AsyncResponseHandler.java) | A Handler that handles the response asynchronously                               |
| [`RequestExecutor`](./src/main/java/io/apimatic/core/request/async/RequestExecutor.java)           | A Request executor that executes request and returns the response asynchronously |
| [`RequestSupplier`](./src/main/java/io/apimatic/core/request/async/RequestSupplier.java)           | A Request Supplier that supplies the request                                     |
| [`TypeCombinator`](./src/main/java/io/apimatic/core/annotations/TypeCombinator.java)               | This is a container of annotations for oneOf/anyOf cases                         |

## Links

* [Core-interfaces-java](https://github.com/apimatic/core-interfaces-java)
* [OkHttpClient](https://github.com/apimatic/okhttp-client-lib)

[license-badge]: https://img.shields.io/badge/licence-MIT-blue

[license-url]: LICENSE

[maven-badge]: https://img.shields.io/maven-central/v/io.apimatic/core?color=green

[maven-url]: https://central.sonatype.com/artifact/io.apimatic/core

[test-badge]: https://github.com/apimatic/core-lib-java/actions/workflows/build-and-test.yml/badge.svg

[test-url]: https://github.com/apimatic/core-lib-java/actions/workflows/build-and-test.yml

[code-climate-url]: https://codeclimate.com/github/apimatic/core-lib-java

[maintainability-url]: https://api.codeclimate.com/v1/badges/74e497222508f9e858d6/maintainability

[test-coverage-url]: https://api.codeclimate.com/v1/badges/74e497222508f9e858d6/test_coverage

[lint-badge]: https://github.com/apimatic/core-lib-java/actions/workflows/linter.yml/badge.svg

[lint-url]: https://github.com/apimatic/core-lib-java/actions/workflows/linter.yml
