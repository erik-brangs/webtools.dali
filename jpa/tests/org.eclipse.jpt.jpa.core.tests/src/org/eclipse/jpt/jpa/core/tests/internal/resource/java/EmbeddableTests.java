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
import org.eclipse.jpt.jpa.core.resource.java.EmbeddableAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.EntityAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;

@SuppressWarnings("nls")
public class EmbeddableTests extends JpaJavaResourceModelTestCase {

	public EmbeddableTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestEmbeddable() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.EMBEDDABLE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Embeddable");
			}
		});
	}
	
	private ICompilationUnit createTestEmbeddableAndEntity() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.EMBEDDABLE, JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
				sb.append("@Embeddable");
			}
		});
	}

	public void testEmbeddable() throws Exception {
		ICompilationUnit cu = this.createTestEmbeddable();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		JavaResourceNode mappingAnnotation = typeResource.getAnnotation(EmbeddableAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof EmbeddableAnnotation);
	}
	
	public void testEmbeddableAndEntity() throws Exception {
		ICompilationUnit cu = this.createTestEmbeddableAndEntity();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		JavaResourceNode mappingAnnotation = typeResource.getAnnotation(EmbeddableAnnotation.ANNOTATION_NAME);
		assertTrue(mappingAnnotation instanceof EmbeddableAnnotation);
		
		EntityAnnotation entity = (EntityAnnotation) typeResource.getAnnotation(JPA.ENTITY);
		assertNotNull(entity);
	}

}