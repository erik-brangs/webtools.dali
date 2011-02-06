/*******************************************************************************
 *  Copyright (c) 2010, 2011  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.core.libprov;

import org.eclipse.jpt.common.core.libprov.JptLibraryProviderInstallOperationConfig;
import org.eclipse.jpt.jpa.core.platform.JpaPlatformDescription;


public interface JpaLibraryProviderInstallOperationConfig 
		extends JptLibraryProviderInstallOperationConfig {
	
	public static final String PROP_JPA_PLATFORM = "JPA_PLATFORM"; //$NON-NLS-1$
	
	JpaPlatformDescription getJpaPlatform();
	
	void setJpaPlatform(JpaPlatformDescription jpaPlatform);
}
