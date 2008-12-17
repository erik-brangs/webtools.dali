/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import org.eclipse.jpt.core.context.MappingFile;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmXml
	extends XmlContextNode, MappingFile
{
	/**
	 * covariant override
	 */
	MappingFileRef getParent();

	String getType();


	// ********** entity mappings **********

	/**
	 * String constant associated with changes to the entity-mappings property
	 */
	public final static String ENTITY_MAPPINGS_PROPERTY = "entityMappings"; //$NON-NLS-1$

	/** 
	 * Return the content represented by the root of the orm.xml file.
	 * This may be null.
	 */
	EntityMappings getEntityMappings();

	/**
	 * Add a entity-mappings node to the orm.xml file and return the object 
	 * representing it.
	 * Throws {@link IllegalStateException} if a entity-mappings node already exists.
	 */
	EntityMappings addEntityMappings();

	/**
	 * Remove the entity-mappings node from the orm.xml file.
	 * Throws {@link IllegalStateException} if a persistence node does not exist.
	 */
	void removeEntityMappings();

}
