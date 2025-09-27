# Spring Boot Todo API

A RESTful Todo application built with Spring Boot, featuring JWT-based authentication, user management, and CRUD operations for todo items. This application uses an in-memory H2 database and provides a secure API for managing personal todo lists.

## Features

- 🔐 **JWT Authentication**: Secure user authentication with JSON Web Tokens
- 👤 **User Management**: User registration and login functionality
- 📝 **Todo CRUD Operations**: Create, read, update, and delete todo items
- 🔒 **User-specific Data**: Each user can only access their own todos
- 🏃 **Token Blacklisting**: Secure logout functionality with token revocation
- 💾 **H2 Database**: In-memory database for development and testing
- 🛡️ **Spring Security**: Comprehensive security configuration

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.3**
- **Spring Security**: For authentication and authorization
- **Spring Data JPA**: For data persistence
- **H2 Database**: In-memory database
- **JWT (JSON Web Tokens)**: For stateless authentication
- **Maven**: Build and dependency management
- **Lombok**: For reducing boilerplate code

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/AR2030/spring-todo-api.git
   cd spring-todo-api/todo
   ```

2. **Build the project**
   ```bash
   ./mvnw clean compile
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Database Access

The application uses H2 in-memory database. You can access the H2 console at:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: *(leave empty)*

## API Endpoints

### Authentication

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "username": "your_username"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "your_username",
  "password": "your_password"
}
```

**Response**: `200 OK`
```json
"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5b3VyX3VzZXJuYW1lIiwiaWF0IjoxNjk5..."
```

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer <your_jwt_token>
```

**Response**: `200 OK`
```json
"Logged out successfully"
```

### Todo Management

All todo endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

#### Get All Todos
```http
GET /api/todos
Authorization: Bearer <your_jwt_token>
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "completed": false
  },
  {
    "id": 2,
    "title": "Build a REST API",
    "completed": true
  }
]
```

#### Create Todo
```http
POST /api/todos
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
  "title": "New todo item",
  "completed": false
}
```

**Response**: `200 OK`
```json
{
  "id": 3,
  "title": "New todo item",
  "completed": false
}
```

#### Update Todo
```http
PUT /api/todos/{id}
Authorization: Bearer <your_jwt_token>
Content-Type: application/json

{
  "title": "Updated todo item",
  "completed": true
}
```

**Response**: `200 OK`
```json
{
  "id": 1,
  "title": "Updated todo item",
  "completed": true
}
```

#### Delete Todo
```http
DELETE /api/todos/{id}
Authorization: Bearer <your_jwt_token>
```

**Response**: `200 OK`

## Configuration

### Application Properties

Key configuration options in `src/main/resources/application.properties`:

```properties
# Application name
spring.application.name=todo

# H2 Database configuration
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.path=/h2-console

# JPA/Hibernate configuration
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### Security Configuration

The application is configured with the following security settings:

- **Public endpoints**: `/api/auth/login`, `/api/auth/register`, `/h2-console/**`
- **Protected endpoints**: All `/api/todos/**` endpoints
- **Authentication**: JWT-based with HS256 signing algorithm
- **Token expiration**: 1 hour (configurable in `JwtService.java`)

## Project Structure

```
src/
├── main/
│   └── java/com/example/todo/
│       ├── TodoApplication.java          # Main application class
│       ├── config/
│       │   └── SecurityConfig.java       # Security configuration
│       ├── controller/
│       │   ├── AuthController.java       # Authentication endpoints
│       │   ├── TodoController.java       # Todo CRUD endpoints
│       │   └── UserController.java       # User management endpoints
│       ├── dto/
│       │   └── LoginRequest.java         # Login request DTO
│       ├── model/
│       │   ├── RevokedToken.java         # Token blacklist entity
│       │   ├── Todo.java                 # Todo entity
│       │   └── User.java                 # User entity
│       ├── repository/
│       │   ├── RevokedTokenRepository.java
│       │   ├── TodoRepository.java
│       │   └── UserRepository.java
│       ├── security/
│       │   ├── JwtAuthFilter.java        # JWT authentication filter
│       │   └── JwtService.java           # JWT token management
│       └── service/
│           ├── TodoService.java          # Todo business logic
│           ├── TokenBlacklistService.java# Token blacklist management
│           └── UserService.java          # User management logic
└── test/
    └── java/com/example/todo/
        └── TodoApplicationTests.java     # Basic application tests
```

## Testing

Run the test suite using Maven:

```bash
./mvnw test
```

The application includes basic integration tests to ensure the Spring context loads properly.

## Development

### Running in Development Mode

1. Start the application:
   ```bash
   ./mvnw spring-boot:run
   ```

2. The application will automatically reload on code changes if using an IDE with Spring Boot DevTools.

3. Access the H2 console for database inspection at `http://localhost:8080/h2-console`

### API Testing

You can test the API using tools like:
- **Postman**: Import the endpoints and test with the provided examples
- **curl**: Use command-line requests with the examples above
- **HTTPie**: Alternative command-line HTTP client

#### Example curl commands:

```bash
# Register a user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Get todos (replace TOKEN with actual JWT)
curl -X GET http://localhost:8080/api/todos \
  -H "Authorization: Bearer TOKEN"

# Create a todo
curl -X POST http://localhost:8080/api/todos \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"title":"Learn Spring Security","completed":false}'
```

## Security Notes

⚠️ **Important Security Considerations:**

1. **JWT Secret**: The current implementation uses a hardcoded secret key. In production, use environment variables or a secure configuration management system.

2. **HTTPS**: Always use HTTPS in production to protect JWT tokens in transit.

3. **Token Storage**: Store JWT tokens securely on the client side (avoid localStorage for sensitive applications).

4. **Password Encoding**: Passwords are automatically encoded using BCrypt.

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built with Spring Boot framework
- JWT implementation using JJWT library
- H2 Database for easy development setup
- Lombok for reducing boilerplate code