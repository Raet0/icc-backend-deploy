plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ec.edu.ups.icc"
version = "0.0.1-SNAPSHOT"
description = "Portafolio Backend"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}
dependencies {
    implementation("com.google.api-client:google-api-client:2.4.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    // API REST
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Validaciones (@NotBlank, @Email, etc.)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // JPA + Hibernate
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Seguridad
    implementation("org.springframework.boot:spring-boot-starter-security")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    // Email
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // Swagger / OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // PDF y Excel para reportes
    implementation("com.github.librepdf:openpdf:1.3.31")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

    // Driver para Neon (PostgreSQL)
    runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    // Dev y testing
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}
tasks.withType<Test> {
    useJUnitPlatform()
}