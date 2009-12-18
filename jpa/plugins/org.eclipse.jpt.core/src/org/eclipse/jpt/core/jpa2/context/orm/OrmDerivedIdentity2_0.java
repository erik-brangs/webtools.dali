/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.jpa2.context.orm;

import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.jpa2.context.DerivedIdentity2_0;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.0
 * @since 3.0
 */
public interface OrmDerivedIdentity2_0
	extends DerivedIdentity2_0, XmlContextNode
{
	OrmSingleRelationshipMapping2_0 getMapping();

	OrmMapsIdDerivedIdentityStrategy2_0 getMapsIdDerivedIdentityStrategy();
	
	OrmIdDerivedIdentityStrategy2_0 getIdDerivedIdentityStrategy();
	
	void initializeFrom(OrmDerivedIdentity2_0 oldDerivedIdentity);
	
	void update();
}
