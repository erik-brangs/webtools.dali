/*******************************************************************************
 *  Copyright (c) 2008  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.resource.persistence;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.internal.JpaConstants;
import org.eclipse.jpt.core.resource.AbstractResourceModelProvider;
import org.eclipse.jpt.core.resource.JpaResourceModelProviderManager;
import org.eclipse.jpt.core.resource.common.JpaXmlResource;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.PersistenceResource;
import org.eclipse.jpt.core.resource.persistence.XmlPersistence;
import org.eclipse.jpt.core.resource.persistence.XmlPersistenceUnit;

public class PersistenceResourceModelProvider extends AbstractResourceModelProvider
{
	/**
	 * (Convenience method) Returns a persistence resource model provider for 
	 * the given project in the default location
	 */
	public static PersistenceResourceModelProvider getDefaultModelProvider(IProject project) {
		return getModelProvider(project, JptCorePlugin.DEFAULT_PERSISTENCE_XML_FILE_PATH);
	}
	
	/**
	 * (Convenience method) Returns an persistence resource model provider for
	 * the given project in the specified location
	 */
	public static PersistenceResourceModelProvider getModelProvider(IProject project, String location) {
		return (PersistenceResourceModelProvider) JpaResourceModelProviderManager.getModelProvider(
			project, 
			new Path(JptCorePlugin.getDeploymentURI(project, location)),
			JptCorePlugin.PERSISTENCE_XML_CONTENT_TYPE);
	}
	
	public PersistenceResourceModelProvider(IProject project) {
		this(project, new Path(JptCorePlugin.DEFAULT_PERSISTENCE_XML_FILE_PATH));
	}
		
	public PersistenceResourceModelProvider(IProject project, IPath filePath) {
		super(project, filePath);
	}
	
	
	@Override
	protected String getContentTypeDescriber() {
		return JptCorePlugin.PERSISTENCE_XML_CONTENT_TYPE;
	}
	
	@Override
	protected void populateRoot(JpaXmlResource resource) {
		XmlPersistence persistence = PersistenceFactory.eINSTANCE.createXmlPersistence();
		persistence.setVersion(JpaConstants.VERSION_1_0_TEXT);
		XmlPersistenceUnit persistenceUnit = PersistenceFactory.eINSTANCE.createXmlPersistenceUnit();
		persistenceUnit.setName(getProject().getName());
		persistence.getPersistenceUnits().add(persistenceUnit);
		getResourceContents(resource).add(persistence);
	}
	
	@Override
	public PersistenceResource getResource() {
		return (PersistenceResource) super.getResource();
	}
}
