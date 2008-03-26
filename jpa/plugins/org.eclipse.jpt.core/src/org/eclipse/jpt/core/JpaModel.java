/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core;

import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jpt.utility.model.Model;

/**
 * The JPA model holds all the JPA projects.
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface JpaModel extends Model {

	/**
	 * Return the JPA project corresponding to the specified Eclipse project.
	 * Return null if unable to associate the specified Eclipse project
	 * with a JPA project.
	 */
	JpaProject getJpaProject(IProject project) throws CoreException;

	/**
	 * Return whether the JPA model contains a JPA project corresponding
	 * to the specified Eclipse project.
	 */
	boolean containsJpaProject(IProject project);

	/**
	 * Return the JPA model's JPA projects. This has performance implications,
	 * it will build all the JPA projects.
	 */
	Iterator<JpaProject> jpaProjects() throws CoreException;
		public static final String JPA_PROJECTS_COLLECTION = "jpaProjects";

	/**
	 * Return the size of the JPA model's list of JPA projects.
	 */
	int jpaProjectsSize();
	
	/**
	 * Return the JPA file corresponding to the specified Eclipse file,
	 * or null if unable to associate the specified file with a JPA file.
	 */
	JpaFile getJpaFile(IFile file) throws CoreException;

}
