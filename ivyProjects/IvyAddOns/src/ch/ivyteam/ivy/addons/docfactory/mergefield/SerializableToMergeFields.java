package ch.ivyteam.ivy.addons.docfactory.mergefield;

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

import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;

public class SerializableToMergeFields {
	
	private static final Set<Class<?>> WRAPPER_TYPES = getNotIntrospectedClasses();

	
	public static Collection<TemplateMergeField> getMergeFields(Serializable bean) {
		if(bean == null) {
			return Collections.emptyList();
		}
		
		
		Collection<TemplateMergeField> result = new ArrayList<>();
		Map<String, Object> introspected = null;
		try {
			introspected = introspect(bean, bean.getClass().getSimpleName().toLowerCase() + ".");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<Entry<String, Object>> iter = introspected.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			result.add(TemplateMergeField.withName(entry.getKey()).withValue(entry.getValue()));
		}
		return result;
	}

	private static Map<String, Object> introspect(Serializable bean, String optionalBaseName) throws Exception {
		
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
		return !WRAPPER_TYPES.contains(obj.getClass());
	}
	
	private static Set<Class<?>> getNotIntrospectedClasses()
    {
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


}
