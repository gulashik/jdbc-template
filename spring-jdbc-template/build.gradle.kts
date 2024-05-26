import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"

	// flywayMigrate Task
	// ПРОБУЕМ РАЗНЫЕ ВЕРСИИ на совместимость
	// https://plugins.gradle.org/plugin/org.flywaydb.flyway/10.8.1
	id("org.flywaydb.flyway") version "9.22.3"
}

group = "org.gulash.kfk"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
	// jdbc
	implementation("org.springframework.boot:spring-boot-starter-jdbc")

	implementation("org.springframework.retry:spring-retry") // для возможности @Retryable

	// flyway
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql") // может не нужны потом проверить

	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// драйвер
	runtimeOnly("org.postgresql:postgresql") // может не нужны потом проверить

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// flywayMigrate Task. Настройки аналогичны как в application.properties
flyway {
	url = "jdbc:postgresql://localhost:5432/testbd"
	user = "postgres"
	password = "postgres"
	baselineOnMigrate = true
	locations = arrayOf("filesystem:db/migration/dev/postgresql")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
