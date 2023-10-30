package ch.ivyteam.ivy.addons.docfactory.mergefield.internal;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;
import ch.ivyteam.ivy.addons.docfactory.exception.MergeFieldsBeansIntrospectionException;

/**
 * Not public API
 *
 */
public class MergeFieldsExtractor {

  private static final String CLASS_PROPERTY_NAME = "class";
  private static final String DOT = ".";
  private static final String EMPTY_MERGEFIELD_PREFIX_NAME = "";
  private static final Set<Class<?>> TYPES_IGNORED_FOR_GETTING_EMBEDDED_PROPERTIES = getTypesWhichShouldNotBeIntrospected();

  /**
   * Returns a collection of template merge-fields built with the given bean
   * accessible properties and its contained children-beans-tree accessible
   * properties.<br />
   * Each template merge field of the collection is NOT set as a simple value.
   * This means that its contained collections can be used for mail merging with
   * regions.
   * @param bean the bean which properties have to be transformed in
   *          TemplateMergeField objects. If null, an empty collection is
   *          returned.
   */
  public static Collection<TemplateMergeField> getMergeFields(Object bean) {
    if (bean == null) {
      return Collections.emptyList();
    }
    Map<String, Object> fieldsNameValueMap = getBeanPropertyValues(bean,
            Introspector.decapitalize(bean.getClass().getSimpleName()) + DOT);
    if (fieldsNameValueMap == null) {
      return Collections.emptyList();
    }
    Collection<TemplateMergeField> result = new ArrayList<>();
    fieldsNameValueMap.forEach((key, value) -> result.add(TemplateMergeField.withName(key).withValue(value)));
    return result;
  }

  /**
   * Returns a collection of template merge-fields built with the given bean
   * accessible properties and its contained children-beans-tree accessible
   * properties.<br />
   * Each template merge field of the collection is set as a simple value. This
   * means that the collections won't be used for mail merging with region.
   * @param bean the bean which properties have to be transformed in
   *          TemplateMergeField objects. If null, an empty collection is
   *          returned.
   */
  public static Collection<TemplateMergeField> getMergeFieldsForSimpleMerge(Object bean) {
    if (bean == null) {
      return Collections.emptyList();
    }
    Map<String, Object> fieldsNameValueMap = getBeanPropertyValues(bean,
            Introspector.decapitalize(bean.getClass().getSimpleName()) + DOT);
    if (fieldsNameValueMap == null) {
      return Collections.emptyList();
    }
    Collection<TemplateMergeField> result = new ArrayList<>();
    fieldsNameValueMap
            .forEach((key, value) -> result.add(TemplateMergeField.withName(key).asSimpleValue(value)));
    return result;
  }

  /**
   * Same as {@link #getMergeFields(Object)} where the object bean is the given
   * TemplateMergeField value
   * @param templateMergeField
   */
  public static Collection<TemplateMergeField> getChildrenMergeFieldsOfTemplateMergeField(
          TemplateMergeField templateMergeField) {
    API.checkNotNull(templateMergeField, "TemplateMergeField");
    if (templateMergeField.getValue() == null) {
      return Collections.emptyList();
    }
    String baseName = getNamePrefixForChildrenTemplateMergeFields(templateMergeField);

    Map<String, Object> childrenPropertiesForTemplateMergeField = getBeanPropertyValues(
            templateMergeField.getValue(),
            baseName + Introspector.decapitalize(templateMergeField.getValue().getClass().getSimpleName())
                    + DOT);

    if (childrenPropertiesForTemplateMergeField == null) {
      return Collections.emptyList();
    }

    Collection<TemplateMergeField> result = new ArrayList<>();
    childrenPropertiesForTemplateMergeField.forEach((key, value) -> result.add(
            TemplateMergeField.withName(key).withValue(value)
                    .useLocaleAndResetNumberFormatAndDateFormat(templateMergeField.getLocale())));
    return result;
  }

  /**
   * Used by the DocFactory for building Mail merge data sources for mail
   * merging with regions.
   * @param tablename
   * @param templateMergeFieldFromCollectionType
   */
  public static Collection<Collection<TemplateMergeField>> getMergeFieldsForCollectionTemplateMergeField(
          String tablename, TemplateMergeField templateMergeFieldFromCollectionType) {
    if (!templateMergeFieldFromCollectionType.isCollection()) {
      return Collections.emptyList();
    }
    Collection<?> collection = null;
    if (templateMergeFieldFromCollectionType.getValue() instanceof Map<?, ?>) {
      collection = ((Map<?, ?>) templateMergeFieldFromCollectionType.getValue()).values();
    } else {
      collection = (Collection<?>) templateMergeFieldFromCollectionType.getValue();
    }

    Collection<Collection<TemplateMergeField>> result = new ArrayList<>();
    for (Object obj : collection) {
      Collection<TemplateMergeField> templateMergeFieldsRow = new ArrayList<>();
      if (!shouldBeIntrospectedForGettingEmbeddedProperties(obj)) {
        templateMergeFieldsRow.add(TemplateMergeField.withName(tablename).withValue(obj));
        result.add(templateMergeFieldsRow);
        continue;
      }
      Map<String, Object> introspected = getBeanPropertyValues(obj,
              obj.getClass().getSimpleName().toLowerCase() + DOT);
      introspected.forEach((key, value) -> templateMergeFieldsRow.add(
              TemplateMergeField.withName(key).withValue(value).useLocaleAndResetNumberFormatAndDateFormat(
                      templateMergeFieldFromCollectionType.getLocale())));
      result.add(templateMergeFieldsRow);
    }
    return result;
  }

