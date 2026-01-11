# GitHub Repositories Proxy Service

The application fetches public, non-fork repositories for a given GitHub user and enriches the data with branch information.

## 🚀 Key Features

- **Automated Filtering**: Automatically filters out all fork repositories.
- **Data Enrichment**: For each repository, it lists all branches with their latest commit SHA.
- **Standardized Error Handling**: Provides a clean, required JSON format for 404 errors.
- **Proxy Architecture**: Lightweight implementation focused on data transformation without unnecessary overhead.

## 🛠 Tech Stack

- **Java 25**
- **Spring Boot 4.0.1** (WebMVC & RestClient)
- **Gradle Kotlin**
- **WireMock** 

## 📐 Architecture & Compliance

Following the specific requirements provided by Adam Popławski:
- **Minimalism**: No WebFlux, Security, Cache, or Resilience layers.
- **Single Package Structure**: All classes reside in `com.task.atiperatask` for maximum simplicity and correct use of package-private encapsulation.
- **Direct Modeling**: Minimized number of models; application acts as a direct proxy using Java Records.
- **Integration Focused**: Testing is done via WireMock without mocks to emulate real-world API behavior.

## 🏃 Getting Started

### Prerequisites
- JDK 25
- Gradle (included wrapper)

### Installation & Running
1. Clone this repository.
2. Build and run the application:
   ```bash
   ./gradlew bootRun

## 📖 API Usage

## Get User Repositories

Returns a list of **non-fork repositories** for a given username.

- **Endpoint:** GET `/api/{username}/repositories`

### Sample Response (200 OK):
````
[
    {
        "name": "bilet-system",
        "owner": "WilinskiW",
        "branches": [
            {
                "name": "deployment",
                "sha": "9f7497177c7bc04daeb8d7ebb7e1e772809f8b35"
            },
            {
                "name": "gh-pages",
                "sha": "9a532c02ba077be4a27e3ec4294008a99391b1b2"
            },
            {
                "name": "master",
                "sha": "7401f8b46f4ead6ee9842517754fb9ec4a32e136"
            }
        ]
    
]
````

### ⚠️ Error Handling

If a GitHub user is not found, the API returns a 404 Not Found status with the following body:
````
{
    "message": "Not Found",
    "status": 404
}
````

