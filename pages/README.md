# Doc Factory documentation on GitHub Pages

From the repository root (JDK 21+ and Maven required), start a one-command preview that includes the configured development snapshot:

```sh
./pages/preview.sh
```

The script builds the site with Maven, then serves it at <http://127.0.0.1:8000/doc-factory/>. Stop the server with Ctrl+C. To preview only released documentation instead, run these two commands:

```sh
mvn -U --batch-mode -f pages/pom.xml clean generate-resources
java -m jdk.httpserver -b 127.0.0.1 -p 8000 -d "$(pwd)/pages/target"
```

The root opens the latest release, and <http://127.0.0.1:8000/doc-factory/versions.html> lists the included versions. Previewing builds and serves files locally; it does **not** publish Pages. This is not an in-guide version switcher.

The [Pages workflow](../.github/workflows/doc-factory-pages.yml) uploads the complete site for review and includes the development snapshot by default. Publishing still requires an explicit `publish=true` dispatch from `master`. A deployment replaces the whole site, so retain all supported releases when updating version pins. Keep existing Marketplace/Maven hosting until consumers are migrated.
