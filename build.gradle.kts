import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.sentry.jvm.gradle") version "4.3.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.9.0"
}
group = "br.com.lucas-cm.manga-easy"
version = "1.7.0"
java.sourceCompatibility = JavaVersion.VERSION_17

sentry {
    // Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
    // This enables source context, allowing you to see your source
    // code as part of your stack traces in Sentry.
    includeSourceContext.set(true)
    org.set("lucas-cm")
    projectName.set("manga_easy_micro_api_monolito")
    authToken.set(System.getenv("SENTRY_AUTH_TOKEN"))
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation("io.appwrite:sdk-for-kotlin:5.0.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.6")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.6")
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.6")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.8.0")
    implementation(enforcedPlatform("com.oracle.oci.sdk:oci-java-sdk-bom:3.43.2"))
    implementation("com.oracle.oci.sdk:oci-java-sdk-common-httpclient-jersey3:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-addons-apache-configurator-jersey3:3.39.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-core:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-identity:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-objectstorage:3.42.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.2.4")
    implementation("net.swiftzer.semver:semver:2.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.5.0")
    implementation("com.github.sonus21:rqueue-spring:3.1.1-RELEASE")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.bootJar {
    archiveFileName.set("application.jar")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Test>("test") {
    enabled = false
}