  private static String getNamePrefixForChildrenTemplateMergeFields(TemplateMergeField templateMergeField) {
    String baseName = templateMergeField.getMergeFieldName();
    if (StringUtils.isBlank(baseName)) {
      baseName = EMPTY_MERGEFIELD_PREFIX_NAME;
    } else if (!baseName.endsWith(DOT)) {
      baseName = baseName + DOT;
    }
    return baseName;
  }

  private static Map<String, Object> getBeanPropertyValues(Object bean, String propertiesNamePrefix) {
    Map<String, Object> result = new HashMap<String, Object>();
    BeanInfo info;
    try {
      info = Introspector.getBeanInfo(bean.getClass());
    } catch (IntrospectionException e) {
      throw new MergeFieldsBeansIntrospectionException(
              "An Exception occurred while getting the BeanInfo of " + bean.getClass().getName(), e);
    }

    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
      Object propertyValue = getPropertyValue(bean, pd);
      if (propertyValue == null) {
        continue;
      }
      if (shouldBeIntrospectedForGettingEmbeddedProperties(propertyValue)) {
        result.putAll(getBeanPropertyValues(propertyValue, propertiesNamePrefix + pd.getName() + DOT));
      }
      result.put(propertiesNamePrefix + pd.getName(), propertyValue);
    }
    return result;
  }

  private static Object getPropertyValue(Object objectContainingValue,
          PropertyDescriptor propertyDescriptor) {
    Method reader = propertyDescriptor.getReadMethod();
    if (reader == null || propertyDescriptor.getName().equals(CLASS_PROPERTY_NAME)) {
      return null;
    }
    try {
      reader.setAccessible(true);
      //return MethodUtils.getAccessibleMethod(reader).invoke(objectContainingValue);
      return reader.invoke(objectContainingValue);
    } catch (IllegalAccessException | IllegalArgumentException
            | InvocationTargetException e) {
      throw new MergeFieldsBeansIntrospectionException(
              "An Exception occurred while reading the value of the property " + propertyDescriptor.getName(),
              e);
    }
  }

  private static boolean shouldBeIntrospectedForGettingEmbeddedProperties(Object obj) {
    return !(TYPES_IGNORED_FOR_GETTING_EMBEDDED_PROPERTIES.contains(obj.getClass()) || isCollection(obj));
  }

  private static Set<Class<?>> getTypesWhichShouldNotBeIntrospected() {
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
    ret.add(Class.class);
    return ret;
  }

  private static boolean isCollection(Object obj) {
    return obj instanceof Collection || obj instanceof Map<?, ?>;
  }

  /**
   * @deprecated Because it is not used in the DocFactory API. As this class is
   *             not Public API, you should not use it.
   */
  @Deprecated
  public static Collection<TemplateMergeField> getMergeFieldsWithBaseName(Object bean,
          String mergeFieldsNamePrefix) {
    if (bean == null) {
      return Collections.emptyList();
    }
    if (StringUtils.isBlank(mergeFieldsNamePrefix)) {
      mergeFieldsNamePrefix = EMPTY_MERGEFIELD_PREFIX_NAME;
    } else if (!mergeFieldsNamePrefix.endsWith(DOT)) {
      mergeFieldsNamePrefix = mergeFieldsNamePrefix + DOT;
    }
    Map<String, Object> fieldsNameValueMap = getBeanPropertyValues(bean, mergeFieldsNamePrefix);
    if (fieldsNameValueMap == null) {
      return Collections.emptyList();
    }
    Collection<TemplateMergeField> result = new ArrayList<>();
    fieldsNameValueMap.forEach((key, value) -> result.add(TemplateMergeField.withName(key).withValue(value)));
    return result;
  }

  /**
   * @deprecated Use
   *             {@link #getChildrenMergeFieldsOfTemplateMergeField(TemplateMergeField)}
   *             instead Will be removed in a near future
   */
  @Deprecated
  public static Collection<TemplateMergeField> getChildrenMergeFieldsForObjectMergeField(
          TemplateMergeField templateMergeField) {
    return getChildrenMergeFieldsOfTemplateMergeField(templateMergeField);
  }

}
