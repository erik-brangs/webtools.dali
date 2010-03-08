/*******************************************************************************
 *  Copyright (c) 2009, 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.jpa2.context.java;

import org.eclipse.jpt.core.context.JoinColumnJoiningStrategy;
import org.eclipse.jpt.core.context.RelationshipMapping;
import org.eclipse.jpt.core.context.RelationshipReference;
import org.eclipse.jpt.core.context.java.JavaAssociationOverride;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.jpa2.context.AssociationOverrideRelationshipReference2_0;
import org.eclipse.jpt.core.resource.java.AssociationOverrideAnnotation;

/**
 * An <code>AssociationOverrideRelationshipReference</code> is a type of 
 * {@link RelationshipReference} that may utilize a 
 * {@link JoinColumnJoiningStrategy}
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @see RelationshipMapping
 * 
 * @version 2.3
 * @since 2.3
 */
public interface JavaAssociationOverrideRelationshipReference2_0 
	extends AssociationOverrideRelationshipReference2_0, JavaJpaContextNode
{
	JavaAssociationOverride getAssociationOverride();
	
	JavaJoinTableInAssociationOverrideJoiningStrategy2_0 getJoinTableJoiningStrategy();

	void initialize(AssociationOverrideAnnotation associationOverride);
	
	void update(AssociationOverrideAnnotation associationOverride);
	
}
