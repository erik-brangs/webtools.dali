/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.jpa.core.resource.java.OrderByAnnotation;

@SuppressWarnings("nls")
public class OrderByTests extends JpaJavaResourceModelTestCase {

	public OrderByTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestOrderBy() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ORDER_BY);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@OrderBy");
			}
		});
	}
	
	private ICompilationUnit createTestOrderByWithValue() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ORDER_BY);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@OrderBy(value = \"key\")");
			}
		});
	}

	public void testOrderBy() throws Exception {
		ICompilationUnit cu = this.createTestOrderBy();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		OrderByAnnotation orderBy = (OrderByAnnotation) attributeResource.getAnnotation(JPA.ORDER_BY);
		assertNotNull(orderBy);
	}
	
	public void testGetValue() throws Exception {
		ICompilationUnit cu = this.createTestOrderByWithValue();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();
		
		OrderByAnnotation orderBy = (OrderByAnnotation) attributeResource.getAnnotation(JPA.ORDER_BY);
		assertEquals("key", orderBy.getValue());
	}
	
	public void testSetValue() throws Exception {
		ICompilationUnit cu = this.createTestOrderBy();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		JavaResourcePersistentAttribute attributeResource = typeResource.fields().next();

		OrderByAnnotation orderBy = (OrderByAnnotation) attributeResource.getAnnotation(JPA.ORDER_BY);

		orderBy.setValue("foo");
		
		assertSourceContains("@OrderBy(\"foo\")", cu);
		
		orderBy.setValue(null);
		
		assertSourceContains("@OrderBy", cu);
	}

}