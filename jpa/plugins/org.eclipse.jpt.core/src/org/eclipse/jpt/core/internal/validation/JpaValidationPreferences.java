/*******************************************************************************
 * Copyright (c) 2005, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.validation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jpt.core.IResourcePart;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class JpaValidationPreferences {
	
	public static String HIGH_SEVERITY = "error";
	public static String NORMAL_SEVERITY = "warning";
	public static String LOW_SEVERITY = "info";
	public static String IGNORE = "ignore";
	
	static int NO_SEVERITY_PREFERENCE = -1;
	
	/**
	 * Returns only the severity level of a given problem preference.  This does not
	 * include information on whether the problem is ignored.  See isProblemIgnored.
	 * @return an IMessage severity level
	 */
	public static int getProblemSeverityPreference(Object targetObject, String messageId) {

		IAdaptable target = (IAdaptable)targetObject;
		IResource resource = ((IResourcePart) target.getAdapter(IResourcePart.class)).getResource();
		IProject project = resource.getProject();
			
		String problemPreference = getPreference(project, messageId);
		
		if (problemPreference==null){
			return NO_SEVERITY_PREFERENCE;
		}else if (problemPreference.equals(HIGH_SEVERITY)){
			return IMessage.HIGH_SEVERITY;
		} else if (problemPreference.equals(NORMAL_SEVERITY)){
			return IMessage.NORMAL_SEVERITY;
		} else if (problemPreference.equals(LOW_SEVERITY)){
			return IMessage.LOW_SEVERITY;
		}
		return NO_SEVERITY_PREFERENCE;
	}

	/**
	 * Returns whether or not this problem should be ignored based on project or
	 * workspace preferences
	 */
	public static boolean isProblemIgnored(IProject project, String messageId){
		String problemPreference = getPreference(project, messageId);
		if (problemPreference.equals(IGNORE)){
			return true;
		}
		return false;
	}

	private static String getPreference(IProject project, String messageId) {
		String problemPreference = null;
		problemPreference = getProjectLevelProblemPreference(project, messageId);
		//if severity is still null, check the workspace preferences
		if(problemPreference==null) {
			problemPreference = getWorkspaceLevelProblemPreference(messageId);
		}
		return problemPreference;
	}
	
	/**
	 * Returns the String value of the problem preference from the project preferences
	 */
	public static String getProjectLevelProblemPreference(IProject project, String messageId){
		IEclipsePreferences projectPreferences = JptCorePlugin.getProjectPreferences(project);
		return projectPreferences.get(messageId, null);
	}
	
	public static void setProjectLevelProblemPreference(IProject project, String messageId, String problemPreference) {
		IEclipsePreferences projectPreferences = JptCorePlugin.getProjectPreferences(project);
		projectPreferences.put(messageId, problemPreference);
	}
	
	public static void removeProjectLevelProblemPreference(IProject project, String messageId){
		IEclipsePreferences projectPreferences = JptCorePlugin.getProjectPreferences(project);
		projectPreferences.remove(messageId);
	}
	
	/**
	 * Returns the String value of the problem preference from the workspace preferences
	 */
	public static String getWorkspaceLevelProblemPreference(String messageId){
		IEclipsePreferences workspacePreferences = JptCorePlugin.getWorkspacePreferences();
		return workspacePreferences.get(messageId, null);
	}
	
	public static void setWorkspaceLevelProblemPreference(String messageId, String problemPreference) {
		IEclipsePreferences workspacePreferences = JptCorePlugin.getWorkspacePreferences();
		workspacePreferences.put(messageId, problemPreference);
	}	
	
	public static void removeWorkspaceLevelProblemPreference(String messageId){
		IEclipsePreferences workspacePreferences = JptCorePlugin.getWorkspacePreferences();
		workspacePreferences.remove(messageId);
	}
}
