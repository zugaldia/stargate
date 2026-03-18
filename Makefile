export GRADLE_OPTS = --enable-native-access=ALL-UNNAMED

.PHONY: run build check clean shadow-build shadow-run flatpak-sources publish-local generate-java

run:
	./gradlew :app:run

build:
	./gradlew build

check:
	./gradlew check

clean:
	./gradlew clean

shadow-build: clean
	./gradlew :app:shadowJar

shadow-run: shadow-build
	java --enable-native-access=ALL-UNNAMED -jar app/build/libs/stargate.jar

flatpak-sources:
	rm -f buildSrc/flatpak-sources.json sdk/flatpak-sources.json app/flatpak-sources.json
	./gradlew --project-dir buildSrc flatpakGradleGenerator --no-configuration-cache
	./gradlew :app:flatpakGradleGenerator :sdk:flatpakGradleGenerator --no-configuration-cache

publish-local:
	./gradlew :sdk:publishToMavenLocal

publish-central:
	./gradlew :sdk:publishToMavenCentral

generate-java:
	rm -rf sdk/src/main/generated
	./gradlew :generator:run --args="generate-java"
