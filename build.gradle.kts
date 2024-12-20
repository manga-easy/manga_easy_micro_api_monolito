import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.9.0"
}
group = "br.com.lucas-cm.manga-easy"
version = "1.7.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.3.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.0")
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation("io.appwrite:sdk-for-kotlin:2.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.6")
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.6")
    implementation("org.springframework.boot:spring-boot-starter-security:3.2.6")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:3.3.0")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.3.0")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.3")
    implementation(enforcedPlatform("com.oracle.oci.sdk:oci-java-sdk-bom:3.44.1"))
    implementation("com.oracle.oci.sdk:oci-java-sdk-common-httpclient-jersey3:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-addons-apache-configurator-jersey3:3.39.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-core:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-identity:3.42.0")
    implementation("com.oracle.oci.sdk:oci-java-sdk-objectstorage:3.42.0")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.1")
    implementation("net.swiftzer.semver:semver:2.0.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.6.0")
    implementation("com.github.sonus21:rqueue-spring:3.2.0-RELEASE")
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