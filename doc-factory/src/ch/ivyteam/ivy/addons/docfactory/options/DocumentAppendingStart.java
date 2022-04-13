package ch.ivyteam.ivy.addons.docfactory.options;

/**
 * The DocumentAppendingStart enum sets the way different documents will be
 * appended together.<br />
 * - {@link DocumentAppendingStart#NEW_PAGE} means that a new page break is
 * inserted for each document added.<br />
 * - {@link DocumentAppendingStart#CONTINUOUS} means that the documents are
 * continuously appended to each other without page break.
 */
public enum DocumentAppendingStart {
  CONTINUOUS, NEW_PAGE;
}
