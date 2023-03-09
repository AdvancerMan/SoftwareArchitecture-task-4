plugins {
    id("java")
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.spring") version "1.6.21"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.reactivex:rxjava:1.2.7")
    implementation("org.mongodb:mongo-java-driver:3.6.4")
    implementation("org.mongodb:mongodb-driver-rx:1.5.0")
    implementation("io.reactivex:rxnetty-http:0.5.3")
    implementation("io.reactivex:rxnetty-common:0.5.2")
    implementation("io.reactivex:rxnetty-tcp:0.5.2")
    implementation("io.netty:netty-all:4.1.82.Final")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}
