.PHONY: run build check clean generate-java generate-xml

run:
	./gradlew run

build:
	./gradlew build

check:
	./gradlew check

clean:
	./gradlew clean

generate-xml:
	rm -f generator/src/main/resources/xml/desktop.xml
	./gradlew :generator:run --args="generate-xml"

generate-java:
	rm -rf sdk/src/main/generated
	./gradlew :generator:run --args="generate-java"
