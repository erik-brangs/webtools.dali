/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.core.tests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.jpt.common.core.AnnotationProvider;
import org.eclipse.jpt.common.core.tests.BundleActivatorTest;
import org.eclipse.jpt.common.core.tests.internal.resource.java.JptCommonCoreResourceJavaTests;
import org.eclipse.jpt.common.core.tests.internal.utility.jdt.JptCommonCoreUtilityJdtTests;

public class JptCommonCoreTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JptCommonCoreTests.class.getPackage().getName());
		suite.addTest(JptCommonCoreResourceJavaTests.suite());
		suite.addTest(JptCommonCoreUtilityJdtTests.suite());
		suite.addTest(new BundleActivatorTest(AnnotationProvider.class));
		return suite;
	}
	
	private JptCommonCoreTests() {
		super();
		throw new UnsupportedOperationException();
	}
}
