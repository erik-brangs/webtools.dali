/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import org.eclipse.jpt.core.context.java.JavaGeneratedValue;
import org.eclipse.jpt.core.context.java.JavaIdMapping;
import org.eclipse.jpt.core.resource.common.AbstractJpaEObject;
import org.eclipse.jpt.core.resource.orm.GenerationType;
import org.eclipse.jpt.core.resource.orm.XmlGeneratedValue;
import org.eclipse.jpt.core.utility.TextRange;

public class VirtualXmlGeneratedValue extends AbstractJpaEObject implements XmlGeneratedValue
{
	JavaIdMapping javaIdMapping;

	protected boolean metadataComplete;
	
		
	public VirtualXmlGeneratedValue(JavaIdMapping javaIdMapping, boolean metadataComplete) {
		super();
		this.javaIdMapping = javaIdMapping;
		this.metadataComplete = metadataComplete;
	}

	protected JavaGeneratedValue getJavaGeneratedValue() {
		return this.javaIdMapping.getGeneratedValue();
	}


	public String getGenerator() {
		return this.metadataComplete ? null : this.getJavaGeneratedValue().getGenerator();
	}

	public GenerationType getStrategy() {
		return this.metadataComplete ? null : org.eclipse.jpt.core.context.GenerationType.toOrmResourceModel(this.getJavaGeneratedValue().getStrategy());
	}

	public void setGenerator(@SuppressWarnings("unused")String value) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$
	}

	public void setStrategy(@SuppressWarnings("unused")GenerationType value) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$
	}

	public TextRange getGeneratorTextRange() {
		return null;
	}
}
