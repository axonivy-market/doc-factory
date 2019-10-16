# Axon.ivy Product Documentation

Based on [sphinx](http://www.sphinx-doc.org).
Using reStructredText as markup language.


## Build

    # download images
    mvn clean package

    # build and serve doc
    docker-compose up

You can browse the documentation at http://127.0.0.1:8000


## Dev Environment Setup

Use Eclipse or Visual Studio Code.

* **Eclipse**
    * [LiClipseText](https://marketplace.eclipse.org/content/liclipsetext)
      seems to be better maintained.
    * [ReST Editor](https://marketplace.eclipse.org/content/rest-editor)
      could be interesting but under active development.

* **Visual Studio Code**
    * Plugin *lextudio.restructuredtext* for syntax highlighting and code completion.
    * Plugin *rewrap* to wrap lines to 80 characters with ALT+Q


## Links

* [Sphinx](https://www.sphinx-doc.org/)
* [reStructuredtext Reference](http://www.sphinx-doc.org/en/master/usage/restructuredtext/index.html)
* [sphinx-rtd-theme demo](https://sphinx-rtd-theme.readthedocs.io/en/latest/demo/demo.html) and its [raw sources](https://raw.githubusercontent.com/rtfd/sphinx_rtd_theme/master/docs/demo/demo.rst)
* [reStructuredtext tables](https://rest-sphinx-memo.readthedocs.io/en/latest/ReST.html#tables)


## Guidelines

1. **Prevent big files**. If you use includes the included file should start with underscore.
2. **No binary files**. Use another repository for binary files.


## Samples

- GUI elements

	:guilabel:`Next button'

- Files and Directories

	:file:`docker-entrypoint.sh`
	:file:`[engineDir]/configuration/defaults`

- Inline code

	:code:`docker run -p 8080:8080 axonivy/axonivy-engine:dev`

- Inline code block

	.. code-block:: bash
       
       docker run -p 8080:8080 axonivy/axonivy-engine

- Include code files

	.. literalinclude:: includes/docker-entrypoint.sh
       :language: bash
       :linenos:

- Links to dev.axonivy.com

	:dev-url:`Download </download>`

- Links to public api

	:public-api:`IvyPrimefacesThemeResolver </ch/ivyteam/ivy/jsf/primefaces/theme/IvyPrimefacesThemeResolver.html>`

- Warnings

	.. Warn:: Attention! Make a backup!

- Tips

	.. Tip:: Best Practice. Advice!

- External Link

	`Title <https://www.google.ch>`_ 

- Internal link (original title)

	:ref:`web-xml`

- Interanl Link (custom title)

	:ref:`prepared <deployment-prepare>`

- Image

	.. figure:: images/workflow-demos.png

- Headings  

	# with overline, for parts
    * with overline, for chapters
    =, for sections
    -, for subsections
    ^, for subsubsections
    ", for paragraphs

