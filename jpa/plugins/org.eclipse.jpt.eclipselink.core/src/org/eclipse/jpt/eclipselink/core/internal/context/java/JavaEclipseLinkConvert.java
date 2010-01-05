/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.java;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaConverter;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaJpaContextNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConverter;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkConvertAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkConverterAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkObjectTypeConverterAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkStructConverterAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkTypeConverterAnnotation;
import org.eclipse.jpt.utility.Filter;
import org.eclipse.jpt.utility.internal.CollectionTools;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.FilteringIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class JavaEclipseLinkConvert extends AbstractJavaJpaContextNode implements EclipseLinkConvert, JavaConverter
{
	private String specifiedConverterName;
	
	private JavaResourcePersistentAttribute resourcePersistentAttribute;
	
	private JavaEclipseLinkConverter converter;
	
	public JavaEclipseLinkConvert(JavaAttributeMapping parent, JavaResourcePersistentAttribute jrpa) {
		super(parent);
		this.initialize(jrpa);
	}

	@Override
	public JavaAttributeMapping getParent() {
		return (JavaAttributeMapping) super.getParent();
	}

	public String getType() {
		return EclipseLinkConvert.ECLIPSE_LINK_CONVERTER;
	}

	protected String getAnnotationName() {
		return EclipseLinkConvertAnnotation.ANNOTATION_NAME;
	}
		
	public void addToResourceModel() {
		this.resourcePersistentAttribute.addAnnotation(getAnnotationName());
	}
	
	public void removeFromResourceModel() {
		this.resourcePersistentAttribute.removeAnnotation(getAnnotationName());
		if (getConverter() != null) {
			this.resourcePersistentAttribute.removeAnnotation(getConverter().getAnnotationName());
		}
	}

	public TextRange getValidationTextRange(CompilationUnit astRoot) {
		return getResourceConvert().getTextRange(astRoot);
	}

	protected EclipseLinkConvertAnnotation getResourceConvert() {
		return (EclipseLinkConvertAnnotation) this.resourcePersistentAttribute.getAnnotation(getAnnotationName());
	}
	
	public String getConverterName() {
		return getSpecifiedConverterName() == null ? getDefaultConverterName() : getSpecifiedConverterName();
	}

	public String getDefaultConverterName() {
		return DEFAULT_CONVERTER_NAME;
	}

	public String getSpecifiedConverterName() {
		return this.specifiedConverterName;
	}

	public void setSpecifiedConverterName(String newSpecifiedConverterName) {
		String oldSpecifiedConverterName = this.specifiedConverterName;
		this.specifiedConverterName = newSpecifiedConverterName;
		getResourceConvert().setValue(newSpecifiedConverterName);
		firePropertyChanged(SPECIFIED_CONVERTER_NAME_PROPERTY, oldSpecifiedConverterName, newSpecifiedConverterName);
	}
	
	protected void setSpecifiedConverterName_(String newSpecifiedConverterName) {
		String oldSpecifiedConverterName = this.specifiedConverterName;
		this.specifiedConverterName = newSpecifiedConverterName;
		firePropertyChanged(SPECIFIED_CONVERTER_NAME_PROPERTY, oldSpecifiedConverterName, newSpecifiedConverterName);
	}

	public JavaEclipseLinkConverter getConverter() {
		return this.converter;
	}
	
	protected String getConverterType() {
		if (this.converter == null) {
			return EclipseLinkConverter.NO_CONVERTER;
		}
		return this.converter.getType();
	}

	public void setConverter(String converterType) {
		if (getConverterType() == converterType) {
			return;
		}
		JavaEclipseLinkConverter oldConverter = this.converter;
		JavaEclipseLinkConverter newConverter = buildConverter(converterType);
		this.converter = null;
		if (oldConverter != null) {
			this.resourcePersistentAttribute.removeAnnotation(oldConverter.getAnnotationName());
		}
		this.converter = newConverter;
		if (newConverter != null) {
			this.resourcePersistentAttribute.addAnnotation(newConverter.getAnnotationName());
		}
		firePropertyChanged(CONVERTER_PROPERTY, oldConverter, newConverter);
	}
	
	protected void setConverter(JavaEclipseLinkConverter newConverter) {
		JavaEclipseLinkConverter oldConverter = this.converter;
		this.converter = newConverter;
		firePropertyChanged(CONVERTER_PROPERTY, oldConverter, newConverter);
	}
	
	protected void initialize(JavaResourcePersistentAttribute jrpa) {
		this.resourcePersistentAttribute = jrpa;
		this.specifiedConverterName = this.getResourceConverterName();
		this.converter = this.buildConverter(this.getResourceConverterType());
	}
	
	public void update(JavaResourcePersistentAttribute jrpa) {
		this.resourcePersistentAttribute = jrpa;
		this.setSpecifiedConverterName_(this.getResourceConverterName());
		if (getResourceConverterType() == getConverterType()) {
			getConverter().update(this.resourcePersistentAttribute);
		}
		else {
			JavaEclipseLinkConverter javaConverter = buildConverter(getResourceConverterType());
			setConverter(javaConverter);
		}
	}
	
	protected String getResourceConverterName() {
		EclipseLinkConvertAnnotation resourceConvert = getResourceConvert();
		return resourceConvert == null ? null : resourceConvert.getValue();
	}

	
	protected JavaEclipseLinkConverter buildConverter(String converterType) {
		if (converterType == EclipseLinkConverter.NO_CONVERTER) {
			return null;
		}
		if (converterType == EclipseLinkConverter.CUSTOM_CONVERTER) {
			return buildCustomConverter();
		}
		else if (converterType == EclipseLinkConverter.TYPE_CONVERTER) {
			return buildTypeConverter();
		}
		else if (converterType == EclipseLinkConverter.OBJECT_TYPE_CONVERTER) {
			return buildObjectTypeConverter();
		}
		else if (converterType == EclipseLinkConverter.STRUCT_CONVERTER) {
			return buildStructConverter();
		}
		return null;
	}
	
	protected JavaEclipseLinkCustomConverter buildCustomConverter() {
		JavaEclipseLinkCustomConverter contextConverter = new JavaEclipseLinkCustomConverter(this);
		contextConverter.initialize(this.resourcePersistentAttribute);
		return contextConverter;
	}

	protected JavaEclipseLinkTypeConverter buildTypeConverter() {
		JavaEclipseLinkTypeConverter contextConverter = new JavaEclipseLinkTypeConverter(this);
		contextConverter.initialize(this.resourcePersistentAttribute);
		return contextConverter;
	}

	protected JavaEclipseLinkObjectTypeConverter buildObjectTypeConverter() {
		JavaEclipseLinkObjectTypeConverter contextConverter = new JavaEclipseLinkObjectTypeConverter(this);
		contextConverter.initialize(this.resourcePersistentAttribute);
		return contextConverter;
	}

	protected JavaEclipseLinkStructConverter buildStructConverter() {
		JavaEclipseLinkStructConverter contextConverter = new JavaEclipseLinkStructConverter(this);
		contextConverter.initialize(this.resourcePersistentAttribute);
		return contextConverter;
	}

	protected String getResourceConverterType() {
		if (this.resourcePersistentAttribute.getAnnotation(EclipseLinkConverterAnnotation.ANNOTATION_NAME) != null) {
			return EclipseLinkConverter.CUSTOM_CONVERTER;
		}
		else if (this.resourcePersistentAttribute.getAnnotation(EclipseLinkTypeConverterAnnotation.ANNOTATION_NAME) != null) {
			return EclipseLinkConverter.TYPE_CONVERTER;
		}
		else if (this.resourcePersistentAttribute.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME) != null) {
			return EclipseLinkConverter.OBJECT_TYPE_CONVERTER;
		}
		else if (this.resourcePersistentAttribute.getAnnotation(EclipseLinkStructConverterAnnotation.ANNOTATION_NAME) != null) {
			return EclipseLinkConverter.STRUCT_CONVERTER;
		}
		
		return null;
	}

	//*************** code assist ******************
	
	@Override
	public Iterator<String> javaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterator<String> result = super.javaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.convertValueTouches(pos, astRoot)) {
			result = this.persistenceConvertersNames(filter);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	protected boolean convertValueTouches(int pos, CompilationUnit astRoot) {
		if (getResourceConvert() != null) {
			return this.getResourceConvert().valueTouches(pos, astRoot);
		}
		return false;
	}

	protected Iterator<String> persistenceConvertersNames() {
		if(this.getEclipseLinkPersistenceUnit().convertersSize() == 0) {
			return EmptyIterator.<String> instance();
		}
		return CollectionTools.iterator(this.getEclipseLinkPersistenceUnit().uniqueConverterNames());
	}

	private Iterator<String> convertersNames(Filter<String> filter) {
		return new FilteringIterator<String>(this.persistenceConvertersNames(), filter);
	}

	protected Iterator<String> persistenceConvertersNames(Filter<String> filter) {
		return StringTools.convertToJavaStringLiterals(this.convertersNames(filter));
	}

	protected EclipseLinkPersistenceUnit getEclipseLinkPersistenceUnit() {
		return (EclipseLinkPersistenceUnit) this.getPersistenceUnit();
	}
	
	//****************** validation ********************
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
		if (getConverter() != null) {
			getConverter().validate(messages, reporter, astRoot);
		}
	}

}
