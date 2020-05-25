package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.ivyteam.io.IModificationListener;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.cm.IContentManagement;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.cm.IContentObject;
import ch.ivyteam.ivy.cm.IContentObjectValue;
import ch.ivyteam.ivy.cm.impl.ContentManagementEvent;
import ch.ivyteam.ivy.search.internal.IndexedObject;
import ch.ivyteam.ivy.search.restricted.exceptions.IndexException;
import ch.ivyteam.ivy.search.restricted.indexed.IIndexedSearchQuery;
import ch.ivyteam.ivy.search.restricted.indexed.IIndexedSearchableSystem;
import ch.ivyteam.ivy.search.restricted.indexed.ISearchIndex;
import ch.ivyteam.ivy.search.restricted.indexed.ISearchQueryTerm;
import ch.ivyteam.ivy.search.restricted.indexed.ISearchableIndexField;
import ch.ivyteam.ivy.search.restricted.indexed.SearchOperator;
import ch.ivyteam.ivy.search.restricted.indexed.SearchQueryBuilder.Field;

@SuppressWarnings("restriction")
public class MyCMS implements IContentManagementSystem
{
  @Override
  public IContentManagement getContentManagement()
  {
    return null;
  }

  @Override
  public IContentManagementSystem getContentManagementSystem()
  {
    return null;
  }

  @Override
  public IProcessModelVersion getProcessModelVersion()
  {
    return null;
  }

  @Override
  public <T> T getAdapter(Class<T> arg0)
  {
    return null;
  }

  @Override
  public IndexedObject createIndexedObject(Object arg0)
  {
    return null;
  }

  @Override
  public void createSearchIndex(IProgressMonitor arg0) throws IndexException
  {
  }

  @Override
  public ISearchIndex getIndex()
  {
    return null;
  }

  @Override
  public IProject getProject()
  {
    return null;
  }

  @Override
  public IIndexedSearchableSystem<?> getSearchableSystem()
  {
    return null;
  }

  @Override
  public boolean isReadyForSearching()
  {
    return false;
  }

  @Override
  public void addModificationListener(IModificationListener<ContentManagementEvent> arg0)
  {
  }

  @Override
  public void addSupportedLanguage(Locale language)
  {
  }

  @Override
  public String co(String uri)
  {
    return null;
  }

  @Override
  public String co(String uri, List<Object> formatObjects)
  {
    return null;
  }

  @Override
  public String cr(String uri)
  {
    return null;
  }

  @Override
  public IContentObject findContentObject(String uri)
  {
    return null;
  }

  @Override
  public IContentObjectValue findContentObjectValue(String uri, Locale language)
  {
    return null;
  }

  @Override
  public Set<IContentManagementSystem> getAllCmsFromRequiredProjects()
  {
    return null;
  }

  @Override
  public IContentObject getContentObject(String uri)
  {
    return null;
  }

  @Override
  @Deprecated
  public IContentObject getContentObject(String arg0, boolean arg1)
  {
    return null;
  }

  @Override
  public IContentObject getContentObjectForKey(Object key)
  {
    return null;
  }

  @Override
  public IContentObjectValue getContentObjectValue(String uri, Locale language)
  {
    return null;
  }

  @Override
  @Deprecated
  public IContentObjectValue getContentObjectValue(String arg0, Locale arg1, boolean arg2)
  {
    return null;
  }

  @Override
  public Locale getDefaultLanguage()
  {
    return null;
  }

  @Override
  public IContentObject getDefaultPageLayout()
  {
    return null;
  }

  @Override
  public IContentObject getDefaultPageStyleSheet()
  {
    return null;
  }

  @Override
  public String getDescription()
  {
    return null;
  }

  @Override
  public Object getIdentifier()
  {
    return null;
  }

  @Override
  public String getName()
  {
    return null;
  }

  @Override
  public IContentObject getRootContentObject()
  {
    return null;
  }

  @Override
  public java.util.List<Locale> getSupportedLanguages()
  {
    return null;
  }

  @Override
  public boolean isSupportedLanguage(Locale language)
  {
    return false;
  }

  @Override
  public void removeModificationListener(IModificationListener<ContentManagementEvent> arg0)
  {
  }

  @Override
  public void removeSupportedLanguage(Locale language)
  {
  }

  @Override
  public void setDefaultLanguage(Locale defaultLanguage)
  {
  }

  @Override
  public void setDefaultPageLayout(IContentObject defaultLayout)
  {
  }

  @Override
  public void setDefaultPageStyleSheet(IContentObject defaultStyleSheet)
  {
  }

  @Override
  public IIndexedSearchQuery createSearchQuery(List<ISearchQueryTerm> arg0, SearchOperator arg1)
  {
    return null;
  }

  @Override
  public Field createSearchQueryBuilder()
  {
    return null;
  }

  @Override
  public IIndexedSearchQuery createSearchQuery(String arg0, ISearchableIndexField arg1)
  {
    return null;
  }

  @Override
  public String coLocale(String uri, Locale locale)
  {
    return null;
  }

  @Override
  public String coLocale(String uri, String locale)
  {
    return null;
  }
}
