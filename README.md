Overview
This project is a social media platform where users can interact with posts, follow other users, and perform actions such as liking posts, posting comments, and managing their profiles. The platform also implements real-time updates, JWT-based authentication, and robust authorization to ensure a secure and personalized user experience.

Features
User Authentication: Users can register and log in to the platform.
User Profile Management: Each user can update their profile, including username, bio, and profile picture.
Post Content: Users can create, edit, and delete posts.
Commenting & Liking: Users can comment on and like posts.
Following System: Users can follow and unfollow others to stay updated on their posts.
Search: Search for users and posts based on keywords.
Security: JWT-based authentication and role-based access control.
Pagination: Pagination and sorting for retrieving posts, users, and comments.
Tech Stack
Backend: Spring Boot 3.x
Database: MySQL
Authentication: JWT (JSON Web Token)
ORM: JPA (Hibernate)
API Documentation: Swagger/OpenAPI
Testing: JUnit 5 (Unit & Integration Tests)
Caching: (Optional, based on your implementation)
Project Setup
Prerequisites
Java 17 or higher
MySQL Server
Maven or Gradle (based on your project setup)
Step 1: Clone the Repository
bash
Copy code
git clone https://github.com/your-repo/social-media-platform.git
cd social-media-platform
Step 2: Configure MySQL Database
Ensure that you have MySQL installed and running. Create a database in MySQL:

sql
Copy code
CREATE DATABASE social_media_platform;
Step 3: Configure application.properties
Update the src/main/resources/application.properties file with your MySQL database credentials:

properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/social_media_platform
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
Step 4: Build and Run the Application
For Maven users:

bash
Copy code
mvn clean install
mvn spring-boot:run
For Gradle users:

bash
Copy code
./gradlew build
./gradlew bootRun
The application will start on http://localhost:8080.

Step 5: Access Swagger API Documentation
Once the application is running, you can access the Swagger UI to explore the API and test endpoints:

bash
Copy code
http://localhost:8080/swagger-ui.html
API Endpoints
1. User Authentication and Registration
POST /users/register: Register a new user.

Request body: { "username": "user1", "email": "user1@example.com", "password": "password123" }
Response: 201 Created on success.
POST /users/login: Login a user and generate a JWT token.

Request body: { "email": "user1@example.com", "password": "password123" }
Response: 200 OK with JWT token.
2. User Profile Management
GET /users/{id}: Retrieve a user profile by ID.

Response: 200 OK with user details.
PUT /users/{id}: Update a user profile.

Request body: { "username": "newUsername", "bio": "Updated bio" }
Response: 200 OK on success.
3. Post Management
POST /posts: Create a new post.

Request body: { "content": "This is a new post" }
Response: 201 Created with post details.
GET /posts: Retrieve all posts with pagination and sorting.

Query params: page=0&size=10&sort=date,desc
Response: 200 OK with a list of posts.
GET /posts/{id}: Retrieve a post by ID.

Response: 200 OK with post details.
PUT /posts/{id}: Update an existing post.

Request body: { "content": "Updated post content" }
Response: 200 OK on success.
DELETE /posts/{id}: Delete a post.

Response: 204 No Content on success.
4. Comment Management
POST /posts/{id}/comments: Add a comment to a post.
Request body: { "content": "Great post!" }
Response: 201 Created with comment details.
5. Like Management
POST /posts/{id}/like: Like a post.
Response: 200 OK on success.
6. Follow Management
POST /users/{id}/follow: Follow a user.

Response: 200 OK on success.
GET /users/{id}/followers: Retrieve a user's followers.

Response: 200 OK with a list of followers.
GET /users/{id}/following: Retrieve users followed by a user.

Response: 200 OK with a list of following.
7. Search Functionality
POST /posts/search: Search for posts based on keywords in the content.

Request body: { "keyword": "funny" }
Response: 200 OK with a list of posts.
POST /users/search: Search for users based on keywords in the username, email, or bio.

Request body: { "keyword": "john" }
Response: 200 OK with a list of users.
Security
JWT Authentication: All endpoints (except for login and registration) are protected by JWT authentication.
Authorization: Users can only update or delete their own posts. Unauthorized access will result in a 403 Forbidden error.
