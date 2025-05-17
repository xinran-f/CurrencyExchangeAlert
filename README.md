# CurrencyExchangeAlert

This project tracks currency exchange rate alerts. It consists of a Spring Boot backend and a React frontend.

## Prerequisites

* **Java 17** (JDK 17)
* **Maven**
* **Node.js** (>=14) & **npm**
* **MySQL** (or compatible)

## 1. Database Setup

1. Start MySQL and log in:

   ```bash
   mysql -u root -p
   ```

2. In the MySQL prompt, run the schema script to create the database and tables:

   ```sql
   SOURCE path/to/schema.sql;
   ```

   (or copy-paste the contents of `schema.sql`).

3. (Optional) Seed a user for testing:

   ```sql
   INSERT INTO users (username, `password`, email)
     VALUES ('alice','secret','alice@example.com');
   ```

## 2. Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/exchange_tracker?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=<YOUR_DB_PASSWORD>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

currencyfreaks.api.url=https://api.currencyfreaks.com/latest
currencyfreaks.api.key=<YOUR_CURRENCYFREAKS_API_KEY>
```

Replace `<YOUR_DB_PASSWORD>` and `<YOUR_CURRENCYFREAKS_API_KEY>` with your values.

## 3. Run Backend

From the project root:

```bash
mvn clean install
mvn spring-boot:run
```

The backend will start on **[http://localhost:8080](http://localhost:8080)**.

## 4. Run Frontend (Development)

In a separate terminal:

```bash
cd frontend
npm install
npm start
```

The React dev server runs on **[http://localhost:3000](http://localhost:3000)** and proxies API calls to the backend.

## 5. Production Build

1. Build the React app:

   ```bash
   cd frontend
   npm run build
   ```
2. Copy build output into Spring Boot static resources:

   ```bash
   cp -r frontend/build/* src/main/resources/static/
   ```
3. Rebuild and run the Spring Boot jar:

   ```bash
   mvn clean package
   java -jar target/CurrencyExchangeAlert-0.0.1-SNAPSHOT.jar
   ```

Now the combined app is served from **[http://localhost:8080](http://localhost:8080)**.

## 6. Testing Endpoints

Use cURL or Postman:

* **Create user**:

  ```bash
  curl -X POST http://localhost:8080/api/users \
    -H 'Content-Type: application/json' \
    -d '{"username":"alice","password":"secret","email":"alice@example.com"}'
  ```

* **Create alert**:

  ```bash
  curl -X POST http://localhost:8080/api/alerts \
    -H 'Content-Type: application/json' \
    -d '{"userId":1,"baseCurrency":"USD","targetCurrency":"EUR","targetRate":1.05}'
  ```

* **List alerts**:

  ```bash
  curl http://localhost:8080/api/alerts
  ```

* **Delete alert**:

  ```bash
  curl -X DELETE http://localhost:8080/api/alerts/1
  ```

* **Lookup rate**:

  ```bash
  curl http://localhost:8080/api/rate?base=USD&target=EUR
  ```
