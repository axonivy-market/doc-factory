package ch.ivyteam.ivy.addons.docfactory.mergefield.internal;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;

/**
 * Not public API
 * @author ec
 *
 */
public class MergeFieldsExtractor {
	
	private static final Set<Class<?>> WRAPPER_TYPES = getNotIntrospectedClasses();

	public static Collection<TemplateMergeField> getMergeFieldsWithBaseName(Object bean, String baseName) {
		if(bean == null) {
			return Collections.emptyList();
		}
		Iterator<Entry<String, Object>> iter = extractFields(bean, baseName);
		if(iter == null) {
			return Collections.emptyList();
		}
		Collection<TemplateMergeField> result = new ArrayList<>();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			result.add(TemplateMergeField.withName(entry.getKey()).withValue(entry.getValue()));
		}
		return result;
	}
	
	private static Collection<TemplateMergeField> getMergeFieldsWithBaseNameForSimpleMerge(
			Object bean, String baseName) {
		if(bean == null) {
			return Collections.emptyList();
		}
		Iterator<Entry<String, Object>> iter = extractFields(bean, baseName);
		if(iter == null) {
			return Collections.emptyList();
		}
		Collection<TemplateMergeField> result = new ArrayList<>();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			result.add(TemplateMergeField.withName(entry.getKey()).asSimpleValue(entry.getValue()));
		}
		return result;
	}

	private static Iterator<Entry<String, Object>> extractFields(Object bean,
			String baseName) {
		
		if(StringUtils.isBlank(baseName)) {
			baseName = "";
		} else if(!baseName.endsWith(".")) {
			baseName = baseName + ".";
		}
		Map<String, Object> introspected = null;
		try {
			introspected = introspect(bean, baseName + Introspector.decapitalize(bean.getClass().getSimpleName()) + ".");
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		if(introspected == null) {
			return null;
		}
		Iterator<Entry<String, Object>> iter = introspected.entrySet().iterator();
		return iter;
	}
	
	public static Collection<TemplateMergeField> getChildrenMergeFieldsForObjectMergeField(TemplateMergeField templateMergeField) {
		API.checkNotNull(templateMergeField, "TemplateMergeField");
		if(templateMergeField.getValue() == null) {
			return Collections.emptyList();
		}
		String baseName = templateMergeField.getMergeFieldName();
		if(baseName.contains(".declaringClass.")) {
			return Collections.emptyList();
		}
		if(StringUtils.isBlank(baseName)) {
			baseName = "";
		} else if(!baseName.endsWith(".")) {
			baseName = baseName + ".";
		}
		Collection<TemplateMergeField> result = new ArrayList<>();
		Map<String, Object> introspected = null;
		try {
			introspected = introspect(templateMergeField.getValue(), baseName + Introspector.decapitalize(templateMergeField.getValue().getClass().getSimpleName()) + ".");
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		if(introspected == null ||  introspected.entrySet() == null) {
			return Collections.emptyList();
		}
		Iterator<Entry<String, Object>> iter = introspected.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			result.add(TemplateMergeField.withName(entry.getKey()).withValue(entry.getValue()).useLocaleAndResetNumberFormatAndDateFormat(templateMergeField.getLocale()));
		}
		return result;
	}
	
	public static Collection<TemplateMergeField> getMergeFields(Object bean) {
		return getMergeFieldsWithBaseName(bean, "");
	}
	
	public static Collection<TemplateMergeField> getMergeFieldsForSimpleMerge(Object bean) {
		return getMergeFieldsWithBaseNameForSimpleMerge(bean, "");
	}
	
	

	public static Collection<Collection<TemplateMergeField>> getMergeFieldsForCollectionTemplateMergeField(String tablename, TemplateMergeField templateMergeField) {
		if(!templateMergeField.isCollection()) {
			return Collections.emptyList();
		}
		Collection<?> collection = null;
		if(templateMergeField.getValue() instanceof Map<?, ?>) {
			collection = ((Map<?, ?>) templateMergeField.getValue()).values();
		} else {
			collection = (Collection<?>) templateMergeField.getValue();
		}
		
		Collection<Collection<TemplateMergeField>> result = new ArrayList<>();
		//Collection<?> collection = (Collection<?>) templateMergeField;
		for(Object obj: collection) {
			Collection<TemplateMergeField> row = new ArrayList<>();
			if(!hasToBeIntrospected(obj)) {
				row.add(TemplateMergeField.withName(tablename).withValue(obj));
				result.add(row);
				continue;
			} 
			Map<String, Object> introspected = null;
			try {
				introspected = introspect(obj, obj.getClass().getSimpleName().toLowerCase() + ".");
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			Iterator<Entry<String, Object>> iter = introspected.entrySet().iterator();
			while(iter.hasNext()) {
				Entry<String, Object> entry = iter.next();
				row.add(TemplateMergeField.withName(entry.getKey()).withValue(entry.getValue()).useLocaleAndResetNumberFormatAndDateFormat(templateMergeField.getLocale()));
			}
			result.add(row);
		}
		return result;
	}

	private static Map<String, Object> introspect(Object bean, String optionalBaseName) throws Exception {
		
	    Map<String, Object> result = new HashMap<String, Object>();
	    BeanInfo info = Introspector.getBeanInfo(bean.getClass());
	    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	        Method reader = pd.getReadMethod();
	        if(pd.getName().equals("class")) {
	        	continue;
	        }
	        if (reader != null) {
	        	Object obj = reader.invoke(bean);
	        	if(obj == null) {
	        		continue;
	        	}
	        	if(obj instanceof Serializable && hasToBeIntrospected(obj)) {
	        		result.putAll(introspect((Serializable) obj, optionalBaseName + pd.getName() + "."));
	        	}
	            result.put(optionalBaseName + pd.getName(), reader.invoke(bean));
	        }
	    }
	    return result;
	}
	
	private static boolean hasToBeIntrospected(Object obj) {
		return !(WRAPPER_TYPES.contains(obj.getClass()) || isCollection(obj));
	}
	
	private static Set<Class<?>> getNotIntrospectedClasses() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(Date.class);
        ret.add(java.sql.Date.class);
        ret.add(String.class);
        ret.add(java.io.File.class);
        return ret;
    }
	
	private static boolean isCollection(Object obj) {
		return obj instanceof Collection || obj instanceof Map<?,?>;
	}

	


}
