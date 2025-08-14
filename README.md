# Library Management API

A simple, extensible RESTful API for managing books and borrowers in a library system.
Built with Spring Boot, Java 17, and Docker.

---

## Features

- Register new borrowers (unique email).
- Register new books (ISBN-based, supports multiple copies).
- Borrow and return books (one copy per borrower at a time, prevents double-borrow).
- ISBN number uniquely identifies a book in the following way:
  * 2 books with the same title and same author but different ISBN numbers are considered as different books.
  * 2 books with the same ISBN numbers must have the same title and same author.
  * Multiple copies of books with same ISBN number are allowed in the system
- List all books with borrowing status.
- List all registered borrowers.
- Clear validation and error handling (friendly 404s).
- Interactive API documentation with Swagger (OpenAPI 3).
- Completely portable via Docker.

---

## Tech Stack

- Java 17
- Spring Boot 3
- Gradle
- H2 (in-memory database, can be swapped for Postgres/MySQL)
- Docker (optional, for containerized usage)

---

## Getting Started

### Prerequisites

- [Java 17+ JDK](https://www.oracle.com/java/technologies/javase/jdk17-0-13-later-archive-downloads.html) (for direct build/run)
- [Docker](https://docs.docker.com/get-docker/) (for containerized usage)
- [Git](https://git-scm.com/)

### Clone the Repository

```sh
git clone https://github.com/cynic123/Library-Management.git

cd Library-Management
```

### Running The App
**Option 1**: With Docker (Recommended), No need for Java/Gradle installed!

```sh
docker build -t library-api .

docker run -p 8080:8080 library-api
```

**Option 2**: Locally with Gradle

```sh
./gradlew clean build

./gradlew bootRun
```
App will be available at http://localhost:8080/library/

---

## API Endpoints
| **Method** | **Endpoint** | **Description** |
| :------- | :------ | :------- |
| POST	| /library/borrowers  | Register a new borrower |
| GET	| /library/borrowers | List all borrowers |
| POST	| /library/books | Register a new book |
| GET	| /library/books	| List all books |
| POST	| /library/borrow/{borrowerId}/book/{bookId}	| Borrow a book by ID |
| POST	| /library/return/{borrowerId}/book/{bookId}	| Return a borrowed book |

---

## API Documentation
Visit http://localhost:8080/swagger-ui.html
(Or sometimes swagger-ui/index.html) for full interactive OpenAPI docs.

---

## Error Handling
- Returns 404 with error reason if a book/borrower is not found.
- Returns 400 for validation errors or bad requests.

**Example**:
```json
{
  "error": "Book not found"
}
```

---

## Environment Configuration
- Uses H2 in-memory database by default (great for quick testing).
- To use Postgres/MySQL, set environment variables like:

```
SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
```

---

## Contributing
Pull requests welcome! Please open an issue first to discuss major changes.

---

## Author
**Prithwish Samanta**
- https://www.linkedin.com/in/prithwish-samanta-9750574a/
- https://github.com/cynic123

