import org.gradle.tooling.GradleConnector
import java.util.concurrent.*

plugins {
    application
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.5.3")
    implementation("io.ktor:ktor-server-cio:1.5.3")
    implementation("io.ktor:ktor-serialization:1.5.3")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.google.zxing:javase:3.4.1")
    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileKotlin {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

application {
    mainClass.set("AppKt")
}

// one task that does both the continuous compile and the run
tasks.create("dev") {
    doLast {
        fun fork(task: String, vararg args: String): Future<*> {
            return Executors.newSingleThreadExecutor().submit {
                GradleConnector.newConnector()
                        .forProjectDirectory(project.projectDir)
                        .connect()
                        .use {
                            it.newBuild()
                                    .addArguments(*args)
                                    .setStandardError(System.err)
                                    .setStandardInput(System.`in`)
                                    .setStandardOutput(System.out)
                                    .forTasks(task)
                                    .run()
                        }
            }
        }

        val classesFuture = fork("classes", "-t")
        val runFuture = fork("run")

        classesFuture.get()
        runFuture.get()
    }
}

defaultTasks("dev")

tasks.replace("assemble").dependsOn("installDist")

tasks.create("stage").dependsOn("installDist")
