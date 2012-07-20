/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

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
public class JaxbFacet {
	
	public static final String ID = "jpt.jaxb"; //$NON-NLS-1$
	
	public static final IProjectFacet FACET = ProjectFacetsManager.getProjectFacet(ID);
	
	public static final IProjectFacetVersion VERSION_2_1 = FACET.getVersion("2.1"); //$NON-NLS-1$
	
	public static final IProjectFacetVersion VERSION_2_2 = FACET.getVersion("2.2"); //$NON-NLS-1$
	
	
	public static boolean isInstalled(final IProject project) {
		try {
			return FacetedProjectFramework.hasProjectFacet(project, ID);
		}
		catch (CoreException e) {
			JptJaxbCorePlugin.instance().logError(e);
			return false;
		}
	}
	
	
	/**
	 * Not for instantiation
	 */
	private JaxbFacet() {}
}
