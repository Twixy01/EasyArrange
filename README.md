# EasyArrange

Database connection setup

## 1. Tomcat setup
```text
Add it at the run configuration:
Tomcat Local Server - 10.1.50 version
```
## 2. Add the context.xml in webapp/META-INF:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context path="/backend">
   <Resource name="jdbc/easyarrange"
      auth="Container"
      type="javax.sql.DataSource"
      username="root"
      password=""
      driverClassName="org.mariadb.jdbc.Driver"
      url="jdbc:mariadb://localhost:3306/easyarrange"
      maxActive="8"
      maxIdle="4"/>
</Context>

```
## 3. Code for connecting to database in Servlets:
```java
Context initCtx = null;
Connection conn = null;
try {
        initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource)envCtx.lookup("jdbc/easyarrange");
        conn = ds.getConnection();

        conn.close();
} 
```
then -> add the catch statements
