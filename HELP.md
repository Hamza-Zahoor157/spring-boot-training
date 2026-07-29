# Getting Started

## OAuth2 Setup (Google & GitHub Login)

The application supports OAuth2 login with Google and GitHub. If you see
`Error 401: invalid_client` / `The OAuth client was not found` when clicking
"Sign in with Google", it means the OAuth credentials are not configured.

### 1. Create OAuth Credentials

**Google:**

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a project or select an existing one
3. Navigate to **APIs & Services → Credentials**
4. Click **Create Credentials → OAuth client ID**
5. Application type: **Web application**
6. Add authorized redirect URI: `http://localhost:8080/login/oauth2/code/google`
7. Copy the **Client ID** and **Client Secret**

**GitHub:**

1. Go to [GitHub Developer Settings](https://github.com/settings/developers)
2. Click **New OAuth App**
3. Homepage URL: `http://localhost:8080`
4. Authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
5. Copy the **Client ID** and **Client Secret**

### 2. Configure Environment Variables

Copy `.env.example` to `.env` and fill in your credentials:

```bash
cp .env.example .env
# Edit .env with your real credentials
```

The `.env` file is gitignored and should never be committed.

### 3. Run the Application

**Option A - Using the run script (recommended):**

```bash
./run.sh
```

**Option B - Manually set env vars and run:**

```bash
export GOOGLE_CLIENT_ID=your-client-id
export GOOGLE_CLIENT_SECRET=your-client-secret
export GITHUB_CLIENT_ID=your-client-id
export GITHUB_CLIENT_SECRET=your-client-secret
./mvnw spring-boot:run
```

**Option C - From IntelliJ IDEA:**
The run configuration in `.idea/workspace.xml` already has the env vars set. Just run the
`Lecture02Application` configuration.

### 4. Verify OAuth Redirect URI

Make sure the redirect URI in `application.yaml` matches the one registered in Google/GitHub:

- Google: `http://localhost:8080/login/oauth2/code/google`
- GitHub: `http://localhost:8080/login/oauth2/code/github`

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.0.7/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.0.7/maven-plugin/build-image.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.0.7/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Web](https://docs.spring.io/spring-boot/4.0.7/reference/web/servlet.html)
* [Liquibase Migration](https://docs.spring.io/spring-boot/4.0.7/how-to/data-initialization.html#howto.data-initialization.migration-tool.liquibase)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.0.7/reference/actuator/index.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM. While most of
the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from
the parent. To prevent this, the project POM contains empty overrides for these elements. If you
manually switch to a different parent and actually want the inheritance, you need to remove those
overrides.

