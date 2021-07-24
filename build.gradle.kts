plugins {
    java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation( "org.xerial:sqlite-jdbc:3.34.0")
    implementation("mysql:mysql-connector-java:8.0.26")


}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}