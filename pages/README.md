# Doc Factory documentation on GitHub Pages

From the repository root (JDK 21+ and Maven required):

```sh
mvn -U -f pages/pom.xml -Ppreview clean verify
```

For the published development SNAPSHOT too, add `-DincludeDev=true`. Open <http://localhost:8000/doc-factory/>; stop with Ctrl+C. Preview only builds and serves files locally—it does **not** publish Pages. The root opens the latest release; <http://localhost:8000/doc-factory/versions.html> lists the included versions. This is not an in-guide version switcher.

`pom.xml` pins the published release versions and the dev SNAPSHOT coordinate. `-U` refreshes the SNAPSHOT of that coordinate, not a branch. The [Pages workflow](../.github/workflows/doc-factory-pages.yml) separately uploads the complete site for review; public deployment requires an explicit `publish=true` dispatch from master. A deployment replaces the whole site, so retain all supported releases when updating pins. Keep existing Marketplace/Maven hosting until consumers are migrated.
