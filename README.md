# Simatiusu - Login and SignUp System

A modern Java Swing application with complete login and signup functionality.

## Features

- **Login Form**: User authentication with username and password
- **Sign Up Form**: New user registration with validation
- **Database Integration**: MySQL database for user management
- **Password Security**: BCrypt password hashing
- **Input Validation**: 
  - Email format validation
  - Password strength (minimum 6 characters)
  - Password confirmation matching
  - Duplicate username/email checking
- **Modern UI**: Responsive design with GridBagLayout
- **Error Handling**: Comprehensive error messages and validation

## Requirements

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Setup Instructions

### 1. Database Setup

Run the SQL script to create the database and tables:

```bash
mysql -u root -p < database_schema.sql
```

Or manually execute the SQL commands in `database_schema.sql`.

### 2. Database Configuration

Edit `src/main/java/loginandsignup/DatabaseConnection.java` to configure your database connection:

```java
private static final String URL = "jdbc:mysql://localhost:3306/simatiusu_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

### 3. Build the Project

```bash
mvn clean compile
```

### 4. Run the Application

```bash
mvn exec:java -Dexec.mainClass="loginandsignup.LoginAndSignUp"
```

Or run directly from your IDE by executing the `LoginAndSignUp.java` main class.

## Project Structure

```
src/main/java/loginandsignup/
├── Login.java              - Login form UI
├── SignUp.java             - Sign up form UI
├── LoginAndSignUp.java     - Main application entry point
├── DatabaseConnection.java - Database operations
└── User.java               - User model class

database_schema.sql         - Database schema
pom.xml                     - Maven configuration
```

## Dependencies

- MySQL Connector/J 8.0.33
- jBCrypt 0.4 (for password hashing)

## Usage

1. **First Time Users**: Click "Sign Up" to create a new account
2. **Existing Users**: Enter your username and password to login
3. **Password Security**: All passwords are securely hashed using BCrypt

## Validation Rules

- **Email**: Must be a valid email format
- **Password**: Minimum 6 characters
- **Username**: Must be unique
- **Email**: Must be unique
- All fields are required

## Screenshots

(Screenshots will be added after running the application)

## License

This project is open source and available under the MIT License.
