# Movie Information Retrieval Program

## Overview

This is a Java web application built using Spring Framework. The primary functionality of this application is to retrieve comprehensive information about a movie and ascertain whether it has won the "Best Picture" Oscar. The required data is derived from the OMBD Movies API and a CSV file containing a list of Oscar winners from 1935 until 2010.

## Current State

At its current state, the program works as expected, but it uses a traditional, sequential approach to process the CSV file and save Oscar-winning movies. While it serves its purpose, this approach might not be ideal when dealing with large datasets.

Furthermore, the program uses a non-clustered Redis cache tool, WebClient, OpenCSV, and Flyway, all launched through a compose file, which are not fully optimized for a production environment. It retrieves movie data from OMBD API using WebClient and caches the results with Redis to optimize subsequent requests.

Here are the key exposed endpoints:

1. `GET /movies` - Fetches all movies.
2. `GET /movies/{id}` - Retrieves the movie with the provided ID.
3. `GET /best-picture-winners/{title}` - Fetches comprehensive information about a movie and confirms whether it has won the "Best Picture" Oscar.

## Future Improvements

1. **Spring Batch**: As the amount of data grows, processing CSV files using a traditional approach might not be scalable. The program should use Spring Batch for handling CSV files. Spring Batch is a lightweight, comprehensive batch framework designed to enable the development of robust batch applications. It provides reusable functions that are essential in processing large volumes of records, including logging/tracing, transaction management, job processing statistics, and job restart.

2. **Distributed Caching**: A distributed, cluster-aware version of Redis could be used for caching. This ensures that the cache can scale and remain available, even when the load is high.

3. **Database Improvements**: For more scalability, a sharded database can be implemented to distribute the load across multiple servers. Read-replicas can also be used to serve read requests, thereby improving performance.

4. **Application Containerization and Orchestration**: Containerize the application using Docker and deploy on a Kubernetes cluster to allow for efficient scaling, self-healing, and manageability.

5. **Microservice Architecture**: Decomposing the application into microservices could potentially improve the scalability and reliability of the application. This way, each service could scale independently, thereby improving the overall performance of the application.

## Operating Environment

The program is implemented in a supported version of Java using the Spring framework. The specific version details, along with other environment and setup details, can be found in the program files.

Due to personal circumstances and work constraints, I was limited in the ways I could implement these improvements. These are areas for future development.

For the detailed technical design document for possible improvements, please refer to [Movie Rating Feature Technical Design Document (Revised)](TechnicalDesignDocument.md).
