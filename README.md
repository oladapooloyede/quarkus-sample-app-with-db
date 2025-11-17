# Quarkus Sample Application

A Quarkus REST API application with PostgreSQL database integration and a multistage Docker build setup.

## Project Structure

```
quarkus-sample-app/
├── src/
│   └── main/
│       ├── java/com/example/
│       │   ├── entity/
│       │   │   └── Product.java
│       │   ├── repository/
│       │   │   └── ProductRepository.java
│       │   ├── resource/
│       │   │   └── ProductResource.java
│       │   ├── GreetingResource.java
│       │   └── Greeting.java
│       └── resources/
│           ├── application.properties
│           └── import.sql
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── .dockerignore
```

## Features

- RESTful API endpoints
- PostgreSQL database integration
- Hibernate ORM with Panache
- Health checks
- Multistage Docker build (JVM and Native build support)
- JSON serialization
- Bean validation
- Docker Compose for easy deployment

## API Endpoints

### Greeting Endpoints
- `GET /hello` - Returns a plain text greeting
- `GET /hello/{name}` - Returns a JSON greeting with the provided name

### Product Endpoints
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/search?name={name}` - Search products by name
- `GET /api/products/available` - Get products with quantity > 0
- `GET /api/products/price-range?min={min}&max={max}` - Get products by price range
- `GET /api/products/count` - Get total product count
- `POST /api/products` - Create a new product
- `PUT /api/products/{id}` - Update an existing product
- `DELETE /api/products/{id}` - Delete a product

### Health Check
- `GET /health` - Health check endpoint

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- PostgreSQL 15+ (or use Docker Compose)

### Option 1: Using Docker Compose (Recommended)

The easiest way to run the application with PostgreSQL:

```bash
cd quarkus-sample-app
docker-compose up --build
```

This will:
- Start a PostgreSQL container
- Build and start the Quarkus application
- Initialize the database with sample data
- Make the app available at http://localhost:8080

To stop:
```bash
docker-compose down
```

To stop and remove volumes:
```bash
docker-compose down -v
```

### Option 2: Development Mode with Local PostgreSQL

1. Start PostgreSQL locally and create database:
```bash
createdb quarkusdb
```

2. Run in development mode:
```bash
mvn quarkus:dev
```

The application will start on http://localhost:8080 with live reload enabled.

### Option 3: Manual Docker Build

```bash
# Build the Docker image
docker build -t quarkus-sample-app:latest .

# Run PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_DB=quarkusdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:15-alpine

# Run the application
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/quarkusdb \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  quarkus-sample-app:latest
```

## Testing the API

### Greeting Endpoints
```bash
# Plain text endpoint
curl http://localhost:8080/hello

# JSON endpoint
curl http://localhost:8080/hello/World
```

### Product Endpoints
```bash
# Get all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Search products by name
curl http://localhost:8080/api/products/search?name=laptop

# Get available products
curl http://localhost:8080/api/products/available

# Get products by price range
curl "http://localhost:8080/api/products/price-range?min=20&max=100"

# Count products
curl http://localhost:8080/api/products/count

# Create a new product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Wireless Headphones",
    "description": "Noise-cancelling wireless headphones",
    "price": 199.99,
    "quantity": 20
  }'

# Update a product
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gaming Laptop",
    "description": "High-performance gaming laptop with RTX GPU",
    "price": 1499.99,
    "quantity": 5
  }'

# Delete a product
curl -X DELETE http://localhost:8080/api/products/1

# Health check
curl http://localhost:8080/health
```

## Multistage Dockerfile Details

The Dockerfile uses a two-stage build process:

1. **Build Stage**: Uses Maven image to compile and package the application
2. **Runtime Stage**: Uses a minimal JVM runtime image to run the application

This approach:
- Reduces final image size significantly
- Keeps build tools out of the production image
- Improves security by minimizing the attack surface
- Enables better layer caching for faster builds

### Native Build Option

For even smaller image sizes and faster startup times, uncomment the native build sections in the Dockerfile:

```bash
# In Dockerfile, replace the JVM build with:
RUN mvn package -Pnative -DskipTests

# Then build and run
docker build -t quarkus-sample-app:native .
docker run -p 8080:8080 quarkus-sample-app:native
```

## Database Schema

The application automatically creates the following schema:

**Products Table:**
- `id` - Auto-generated primary key
- `name` - Product name (required)
- `description` - Product description
- `price` - Product price (required, decimal)
- `quantity` - Stock quantity (required)
- `created_at` - Timestamp when created
- `updated_at` - Timestamp when last updated

Sample data is automatically loaded from `import.sql` on startup.

## Configuration

### Application Properties

Database configuration can be modified in `src/main/resources/application.properties`:

```properties
# Database connection
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=${DB_USERNAME:postgres}
quarkus.datasource.password=${DB_PASSWORD:postgres}
quarkus.datasource.jdbc.url=${DB_URL:jdbc:postgresql://localhost:5432/quarkusdb}

# Hibernate settings
quarkus.hibernate-orm.database.generation=drop-and-create
quarkus.hibernate-orm.log.sql=true
```

### Environment Variables

The application supports the following environment variables:

- `DB_URL` - Database JDBC URL (default: `jdbc:postgresql://localhost:5432/quarkusdb`)
- `DB_USERNAME` - Database username (default: `postgres`)
- `DB_PASSWORD` - Database password (default: `postgres`)

## Technologies Used

- **Quarkus 3.6.4** - Supersonic Subatomic Java Framework
- **Hibernate ORM with Panache** - Simplified persistence layer
- **PostgreSQL** - Relational database
- **RESTEasy Reactive** - Reactive REST endpoints
- **Jackson** - JSON serialization/deserialization
- **Bean Validation** - Input validation
- **SmallRye Health** - Health check endpoints
- **Docker & Docker Compose** - Containerization
