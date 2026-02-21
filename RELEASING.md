# Releasing

## How versioning works

`baseVersion` in `sdk/build.gradle.kts` is used for local snapshot builds only (e.g. `0.1.0-SNAPSHOT`).
The git tag determines the actual published version in step 3 below, via the `releaseVersion` Gradle property set by CI.

## Steps

1. Create a release branch and open a PR with two changes:
   - Update the version in the `README.md` to the version you are about to release (e.g. `0.1.0`)
   - Bump `baseVersion` in `sdk/build.gradle.kts` to the next development version (e.g. `0.2.0`)

   ```bash
   git checkout -b release/v0.1.0
   # Edit README.md and sdk/build.gradle.kts, then:
   git add README.md sdk/build.gradle.kts
   git commit -m "Release v0.1.0"
   git push origin release/v0.1.0
   # Open a PR and merge it into main
   ```

2. Tag the merge commit on `main` and push the tag:
   ```bash
   git checkout main && git pull
   git tag v0.1.0
   git push origin v0.1.0
   ```

3. GitHub Actions runs automatically: builds, tests, publishes to Maven Central, and creates a GitHub Release.
   All required credentials (Maven Central, GPG signing) are already configured as GitHub secrets in the repo.

4. Verify the release at [central.sonatype.com](https://central.sonatype.com/artifact/com.github.zugaldia/stargate)
   and on the [GitHub Releases](https://github.com/zugaldia/stargate/releases) page.

5. Profit.
