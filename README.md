# 🎬 Cinema Seat

[![version](https://img.shields.io/badge/version-1.0.1-yellow.svg)](https://semver.org)
[![Build](https://github.com/rkociniewski/cinema-seat/actions/workflows/main.yml/badge.svg)](https://github.com/rkociniewski/cinema-seat/actions/workflows/main.yml)
[![codecov](https://codecov.io/gh/rkociniewski/cinema-seat/branch/main/graph/badge.svg)](https://codecov.io/gh/rkociniewski/cinema-seat)
[![Java](https://img.shields.io/badge/JDK-21-white?logo=openjdk)](https://www.java.com/)
[![Gradle](https://img.shields.io/badge/Gradle-8.14.1-blue?logo=gradle)](https://gradle.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-greem.svg)](https://opensource.org/licenses/MIT)

A minimal Java-based simulation of a cinema seat reservation system with concurrency support and test coverage.

## 📦 Features

- Reserve and cancel seats by seat number and client ID.
- Prevent double booking or cancellation by unauthorized users.
- Thread-safe operations using synchronization.
- Extensive unit and concurrency tests.
- JaCoCo-based code coverage enforcement.
- Includes smoke tests for `Main` and logic validation for `ClientTask`.

## 📁 Project Structure

```

src
├── main
│   └── java
│       └── rk
│           └── cinema
│               ├── Main.java
│               └── model
│                   ├── Cinema.java
│                   ├── ClientTask.java
│                   └── error
│                       └── IllegalSeatReservedException.java
└── test
└── java
└── rk
└── cinema
├── MainTest.java
└── model
├── CinemaTest.java
├── ConcurrentCinemaTest.java
└── ClientTaskTest.java

````

## 🚀 How to Run

Compile and run the simulation:

```bash
./gradlew run
````

## ✅ Running Tests

To execute all tests:

```bash
./gradlew test
```

To generate the JaCoCo coverage report:

```bash
./gradlew jacocoTestReport
```

View the report at:
`build/reports/jacoco/test/html/index.html`

## 🧪 Coverage Verification

This project enforces minimum code coverage via `jacocoTestCoverageVerification`.
To verify and fail on low coverage:

```bash
./gradlew jacocoTestCoverageVerification
```

## 📜 License

This project is open-source and available under the [MIT License](https://opensource.org/licenses/MIT).
