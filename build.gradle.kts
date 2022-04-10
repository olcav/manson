

plugins {
    kotlin("jvm") version "1.5.10"
    id("io.gitlab.arturbosch.detekt").version("1.19.0")
    application
}

group = "com.manson"
version = "0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

detekt {
    toolVersion = "1.19.0"
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
    autoCorrect = true
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "1.8"
}

tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("com.jayway.jsonpath:json-path:2.6.0")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
