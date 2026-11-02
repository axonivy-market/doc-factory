# Doc Factory documentation on GitHub Pages

This Maven project collects published documentation ZIPs; it does not build the Sphinx guide. Run it independently of the product reactor:

```sh
mvn -U -f pages/pom.xml clean generate-resources
mvn -U -f pages/pom.xml clean generate-resources -DincludeDev=true
```

The complete site is in `pages/target/doc-factory/`, intended to be served at `/doc-factory/`. The root forwards to the latest **released** guide. `/versions.html` lists the retained releases and, only when included, dev. It is not an in-guide version switcher. Published ZIPs have no Pages navigation injected into their Sphinx HTML.

The POM pins release ZIPs `10.0.24`, `12.0.8`, `13.2.2`, and the optional `14.0.0-SNAPSHOT` development ZIP. The latter is the coordinate currently declared on `dev/14.0`, **not** proof that the latest Nexus publication came from that branch. Check its publication run and source commit before describing it as current dev documentation. `-U` refreshes Maven SNAPSHOT metadata for this coordinate; it does not select a branch.

To inspect the site locally, serve the directory *containing* `doc-factory` as an HTTP document root, then open `/doc-factory/`. For example, if BusyBox is available:

```sh
busybox httpd -f -p 127.0.0.1:8000 -h pages/target
```

To update documentation after publication, change the matching pinned property in `pages/pom.xml`, validate the complete site, review/merge the change to master, then run the Pages workflow with `publish=true` only when explicitly approved. A new release *line* gets a new URL; do not repoint an existing `/release/<line>/` URL to another line. Deployments replace the entire Pages site, so do not drop retained releases from the build. Publishing with `include_dev=false` removes dev from the next deployed site. Keep existing Marketplace and Maven hosting until consumers and old links are addressed.
