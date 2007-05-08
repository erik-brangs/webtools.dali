/*******************************************************************************
 * Copyright (c) 2006, 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0, which accompanies this distribution and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jpt.utility.internal.StringTools;
import org.eclipse.jst.j2ee.classpathdep.ClasspathDependencyUtil;
import org.eclipse.jst.j2ee.classpathdep.IClasspathDependencyConstants;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.FacetedProjectFramework;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

public class JpaFacetInstallDelegate 
	implements IDelegate, IJpaFacetDataModelProperties
{
	public void execute(IProject project, IProjectFacetVersion fv, 
				Object config, IProgressMonitor monitor) throws CoreException {
		
		if (monitor != null) {
			monitor.beginTask("", 1); //$NON-NLS-1$
		}

		// NB:  WTP Natures (including the JavaEMFNature)
		//  should already be added as this facet should 
		//  always coexist with a module facet.
		
		IJavaProject javaProject = JavaCore.create(project);
		
		boolean usesServerLibrary = ((IDataModel) config).getBooleanProperty(USE_SERVER_JPA_IMPLEMENTATION);
		String jpaLibrary = ((IDataModel) config).getStringProperty(JPA_LIBRARY);
		if (! usesServerLibrary && ! StringTools.stringIsEmpty(jpaLibrary)) {
			IClasspathEntry[] classpath = javaProject.getRawClasspath();
			int newLength = classpath.length + 1;
			boolean isWebApp = FacetedProjectFramework.hasProjectFacet(project, IModuleConstants.JST_WEB_MODULE);
			IClasspathAttribute[] attributes;
			if (! isWebApp && J2EEProjectUtilities.isStandaloneProject(project)) {
				attributes = new IClasspathAttribute[0];
			}
			else {
				attributes = new IClasspathAttribute[] {
					JavaCore.newClasspathAttribute(
						IClasspathDependencyConstants.CLASSPATH_COMPONENT_DEPENDENCY,
						ClasspathDependencyUtil.getDefaultRuntimePath(isWebApp).toString()
					)
				};
			}
			IClasspathEntry jpaLibraryEntry = 
				JavaCore.newContainerEntry(
					new Path(JavaCore.USER_LIBRARY_CONTAINER_ID + "/" + jpaLibrary),
					null, attributes, true);
			IClasspathEntry[] newClasspath = new IClasspathEntry[newLength];
			System.arraycopy(classpath, 0, newClasspath, 0, newLength - 1);
			newClasspath[newLength - 1] = jpaLibraryEntry;
			
			javaProject.setRawClasspath(newClasspath, monitor);
		}
			
		if (monitor != null) {
			monitor.worked(1);
		}
	}
}
