# Unisalle App – Database Course Final Assignment

This is a full-stack application developed for the database course final assignment, demonstrating a comprehensive project setup with a modern tech stack.

---

## 🚀 Key Technologies

* **Frontend**: Angular (served via Nginx)
* **Backend**: Spring Boot
* **Database**: MongoDB

All components are containerized with Docker for straightforward deployment and enhanced portability.

---

## 🐳 Getting Started

To get this application up and running, follow these simple steps:

### Prerequisites

Make sure you have [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running on your system. This includes Docker Engine and Docker Compose, which are essential for managing the application's containers.

### Running the Application

1.  **Open your terminal or command prompt.**
2.  **Navigate to the project's root directory** where the `docker-compose.yml` file is located.
3.  **Execute the following command** to build and start all services:

    ```bash
    docker-compose up --build
    ```

    * The `--build` flag is important for the first run or if you've made changes to the `Dockerfile`s, ensuring all images are built before starting the containers.
    * This command will download necessary images (like MongoDB, Nginx, etc.) and build your Angular and Spring Boot application images.

### Accessing the Application

Once all services are running, you can access the application:

* **Frontend**: Typically available at `http://localhost:80` (or `http://localhost` if port 80 is the default for Nginx).
* **Backend API**: Usually exposed on a different port; check your Spring Boot configuration (often `http://localhost:8080`).
* **MongoDB**: Accessible internally within the Docker network.

*Note: Specific port numbers might vary based on your `docker-compose.yml` configuration.*

---

## 🛑 Stopping the Application

To stop all running containers and remove the networks created by `docker-compose`:

```bash
docker-compose down
```


## Authors
* ** Emerson Tavera Quiroz
* ** Karen Alejandra Torres
