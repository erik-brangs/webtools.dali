/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.resource.jaxbprops;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.common.core.JptResourceModel;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.common.core.internal.utility.PlatformTools;
import org.eclipse.jpt.jaxb.core.JptJaxbCorePlugin;


public interface JaxbPropertiesResource
		extends JptResourceModel {
	
	/**
	 * The content type for <code>jaxb.properties</code> files.
	 */
	IContentType CONTENT_TYPE = JptJaxbCorePlugin.instance().getContentType("jaxbProperties"); //$NON-NLS-1$

	/**
	 * The resource type for <code>jaxb.properties</code> files.
	 */
	JptResourceType RESOURCE_TYPE = PlatformTools.getResourceType(CONTENT_TYPE);

	String getPackageName();
	
	String getProperty(String propertyName);
}
