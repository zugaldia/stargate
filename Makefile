APP_ID = com.zugaldia.Stargate
export GRADLE_OPTS = --enable-native-access=ALL-UNNAMED

.PHONY: run build check clean shadow-build shadow-run \
	flatpak-sources flatpak-linter flatpak-build flatpak-bundle flatpak-run \
	publish-local generate-java

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

flatpak-linter:
	flatpak run --command=flatpak-builder-lint org.flatpak.Builder appstream data/$(APP_ID).metainfo.xml.in
	flatpak run --command=flatpak-builder-lint org.flatpak.Builder manifest $(APP_ID).yml

flatpak-build:
	rm -f stargate.flatpak
	flatpak-builder --force-clean --user --install-deps-from=flathub --repo=repo --install builddir $(APP_ID).yml

flatpak-bundle:
	flatpak build-bundle repo stargate.flatpak $(APP_ID) --runtime-repo=https://flathub.org/repo/flathub.flatpakrepo

flatpak-run:
	flatpak run $(APP_ID)

publish-local:
	./gradlew :sdk:publishToMavenLocal

publish-central:
	./gradlew :sdk:publishToMavenCentral

generate-java:
	rm -rf sdk/src/main/generated
	./gradlew :generator:run --args="generate-java"
