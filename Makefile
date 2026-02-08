export GRADLE_OPTS = --enable-native-access=ALL-UNNAMED

.PHONY: run build check clean publish-local generate-java generate-xml

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

generate-xml:
	rm -f generator/src/main/resources/xml/desktop.xml
	./gradlew :generator:run --args="generate-xml"

generate-java:
	rm -rf sdk/src/main/generated
	./gradlew :generator:run --args="generate-java"
