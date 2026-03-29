# Releasing

## How versioning works

`baseVersion` in `sdk/build.gradle.kts` is used for local snapshot builds only (e.g. `0.1.0-SNAPSHOT`).
The git tag determines the actual published version in step 3 below, via the `releaseVersion` Gradle property set by CI.

## Steps

1. Create a local release branch first (do **not** commit directly to `main`):
   ```bash
   git checkout -b release/v0.1.0
   ```

2. Make the following two changes on that branch:
   - Update the version in `README.md` to the version you are about to release (e.g. `0.1.0`)
   - Bump `baseVersion` in `sdk/build.gradle.kts` to the next development version (e.g. `0.2.0`)

3. Commit, push the branch, and open a PR to merge it into `main`:
   ```bash
   git add README.md sdk/build.gradle.kts
   git commit -m "Release v0.1.0"
   git push origin release/v0.1.0
   # Open a PR and merge it into main
   ```

4. Tag the merge commit on `main` and push the tag:
   ```bash
   git checkout main && git pull
   git tag v0.1.0
   git push origin v0.1.0
   ```

5. GitHub Actions runs automatically: builds, tests, publishes to Maven Central, and creates a GitHub Release.
   All required credentials (Maven Central, GPG signing) are already configured as GitHub secrets in the repo.

6. Verify the release at [central.sonatype.com](https://central.sonatype.com/artifact/com.github.zugaldia/stargate)
   and on the [GitHub Releases](https://github.com/zugaldia/stargate/releases) page.

7. Profit.
