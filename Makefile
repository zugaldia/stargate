export GRADLE_OPTS = --enable-native-access=ALL-UNNAMED

.PHONY: run build check clean publish-local generate-java

run:
	./gradlew :app:run

build:
	./gradlew build

check:
	./gradlew check

clean:
	./gradlew clean

publish-local:
	./gradlew :sdk:publishToMavenLocal

publish-central:
	./gradlew :sdk:publishToMavenCentral

generate-java:
	rm -rf sdk/src/main/generated
	./gradlew :generator:run --args="generate-java"
