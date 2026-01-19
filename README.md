# EasyArrange

Database connection setup

The application connects to the database using JDBC and a properties file.
All database-related configuration is externalized to ensure flexibility and security.
---
## 1. Loading database properties

The application loads the configuration from the classpath using the context class loader:
```java
Properties properties = new Properties();

try (InputStream inputStream = Thread.currentThread()
        .getContextClassLoader()
        .getResourceAsStream("db_connection.properties")) {

    properties.load(inputStream);

} catch (IOException e) {
    throw new RuntimeException(e);
}
```
---
## 2. Reading database parameters

The database connection parameters are retrieved from the loaded properties:
```java
final String DB_CONN = properties.getProperty("db_connection");
final String DB_USER = properties.getProperty("db_user");
final String DB_PASSWORD = properties.getProperty("db_password");
```
---
## 3. Establishing the JDBC connection

A JDBC connection is created using DriverManager.
The try-with-resources statement ensures that the connection is automatically closed.
```java
try (Connection connection =
         DriverManager.getConnection(DB_CONN, DB_USER, DB_PASSWORD)) {

    // Database operations go here

} catch (SQLException e) {
    throw new RuntimeException(e);
}
```
