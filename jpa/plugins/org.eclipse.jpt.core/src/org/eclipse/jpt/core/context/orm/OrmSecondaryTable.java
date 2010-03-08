/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.SecondaryTable;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.resource.orm.XmlSecondaryTable;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.1
 * @since 2.0
 */
public interface OrmSecondaryTable
	extends SecondaryTable, XmlContextNode
{

	/**
	 * Update the OrmSecondaryTable context model object to match the XmlSecondaryTable 
	 * resource model object. see {@link org.eclipse.jpt.core.JpaProject#update()}
	 */
	void update(XmlSecondaryTable secondaryTable);

	void initializeFrom(SecondaryTable oldSecondaryTable);

	
	//************ covariant overrides *************

	OrmEntity getParent();
	
	@SuppressWarnings("unchecked")
	ListIterator<OrmPrimaryKeyJoinColumn> primaryKeyJoinColumns();
	
	OrmPrimaryKeyJoinColumn getDefaultPrimaryKeyJoinColumn();
	
	@SuppressWarnings("unchecked")
	ListIterator<OrmPrimaryKeyJoinColumn> specifiedPrimaryKeyJoinColumns();
	
	OrmPrimaryKeyJoinColumn addSpecifiedPrimaryKeyJoinColumn(int index);
	
	@SuppressWarnings("unchecked")
	ListIterator<OrmUniqueConstraint> uniqueConstraints();
	
	OrmUniqueConstraint addUniqueConstraint(int index);
}