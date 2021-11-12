package ch.ivyteam.ivy.addons.docfactory.mergefield;

import java.util.ArrayList;
import java.util.Collection;

import ch.ivyteam.api.API;
import ch.ivyteam.ivy.addons.docfactory.TemplateMergeField;

public class SimpleMergeFieldsHandler {
	
	Collection<TemplateMergeField> templateMergeFields = new ArrayList<>();
	String[] mergeFieldNames;
	Object[] mergeFieldValues;
	
	private SimpleMergeFieldsHandler () {}
	
	public static SimpleMergeFieldsHandler forTemplateMergeFields(Collection<TemplateMergeField> mergeFields) {
		API.checkParameterNotNull(mergeFields, "Collection of TemplateMergeField");
		SimpleMergeFieldsHandler simpleMergeFieldsHandler = new SimpleMergeFieldsHandler();
		simpleMergeFieldsHandler.extractMergeFields(mergeFields);
		return simpleMergeFieldsHandler;
	}
	
	public Collection<TemplateMergeField> getTemplateMergeFields() {
		return templateMergeFields;
	}
	
	public String[] getMergeFieldNames() {
		return mergeFieldNames;
	}

	public Object[] getMergeFieldValues() {
		return mergeFieldValues;
	}

	private void extractMergeFields(Collection<TemplateMergeField> mergeFields) {
		templateMergeFields.addAll(mergeFields);
		mergeFieldNames= new String[templateMergeFields.size()];
		mergeFieldValues= new Object[templateMergeFields.size()];
		int i = 0;
		for(TemplateMergeField tmf: templateMergeFields) {
			String s = tmf.getMergeFieldName();
			if(s.startsWith("Image:") || s.startsWith("Image_")) {
				//The merge field name indicates an Image, we take only the second part of the name
				mergeFieldNames[i]=s.substring(6);
			}
			else {
				mergeFieldNames[i]=s;
			}

			mergeFieldValues[i] = tmf.getValueForMailMerging();
			i++;
		}
	}

}
