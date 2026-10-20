# Flipkart E-Commerce Automation Suite

[![Java 11](https://img.shields.io/badge/Java-11-ED8B00?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Selenium WebDriver 4.21.0](https://img.shields.io/badge/Selenium_WebDriver-4.21.0-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![TestNG 7.7.0](https://img.shields.io/badge/TestNG-7.7.0-FF6C37?logo=testng&logoColor=white)](https://testng.org/)
[![Gradle 7.5.1](https://img.shields.io/badge/Gradle-7.5.1-02303A?logo=gradle&logoColor=white)](https://gradle.org/)

An automated end-to-end testing suite for complex e-commerce user workflows on Flipkart. This project showcases the **Facade / Wrapper Design Pattern**, dynamic DOM traversal across obfuscated and minified React nodes, resilient multi-attribute XPath locators, and automated data aggregation and sorting.

---

## Architecture & Design Patterns

### 1. Wrapper / Facade Pattern
Direct `WebDriver` interactions are decoupled into dedicated static utility methods (`Wrappers.java`). This isolates browser synchronization, DOM polling, scroll-into-view management, and exception handling from test execution flows, ensuring declarative and flake-free test code.

### 2. Resilient Interaction Strategies
* **Obfuscated Class Resilience:** Flipkart dynamically obfuscates its styling classes (e.g., `_1AtVbE`, `KzDlHZ`). Locators use normalized multi-fallback XPath expressions targeting semantic hierarchy, data attributes, and relative sibling/ancestor axes.
* **Two-Tier Event Dispatching:** Combines standard Selenium user actions with an automatic JavaScript execution fallback (`arguments[0].click()`) whenever elements are obscured by sticky navigation headers or animated promo banners.
* **React Node Normalization:** Employs `contains(., ...)` string evaluation instead of rigid child text node queries, bypassing DOM fragmentation caused by React client-side rendering.
* **Charset & Encoding Agnostic Locators:** Uses structural token matching (`contains(text(), '4') and contains(text(), 'above')`) to avoid Windows/Linux UTF-8 character mismatch failures on symbols like `★`.

### 3. Dynamic Data Extraction & Aggregation
* **Regex Sanitization:** Strips currency symbols, commas, parentheses, and marketing text using regex (`[^\d]`) to convert raw text into comparable primitives (`int`, `double`).
* **Descending Data Sorting:** Ingests dynamic user review tallies into a `Set` to eliminate duplicates, normalizes them via `NumberFormat (Locale.US)`, and sorts via `Collections.reverseOrder()` to dynamically isolate top-$N$ products.
* **Bidirectional DOM Traversal:** Uses XPath axis chaining (`./ancestor::...//div`) to traverse upwards from isolated discount or rating badges to locate the matching parent card, title, and media source (`src`).

---

## Test Scenarios Covered

| Test Case | Target Flow | Strategy & Assertion |
| :--- | :--- | :--- |
| **`testCase01`** | **Washing Machine Search & Rating Count** | Searches for *"Washing Machine"*, sorts results by **Popularity**, polls the DOM for all rating tags, and calculates the total count of models with a rating $\le 4.0$. |
| **`testCase02`** | **iPhone Discount Thresholds** | Queries *"iPhone"*, parses discount percentage elements across search rows, filters for discounts $> 17\%$, and maps each discount to its respective product title. |
| **`testCase03`** | **Top 5 Reviewed Coffee Mugs** | Searches *"Coffee Mug"*, applies the **4★ & above** rating filter, parses all review counts into a sorted collection, and outputs the titles and image URLs of the top 5 highest-reviewed items. |

---

## Project Structure

```text
├── src
│   └── test
│       ├── java
│       │   └── demo
│       │       ├── wrappers
│       │       │   └── Wrappers.java       # Reusable action wrappers & data extractors
│       │       └── TestCases.java          # TestNG test scenarios & lifecycle hooks
│       └── resources
│           └── testng.xml                  # TestNG execution suite definition
├── build.gradle                            # Dependency management & checkstyle rules
├── gradle.properties                       # TLS protocols and JVM configuration
├── gradlew                                 # Gradle wrapper script (Unix)
├── gradlew.bat                             # Gradle wrapper script (Windows)
└── README.md                               # Project documentation
