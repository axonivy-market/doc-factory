/**
 * 
 */
package ch.ivyteam.ivy.addons.filemanager.ivy.implemented;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;

import ch.ivyteam.api.IvyScriptVisibility;
import ch.ivyteam.api.PublicAPI;
import ch.ivyteam.io.IModificationListener;
import ch.ivyteam.ivy.application.IProcessModelVersion;
import ch.ivyteam.ivy.cm.IContentManagement;
import ch.ivyteam.ivy.cm.IContentManagementSystem;
import ch.ivyteam.ivy.cm.IContentObject;
import ch.ivyteam.ivy.cm.IContentObjectValue;
import ch.ivyteam.ivy.cm.exceptions.UnsupportedLanguageException;
import ch.ivyteam.ivy.cm.impl.ContentManagementEvent;
import ch.ivyteam.ivy.persistence.PersistencyException;
import ch.ivyteam.ivy.search.internal.IndexedObject;
import ch.ivyteam.ivy.search.restricted.exceptions.IndexException;
import ch.ivyteam.ivy.search.restricted.indexed.IIndexedSearchQuery;
import ch.ivyteam.ivy.search.restricted.indexed.IIndexedSearchableSystem;
import ch.ivyteam.ivy.search.restricted.indexed.ISearchIndex;
import ch.ivyteam.ivy.search.restricted.indexed.ISearchableIndexField;
import ch.ivyteam.ivy.search.restricted.indexed.SearchQueryBuilder.Field;

/**
 * @author ec
 *
 */
public class MyCMS implements IContentManagementSystem {

	@Override
	public IContentManagement getContentManagement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContentManagementSystem getContentManagementSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcessModelVersion getProcessModelVersion()
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getAdapter(Class<T> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IndexedObject createIndexedObject(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createSearchIndex(IProgressMonitor arg0) throws IndexException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public ISearchIndex getIndex() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProject getProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIndexedSearchableSystem<?> getSearchableSystem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isReadyForSearching() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addModificationListener(
			IModificationListener<ContentManagementEvent> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void addSupportedLanguage(Locale language)
			throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String co(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String co(String uri, java.util.List<Object> formatObjects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public String cr(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public IContentObject findContentObject(String uri)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public IContentObjectValue findContentObjectValue(String uri,
			Locale language) throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IContentManagementSystem> getAllCmsFromRequiredProjects()
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public IContentObject getContentObject(String uri)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public IContentObject getContentObject(String arg0, boolean arg1)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public IContentObject getContentObjectForKey(Object key)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public IContentObjectValue getContentObjectValue(String uri, Locale language)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public IContentObjectValue getContentObjectValue(String arg0, Locale arg1,
			boolean arg2) throws PersistencyException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public Locale getDefaultLanguage() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public IContentObject getDefaultPageLayout() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public IContentObject getDefaultPageStyleSheet()
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescription() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public Object getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.NOVICE)
	public IContentObject getRootContentObject() throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public java.util.List<Locale> getSupportedLanguages()
			throws PersistencyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public boolean isSupportedLanguage(Locale language)
			throws PersistencyException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeModificationListener(
			IModificationListener<ContentManagementEvent> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public void removeSupportedLanguage(Locale language)
			throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.ADVANCED)
	public void setDefaultLanguage(Locale defaultLanguage)
			throws PersistencyException, UnsupportedLanguageException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public void setDefaultPageLayout(IContentObject defaultLayout)
			throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	@PublicAPI(IvyScriptVisibility.HIDDEN)
	public void setDefaultPageStyleSheet(IContentObject defaultStyleSheet)
			throws PersistencyException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IIndexedSearchQuery createSearchQuery(
			List<ch.ivyteam.ivy.search.restricted.indexed.ISearchQueryTerm> arg0,
			ch.ivyteam.ivy.search.restricted.indexed.SearchOperator arg1) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Field createSearchQueryBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IIndexedSearchQuery createSearchQuery(String arg0,
			ISearchableIndexField arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String coLocale(String uri, Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String coLocale(String uri, String locale) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
