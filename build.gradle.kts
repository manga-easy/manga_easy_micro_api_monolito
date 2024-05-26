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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.5")
    implementation("com.google.firebase:firebase-admin:9.2.0")
    implementation("io.appwrite:sdk-for-kotlin:2.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.8.0")
    implementation(enforcedPlatform("com.oracle.oci.sdk:oci-java-sdk-bom:3.41.1"))
    implementation("com.oracle.oci.sdk:oci-java-sdk-common-httpclient-jersey3")
    implementation("com.oracle.oci.sdk:oci-java-sdk-addons-apache-configurator-jersey3")
    implementation("com.oracle.oci.sdk:oci-java-sdk-core")
    implementation("com.oracle.oci.sdk:oci-java-sdk-identity")
    implementation("com.oracle.oci.sdk:oci-java-sdk-objectstorage")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("net.swiftzer.semver:semver:2.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.5.0")
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

gradle.beforeProject {
    // Set a default value
    project.ext.set("hasTests", false)
}