plugins {
	kotlin("jvm") version "2.1.0"
	kotlin("kapt") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
	kotlin("plugin.jpa") version "2.1.0"
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
	id("nu.studer.jooq") version "9.0"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		jvmToolchain(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}

dependencies {
	// Kotlin
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

    // infra
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("org.redisson:redisson-spring-boot-starter:3.45.1")
	// Kafka
	implementation("org.springframework.kafka:spring-kafka")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testImplementation("org.awaitility:awaitility:4.2.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	dependencies {
		testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
		testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	}
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:kafka")

	// Swagger (springdoc-openapi)
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

	// Bean Validation (Jakarta)
	implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")
	implementation("org.glassfish:jakarta.el:4.0.2")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}

tasks.test {
	outputs.upToDateWhen { false }
}
