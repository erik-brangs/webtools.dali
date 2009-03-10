/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.java;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AttributeMapping;
import org.eclipse.jpt.core.context.java.JavaManyToManyMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.ManyToManyAnnotation;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;


public class GenericJavaManyToManyMapping extends AbstractJavaMultiRelationshipMapping<ManyToManyAnnotation>
	implements JavaManyToManyMapping
{
	
	public GenericJavaManyToManyMapping(JavaPersistentAttribute parent) {
		super(parent);
	}

	public String getKey() {
		return MappingKeys.MANY_TO_MANY_ATTRIBUTE_MAPPING_KEY;
	}

	public String getAnnotationName() {
		return ManyToManyAnnotation.ANNOTATION_NAME;
	}

	public Iterator<String> supportingAnnotationNames() {
		return new ArrayIterator<String>(
			JPA.ORDER_BY,
			JPA.MAP_KEY,
			JPA.JOIN_TABLE);
	}
		
	// ********** JavaMultiRelationshipMapping implementation **********

	@Override
	protected boolean mappedByTouches(int pos, CompilationUnit astRoot) {
		return this.resourceMapping.mappedByTouches(pos, astRoot);
	}
	
	
	@Override
	protected void setMappedByOnResourceModel(String mappedBy) {
		this.resourceMapping.setMappedBy(mappedBy);
	}
	
	@Override
	protected String getResourceMappedBy() {
		return this.resourceMapping.getMappedBy();
	}
	
	// ********** INonOwningMapping implementation **********
	
	public boolean mappedByIsValid(AttributeMapping mappedByMapping) {
		String mappedByKey = mappedByMapping.getKey();
		return (mappedByKey == MappingKeys.MANY_TO_MANY_ATTRIBUTE_MAPPING_KEY);
	}
	
	@Override
	public TextRange getMappedByTextRange(CompilationUnit astRoot) {
		return this.resourceMapping.getMappedByTextRange(astRoot);
	}
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter, CompilationUnit astRoot) {
		super.validate(messages, reporter, astRoot);
	}
}
