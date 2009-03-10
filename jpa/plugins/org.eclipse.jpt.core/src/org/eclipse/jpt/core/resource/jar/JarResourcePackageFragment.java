/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.resource.jar;

import java.util.Iterator;
import java.util.ListIterator;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;

/**
 * JAR package fragment
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface JarResourcePackageFragment
	extends JarResourceNode
{

	/**
	 * Return the corresponding JDT package fragement.
	 */
	IPackageFragment getPackageFragment();

	/**
	 * Return the package fragment's class files that contain "persistable" types.
	 */
	ListIterator<JarResourceClassFile> classFiles();

	/**
	 * Return the size of the package fragment's class files.
	 */
	int classFilesSize();

	/**
	 * Return the package fragment's "persistable" resource types, as defined
	 * by the JPA spec.
	 */
	Iterator<JavaResourcePersistentType> persistableTypes();

}
