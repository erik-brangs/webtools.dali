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

import org.eclipse.jpt.core.context.Column;
import org.eclipse.jpt.core.resource.orm.XmlColumn;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmColumn extends Column, OrmAbstractColumn
{
	
	Owner owner();
	
	void initializeFrom(Column oldColumn);
	void initialize(XmlColumn column);
	void update(XmlColumn column);

		/**
	 * interface allowing columns to be used in multiple places
	 * (e.g. basic mappings and attribute overrides)
	 */
	interface Owner extends OrmAbstractColumn.Owner
	{
		XmlColumn columnResource();
		
		void addColumnResource();
		
		void removeColumnResource();
	}

}