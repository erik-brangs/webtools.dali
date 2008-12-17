/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.orm;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jpt.core.internal.resource.orm.translators.EntityMappingsTranslator;
import org.eclipse.jpt.core.resource.common.JpaXmlResource;
import org.eclipse.wst.common.internal.emf.resource.Renderer;
import org.eclipse.wst.common.internal.emf.resource.Translator;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public class OrmResource
	extends JpaXmlResource 
{
	public static final String TYPE = "generic"; //$NON-NLS-1$

	public OrmResource(URI uri, Renderer renderer) {
		super(uri, renderer);
	}

	public XmlEntityMappings getEntityMappings() {
		return (XmlEntityMappings) this.getRootObject();
	}

	public Translator getRootTranslator() {
		return EntityMappingsTranslator.INSTANCE;
	}

	public String getType() {
		return TYPE;
	}

}
