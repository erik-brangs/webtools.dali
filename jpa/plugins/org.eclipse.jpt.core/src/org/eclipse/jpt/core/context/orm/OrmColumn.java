/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import org.eclipse.jpt.core.context.AbstractColumn;
import org.eclipse.jpt.core.context.Column;
import org.eclipse.jpt.core.resource.orm.XmlColumn;

public interface OrmColumn extends Column, OrmJpaContextNode
{
	
	Owner owner();
	
	void initializeFrom(Column oldColumn);
	void initialize(XmlColumn column);
	void update(XmlColumn column);

		/**
	 * interface allowing columns to be used in multiple places
	 * (e.g. basic mappings and attribute overrides)
	 */
	interface Owner extends AbstractColumn.Owner
	{
		XmlColumn columnResource();
		
		void addColumnResource();
		
		void removeColumnResource();
	}

}