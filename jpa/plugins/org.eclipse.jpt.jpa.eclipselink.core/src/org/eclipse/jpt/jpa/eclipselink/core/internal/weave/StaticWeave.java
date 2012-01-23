/*******************************************************************************
* Copyright (c) 2012 Oracle. All rights reserved.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0, which accompanies this distribution
* and is available at http://www.eclipse.org/legal/epl-v10.html.
* 
* Contributors:
*     Oracle - initial API and implementation
*******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.weave;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jpt.common.core.internal.gen.AbstractJptGenerator;
import org.eclipse.jpt.common.utility.internal.StringTools;

/**
 *  StaticWeave
 *  Running Static Weaving from Project's classpath
 */
public class StaticWeave extends AbstractJptGenerator
{
	static public String LAUNCH_CONFIG_NAME = "Static Weaving Run Config";   //$NON-NLS-1$
	static public String WEAVING_PACKAGE_NAME = "org.eclipse.persistence.tools.weaving.jpa";   //$NON-NLS-1$
	static public String WEAVING_CLASS = WEAVING_PACKAGE_NAME + ".StaticWeave";	  //$NON-NLS-1$

	private String source;
	private String target;
	private String loglevel;
	private String persistenceinfo;
	private final String mainType;
	
	// ********** static methods **********

	public static void weave(
			IJavaProject javaProject, 
			String source, 
			String target, 
			String loglevel, 
			String persistenceinfo, 
			IProgressMonitor monitor) {
		
//		System.out.println(" ##### StaticWeave.weave() Running");	//TODELETE

		if(javaProject == null) {
			throw new NullPointerException("javaProject is null");   //$NON-NLS-1$
		}
		
		new StaticWeave(javaProject,
			source, 
			target,
			loglevel, 
			persistenceinfo).generate(monitor);
	}

	// ********** constructors **********
	
	protected StaticWeave(
			IJavaProject javaProject, 
			String source, 
			String target, 
			String loglevel, 
			String persistenceinfo) {
		super(javaProject);
		
		this.source = source;
		this.target = target;
		this.loglevel = loglevel;
		this.persistenceinfo = persistenceinfo;

		this.mainType = WEAVING_CLASS;
		
		this.setDebug(true);		// TODO remove
	}

	// ********** overrides **********
	
	@Override
	protected String getMainType() {
		return this.mainType;
	}
	
	@Override
	protected String getLaunchConfigName() {
		return LAUNCH_CONFIG_NAME;
	}

	@Override
	protected void specifyJRE() {
		// do nothing
	}

	// ********** behavior **********

	@Override
	protected void preGenerate(IProgressMonitor monitor) {
		// nothing to do yet...
	}

	// ********** Launch Configuration Setup **********

	@Override
	protected List<String> buildClasspath() throws CoreException {
		List<String> classpath = new ArrayList<String>();
		// Default Project classpath
		classpath.add(this.getDefaultProjectClasspathEntry().getMemento());
		// System Library  
		classpath.add(this.getSystemLibraryClasspathEntry().getMemento());
		// Containers classpath
		for(IRuntimeClasspathEntry containerClasspathEntry: this.getContainersClasspathEntries()) {
			classpath.add(containerClasspathEntry.getMemento());
		}
		return classpath;
	}
	
	@Override
	protected void specifyProgramArguments() {
		StringBuffer programArguments = new StringBuffer();

		if(StringTools.stringIsEmpty(this.source)) {
			throw new RuntimeException("Source directory cannot be empty");	  //$NON-NLS-1$
		}
		if(StringTools.stringIsEmpty(this.target)) {
			throw new RuntimeException("Target directory cannot be empty");	  //$NON-NLS-1$
		}

		if( ! StringTools.stringIsEmpty(this.loglevel)) {
			programArguments.append("-loglevel ");	  //$NON-NLS-1$
			programArguments.append(this.loglevel);
		}

		if( ! StringTools.stringIsEmpty(this.persistenceinfo)) {
			programArguments.append(" -persistenceinfo ");	  //$NON-NLS-1$
			programArguments.append(StringTools.quote(this.persistenceinfo));
		}

		programArguments.append(' ');
		programArguments.append(StringTools.quote(this.source));

		programArguments.append(' ');
		programArguments.append(StringTools.quote(this.target));

		this.launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, programArguments.toString());
	}

	// ********** queries **********

	private List<IRuntimeClasspathEntry> getContainersClasspathEntries() throws CoreException {
		ArrayList<IRuntimeClasspathEntry> classpathEntries = new ArrayList<IRuntimeClasspathEntry>();
		for(IClasspathEntry classpathEntry: this.javaProject.getRawClasspath()) {
			if(classpathEntry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
				IClasspathContainer container = JavaCore.getClasspathContainer(classpathEntry.getPath(), this.javaProject);
				if(container != null && container.getKind() == IClasspathContainer.K_SYSTEM) {
					classpathEntries.add( 
						JavaRuntime.newRuntimeContainerClasspathEntry(
							container.getPath(), 
							IRuntimeClasspathEntry.BOOTSTRAP_CLASSES, 
							this.javaProject));
				}
			}
		}
		return classpathEntries;
	}
}