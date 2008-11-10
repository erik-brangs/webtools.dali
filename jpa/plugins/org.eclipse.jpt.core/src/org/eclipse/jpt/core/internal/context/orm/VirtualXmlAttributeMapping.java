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

import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.resource.common.AbstractJpaEObject;
import org.eclipse.jpt.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.core.utility.TextRange;

public abstract class VirtualXmlAttributeMapping<T extends JavaAttributeMapping> extends AbstractJpaEObject implements XmlAttributeMapping
{
	protected OrmTypeMapping ormTypeMapping;
	
	protected final T javaAttributeMapping;
	
	protected VirtualXmlAttributeMapping(OrmTypeMapping ormTypeMapping, T javaAttributeMapping) {
		super();
		this.ormTypeMapping = ormTypeMapping;
		this.javaAttributeMapping = javaAttributeMapping;
	}
	
	protected boolean isOrmMetadataComplete() {
		return this.ormTypeMapping.isMetadataComplete();
	}
	
	protected T getJavaAttributeMapping() {
		return this.javaAttributeMapping;
	}
	
	public String getMappingKey() {
		//TODO unsure about this, but don't believe we should be asking a virtual mapping for it's mapping key
		//or maybe we should be asking the javaAttributeMapping for its default or specified mapping key?
		throw new UnsupportedOperationException();
	}
	
	public String getName() {
		return this.javaAttributeMapping.getPersistentAttribute().getName();
	}

	public void setName(@SuppressWarnings("unused")String newName) {
		throw new UnsupportedOperationException("cannot set values on a virtual mapping"); //$NON-NLS-1$
	}
	
	public TextRange getNameTextRange() {
		return null;
	}

}
