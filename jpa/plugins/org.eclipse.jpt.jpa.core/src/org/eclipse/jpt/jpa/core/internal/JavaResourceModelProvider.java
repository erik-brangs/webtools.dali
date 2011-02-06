/*******************************************************************************
 * Copyright (c) 2008, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jpt.common.core.JptCommonCorePlugin;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JpaResourceModelProvider;
import org.eclipse.jpt.jpa.core.internal.resource.java.source.SourceTypeCompilationUnit;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceCompilationUnit;

/**
 * Java source code
 */
public class JavaResourceModelProvider
	implements JpaResourceModelProvider
{
	// singleton
	private static final JpaResourceModelProvider INSTANCE = new JavaResourceModelProvider();

	/**
	 * Return the singleton.
	 */
	public static JpaResourceModelProvider instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private JavaResourceModelProvider() {
		super();
	}

	public IContentType getContentType() {
		return JptCommonCorePlugin.JAVA_SOURCE_CONTENT_TYPE;
	}

	public JavaResourceCompilationUnit buildResourceModel(JpaProject jpaProject, IFile file) {
		return new SourceTypeCompilationUnit(
					JavaCore.createCompilationUnitFrom(file),
					jpaProject.getJpaPlatform().getAnnotationProvider(),
					jpaProject.getJpaPlatform().getAnnotationEditFormatter(),
					jpaProject.getModifySharedDocumentCommandExecutor()
				);
	}

}
