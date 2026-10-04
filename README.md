[![Sales Management System (CI included) | © 2026](https://github.com/eternals2346/sales-management-system/actions/workflows/maven.yml/badge.svg)](https://github.com/eternals2346/sales-management-system/actions/workflows/maven.yml)

# Sales Management System Repository

This repository demonstrates Unit Testing and Continuous Integration (CI) for a Java-based Sales Management System.

## Features & Implementation
- **Core Entities:** `Product` and `SalesService` handling business calculations (Subtotal, Discount, Shipping, Total, Customer Classification).
- **Unit Testing:** Comprehensive test suite written with **JUnit 5** (Parameterized Tests with `@CsvSource`, boundary value analysis, exception handling with `assertThrows`).
- **Code Coverage:** Configured with **JaCoCo Maven Plugin** (achieving 100% line & branch coverage).
- **Continuous Integration (CI):** Automated build, test, and packaging via **GitHub Actions**.
- **Deliverable:** Downloadable `.jar` artifact generated automatically on every push to `main`.
