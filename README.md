# Starter App

---

This is a starter application built with **Micronaut** using **Kotlin** and backed by **MongoDB**. It provides a 
foundational setup for developing reactive microservices with a NoSQL database.

## Technologies Used

* **Micronaut**
* **Kotlin**
* **MongoDB**

## Getting Started

### Prerequisites

* **Java Development Kit (JDK) 21**
* **MongoDB instance**: You can run MongoDB locally, use Docker, or connect to a cloud service.

### Running Locally

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd starter
    ```

2.  **Configure MongoDB:**
    Before running the application, ensure your MongoDB connection details are correctly configured. By default, 
3. Micronaut will look for a local MongoDB instance. You can modify the `application.yml` file in `src/main/resources` 
4. to point to your MongoDB instance:

    ```yaml
    # src/main/resources/application.yml
    mongodb:
      uri: mongodb://localhost:27017/starterdb
      database: starter
    ```
    *Replace `localhost:27017/starterdb` with your MongoDB connection string if it's different.*
    *Replace `starter` with your database name.*

3.  **Run the application:**
    You can run the application using Gradle:
    ```bash
    ./gradlew run
    ```
    Or, if you prefer to build a JAR first:
    ```bash
    ./gradlew build
    java -jar build/libs/starter-app.jar
    ```

    The application will start on `http://localhost:8080`.

## Project Structure

The project follows a standard Micronaut and Gradle structure:

* `src/main/kotlin`: Contains the main application source code.
* `src/main/resources`: Contains configuration files (e.g., `application.yml`).
* `build.gradle.kts`: Gradle build script for dependency management and project configuration.

## Contributing

---

Feel free to contribute to this starter app by opening issues or submitting pull requests.

## License

---

This project is open-source and available under the [MIT License](LICENSE).