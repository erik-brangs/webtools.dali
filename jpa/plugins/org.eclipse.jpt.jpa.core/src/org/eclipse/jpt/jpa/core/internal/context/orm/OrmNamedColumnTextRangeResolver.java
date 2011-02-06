/*******************************************************************************
 *  Copyright (c) 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.orm;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.jpa.core.context.orm.OrmNamedColumn;
import org.eclipse.jpt.jpa.core.internal.context.NamedColumnTextRangeResolver;

public class OrmNamedColumnTextRangeResolver
	implements NamedColumnTextRangeResolver
{
	protected final OrmNamedColumn ormNamedColumn;

	public OrmNamedColumnTextRangeResolver(OrmNamedColumn javaNamedColumn) {
		this.ormNamedColumn = javaNamedColumn;
	}

	protected OrmNamedColumn getColumn() {
		return this.ormNamedColumn;
	}
	
	public TextRange getNameTextRange() {
		return this.ormNamedColumn.getNameTextRange();
	}
}
