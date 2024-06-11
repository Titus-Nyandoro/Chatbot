# Spring Boot Chatbot Project

Welcome to the Spring Boot Chatbot project! This application integrates multiple technologies and libraries to deliver a robust chatbot experience, including SMS interactions, PDF document reading, and natural language processing capabilities. Below is a detailed description of the project, including setup instructions, dependencies, and key features.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Configuration](#configuration)
- [Project Structure](#project-structure)
- [Key Features](#key-features)
- [API Endpoints](#api-endpoints)
- [Dependencies](#dependencies)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Prerequisites

- **Java 17**: Ensure that Java Development Kit (JDK) version 17 is installed on your machine.
- **Maven**: This project uses Maven for build and dependency management. Install Maven from [here](https://maven.apache.org/install.html).
- **Neo4j**: Download and set up a Neo4j database from [here](https://neo4j.com/download/).
- **OpenAI API Key**: Register and obtain an API key from [OpenAI](https://beta.openai.com/signup/).
- **Africastalking API Key**: Register and obtain your username and API key from [Africastalking](https://account.africastalking.com/).

## Setup Instructions

1. **Clone the repository**:
    ```bash
    git clone https://github.com/Titus-Nyandoro/Chatbot.git
    cd Chatbot
    ```

2. **Build the project**:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

## Configuration

1. **OpenAI API Key**:
    - Add your OpenAI API key to the `application.properties` file:
    ```properties
    openai.api.key=your_openai_api_key
    ```

2. **Africastalking Configuration**:
    - Add your Africastalking username and API key to the `application.properties` file:
    ```properties
    africastalking.username=your_username
    africastalking.api.key=your_api_key
    africastalking.shortcode=your_sms_shortcode
    ```

3. **Neo4j Database URL**:
    - Add your Neo4j database connection details to the `application.properties` file:
    ```properties
    neo4j.url=bolt://localhost:7687
    neo4j.username=your_neo4j_username
    neo4j.password=your_neo4j_password
    ```

4. **System Message Configuration**:
    - Define the system message by modifying the `system-message.txt` file located in the `src/main/resources` folder. This message will be used to guide the chatbot's responses.

5. **Load PDF Documents**:
    - Add important PDF documents to the `docs` folder located in `src/main/resources`. These documents will be parsed by the chatbot for information retrieval.

## Project Structure

The project follows a typical Spring Boot structure:

```plaintext
spring-boot-chatbot/
│
├── src/
│   ├── main/
│   │   ├── java/com/example/chatbot/
│   │   │   ├── config/      # Initial Configs
│   │   │   ├── controller/  # Controllers for handling API requests
│   │   │   ├── service/     # Service layer for business logic
│   │   │   ├── model/       # Domain models and DTOs
│   │   │   ├── repo/        # repositories
│   │   │   ├── service/     # Services for API processing
│   │   │   ├── utils/       # Necessary app utils
│   │   │   └── ChatbotApplication.java  # Main Spring Boot application class
│   │   └── resources/
│   │       ├── application.properties  # Configuration file
│   │       ├── system-message.txt  # System message for the chatbot
│   │       ├── docs/  # Folder for PDF documents
│   └── test/  # Unit and integration tests
│
└── pom.xml  # Maven build file
```

## Key Features

- **PDF Document Reader**: Integrates with Spring AI PDF Document Reader for document parsing.
- **Neo4j for Vector Database**: Utilizes Neo4j for efficient storage and retrieval of vector data.
- **OpenAI Integration**: Uses OpenAI for text embedding and completion.
- **SMS Interaction**: Implements SMS parsing and sending using the Africastalking library with a response limit of 144 characters.

## API Endpoints

### Send SMS
- **Endpoint**: `/send`
- **Method**: `POST`
- **Request Body**: `SmsRequest`
- **Response**: `SmsResponse`

```java
@PostMapping("/send")
public ResponseEntity<SmsResponse> SendSms(@RequestBody SmsRequest smsRequest) {
    log.warn("Send sms {}", smsRequest);
    return ResponseEntity.ok(smsService.sendSms(smsRequest));
}
```

### Receive SMS
- **Endpoint**: `/incoming`
- **Method**: `POST`
- **Consumes**: `application/x-www-form-urlencoded`
- **Request Body**: `IncomingSmsDTO`

```java
@PostMapping(value = "/incoming", consumes = "application/x-www-form-urlencoded")
public void processIncomingSms(@ModelAttribute IncomingSmsDTO smsPayload) {
    log.warn("Incoming sms {}", smsPayload);
    smsService.processIncomingSms(smsPayload);
}
```

## Dependencies

- **Spring Boot**: Core framework for building the application.
- **Maven**: For project build and dependency management.
- **Spring AI PDF Document Reader**: To handle PDF document reading.
- **Neo4j**: For vector database functionality.
- **OpenAI**: For embedding and text completion capabilities.
- **Africastalking**: For SMS functionalities limited to 144 characters.

### Key Dependencies in `pom.xml`
```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    
    <!-- Neo4j Dependency -->
    <dependency>
        <groupId>org.neo4j.driver</groupId>
        <artifactId>neo4j-java-driver</artifactId>
    </dependency>
    
    <!-- OpenAI Dependency -->
    <dependency>
        <groupId>com.openai</groupId>
        <artifactId>openai-java-client</artifactId>
    </dependency>
    
    <!-- Africastalking Library -->
    <dependency>
        <groupId>com.africastalking</groupId>
        <artifactId>africastalking-sms</artifactId>
    </dependency>
</dependencies>
```

## Usage

1. **Sending SMS**:
    - Send a POST request to `/send` with `SmsRequest` payload to send an SMS.
    - The system will respond with `SmsResponse`.

2. **Receiving SMS**:
    - Configure your SMS gateway to forward messages to `/incoming`.
    - The system will process incoming messages automatically.

3. **Loading PDF Documents**:
    - Ensure that the `docs` folder under `src/main/resources` is loaded with PDF files that contain important information. The application loops through these files to parse and use the content.

4. **Configuring System Message**:
    - Modify the `system-message.txt` file located in `src/main/resources` to set up a customized system message for the chatbot.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any enhancements or bug fixes. Ensure your code adheres to the project’s coding standards and includes relevant tests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

