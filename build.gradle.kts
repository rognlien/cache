import org.gradle.api.publish.PublishingExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.9.20"
    id("java")
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("maven-publish")
}

group = "com.rognlien"
version = "0.0.1-SNAPSHOT"

val baseVersion = property("baseVersion")
val buildNumber: String? = System.getenv("BUILD_NUMBER")
version = buildNumber?.let { "$baseVersion.$buildNumber" } ?: "$baseVersion.local"

repositories {
    mavenCentral()
}

/*
configure<PublishingExtension> {
    publications {
        create<MavenPublication>("default") {
            groupId = "com.rognlien"
            artifactId = "cache"
            from(components["java"])
            // Include any other artifacts here, like javadocs
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rognlien/" + rootProject.name)
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

 */

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlin {
        jvmToolchain(17)
    }
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/rognlien/cache")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}
