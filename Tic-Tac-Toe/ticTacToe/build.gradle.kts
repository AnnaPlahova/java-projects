plugins {
    java
    id("org.springframework.boot") version "4.0.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(18)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server") // Для JWT-валидации
    implementation("io.jsonwebtoken:jjwt-api:0.13.0") // Библиотека для работы с JWT
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0") // Реализация для JWT
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0") // Поддержка Jackson (для сериализации)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
