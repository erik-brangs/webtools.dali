/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.utility.jdt;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jpt.core.internal.utility.jdt.JDTFieldAttribute;
import org.eclipse.jpt.core.internal.utility.jdt.JDTMethodAttribute;
import org.eclipse.jpt.core.internal.utility.jdt.JDTTools;
import org.eclipse.jpt.core.internal.utility.jdt.JDTType;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject.SourceWriter;
import org.eclipse.jpt.core.utility.jdt.Type;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.iterators.SingleElementIterator;
import org.eclipse.jpt.utility.tests.internal.TestTools;

/**
 * Provide an easy(?) way to build an annotated source file.
 * The type must be created by calling one of the #createType() methods
 * before calling any of the various helper methods (i.e. the type is *not*
 * created during #setUp()).
 */
@SuppressWarnings("nls")
public abstract class AnnotationTestCase extends TestCase {
	protected TestJavaProject javaProject;

	public static final String CR = System.getProperty("line.separator");
	public static final String SEP = File.separator;
	public static final String PROJECT_NAME = "AnnotationTestProject";
	public static final String PACKAGE_NAME = "test";
	public static final String TYPE_NAME = "AnnotationTestType";
	public static final String FULLY_QUALIFIED_TYPE_NAME = PACKAGE_NAME + "." + TYPE_NAME;
	public static final String FILE_NAME = TYPE_NAME + ".java";
	public static final IPath FILE_PATH = new Path("src" + SEP + PACKAGE_NAME + SEP + FILE_NAME);

	public static final String[] EMPTY_STRING_ARRAY = new String[0];


	// ********** TestCase behavior **********

	protected AnnotationTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.javaProject = this.buildJavaProject(false);  // false = no auto-build
	}
	
	protected TestJavaProject buildJavaProject(boolean autoBuild) throws Exception {
		return this.buildJavaProject(PROJECT_NAME, autoBuild);
	}
	
	protected TestJavaProject buildJavaProject(String projectName, boolean autoBuild) throws Exception {
		return new TestJavaProject(projectName, autoBuild);
	}

	@Override
	protected void tearDown() throws Exception {
//		this.dumpSource();
		this.deleteProject();
		TestTools.clear(this);
		super.tearDown();
	}
	
	protected void deleteProject() throws Exception {
		int i = 1;
		boolean deleted = false;
		while ( ! deleted) {
			try {
				this.javaProject.getProject().delete(true, true, null);
				deleted = true;
			} catch (CoreException ex) {
				if (i == 4) {
					throw ex;
				}
				Thread.sleep(1000);
				i++;
			}
		}
	}

	protected void dumpSource(ICompilationUnit cu) throws Exception {
		System.out.println("*** " + this.getName() + " ****");
		System.out.println(this.getSource(cu));
		System.out.println();
	}


	// ********** type creation **********

	/**
	 * create an un-annotated type
	 */
	protected ICompilationUnit createTestType() throws CoreException {
		return this.createTestType(new DefaultAnnotationWriter());
	}

	/**
	 * shortcut for simply adding an annotation to the 'id' field
	 */
	protected ICompilationUnit createTestType(final String annotationImport, final String idFieldAnnotation) throws CoreException {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return (annotationImport == null) ?
					EmptyIterator.<String>instance()
				:
					new SingleElementIterator<String>(annotationImport);
			}
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append(idFieldAnnotation);
			}
		});
	}

	/**
	 * shortcut for simply adding a fully-qualified annotation to the 'id' field
	 */
	protected ICompilationUnit createTestType(final String idFieldAnnotation) throws CoreException {
		return this.createTestType(null, idFieldAnnotation);
	}

	
	protected ICompilationUnit createTestType(AnnotationWriter annotationWriter) throws CoreException {
		return this.javaProject.createCompilationUnit(PACKAGE_NAME, FILE_NAME, this.createSourceWriter(annotationWriter));
	}
	
	protected ICompilationUnit createTestType(String packageName, String fileName, String typeName, AnnotationWriter annotationWriter) throws CoreException {
		return this.javaProject.createCompilationUnit(packageName, fileName, this.createSourceWriter(annotationWriter, typeName));
	}

	protected SourceWriter createSourceWriter(AnnotationWriter annotationWriter) {
		return new AnnotatedSourceWriter(annotationWriter);
	}
	
	protected SourceWriter createSourceWriter(AnnotationWriter annotationWriter, String typeName) {
		return new AnnotatedSourceWriter(annotationWriter, typeName);
	}

	protected void appendSourceTo(StringBuilder sb, AnnotationWriter annotationWriter, String typeName) {
		sb.append(CR);
		for (Iterator<String> stream = annotationWriter.imports(); stream.hasNext(); ) {
			sb.append("import ");
			sb.append(stream.next());
			sb.append(";");
			sb.append(CR);
		}
		sb.append(CR);
		annotationWriter.appendTypeAnnotationTo(sb);
		sb.append(CR);
		sb.append("public class ").append(typeName).append(" ");
		annotationWriter.appendExtendsImplementsTo(sb);
		sb.append("{").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendIdFieldAnnotationTo(sb);
		sb.append(CR);
		sb.append("    private int id;").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendNameFieldAnnotationTo(sb);
		sb.append(CR);
		sb.append("    private String name;").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendGetIdMethodAnnotationTo(sb);
		sb.append(CR);
		sb.append("    public int getId() {").append(CR);
		sb.append("        return this.id;").append(CR);
		sb.append("    }").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendSetIdMethodAnnotationTo(sb);
		sb.append(CR);
		sb.append("    public void setId(int id) {").append(CR);
		sb.append("        this.id = id;").append(CR);
		sb.append("    }").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendGetNameMethodAnnotationTo(sb);
		sb.append(CR);
		sb.append("    public String getName() {").append(CR);
		sb.append("        return this.name;").append(CR);
		sb.append("    }").append(CR);
		sb.append(CR);
		sb.append("    ");
		annotationWriter.appendSetNameMethodAnnotationTo(sb);
		sb.append(CR);
		sb.append("    public void setTestField(String testField) {").append(CR);
		sb.append("        this.testField = testField;").append(CR);
		sb.append("    }").append(CR);
		sb.append(CR);
		annotationWriter.appendMemberTypeTo(sb);
		sb.append(CR);		
		sb.append("}").append(CR);
		annotationWriter.appendTopLevelTypesTo(sb);
		sb.append(CR);
	}


	// ********** queries **********

	protected TestJavaProject getJavaProject() {
		return this.javaProject;
	}

	protected JDTType testType(ICompilationUnit cu) {
		return this.buildType(TYPE_NAME, cu);
	}

	protected JDTType buildType(String name, ICompilationUnit cu) {
		return this.buildType(name, 1, cu);
	}

	protected JDTType buildType(String name, int occurrence, ICompilationUnit cu) {
		return this.buildType(null, name, occurrence, cu);
	}

	protected JDTType buildType(Type declaringType, String name, int occurrence, ICompilationUnit cu) {
		return new JDTType(declaringType, name, occurrence, cu);
	}

	protected JDTFieldAttribute idField(ICompilationUnit cu) {
		return this.buildField("id", cu);
	}

	protected JDTFieldAttribute nameField(ICompilationUnit cu) {
		return this.buildField("name", cu);
	}

	protected JDTFieldAttribute buildField(String name, ICompilationUnit cu) {
		return this.buildField(name, 1, cu);
	}

	protected JDTFieldAttribute buildField(String name, int occurrence, ICompilationUnit cu) {
		return this.buildField(this.testType(cu), name, occurrence, cu);
	}

	protected JDTFieldAttribute buildField(Type declaringType, String name, int occurrence, ICompilationUnit cu) {
		return new JDTFieldAttribute(declaringType, name, occurrence, cu);
	}

	protected JDTMethodAttribute idGetMethod(ICompilationUnit cu) {
		return this.buildMethod("getId", cu);
	}
	
	protected JDTMethodAttribute idSetMethod(ICompilationUnit cu) {
		return this.buildMethod("setId", new String[] {"int"}, cu);
	}

	protected JDTMethodAttribute nameGetMethod(ICompilationUnit cu) {
		return this.buildMethod("getName", cu);
	}

	protected JDTMethodAttribute buildMethod(String name, ICompilationUnit cu) {
		return this.buildMethod(name, EMPTY_STRING_ARRAY, cu);
	}

	protected JDTMethodAttribute buildMethod(String name, String[] parameterTypeNames, ICompilationUnit cu) {
		return this.buildMethod(name, parameterTypeNames, 1, cu);
	}

	protected JDTMethodAttribute buildMethod(String name, String[] parameterTypeNames, int occurrence, ICompilationUnit cu) {
		return new JDTMethodAttribute(this.testType(cu), name, parameterTypeNames, occurrence, cu);
	}

	protected JDTMethodAttribute buildMethod(Type declaringType, String name, String[] parameterTypeNames, int occurrence, ICompilationUnit cu) {
		return new JDTMethodAttribute(declaringType, name, parameterTypeNames, occurrence, cu);
	}

	protected String getSource(ICompilationUnit cu) throws JavaModelException {
		return cu.getBuffer().getContents();
	}

	protected CompilationUnit buildASTRoot(ICompilationUnit cu) {
		return JDTTools.buildASTRoot(cu);
	}


	// ********** test validation **********

	protected void assertSourceContains(String s, ICompilationUnit cu) throws JavaModelException {
		String source = this.getSource(cu);
		boolean found = source.indexOf(s) > -1;
		if ( ! found) {
			String msg = "source does not contain the expected string: " + s + " (see System console)";
			System.out.println("*** " + this.getName() + " ****");
			System.out.println(msg);
			System.out.println(source);
			System.out.println();
			fail(msg);
		}
	}

	protected void assertSourceDoesNotContain(String s, ICompilationUnit cu) throws JavaModelException {
		String source = this.getSource(cu);
		int pos = source.indexOf(s);
		if (pos != -1) {
			String msg = "unexpected string in source (position: " + pos + "): " + s + " (see System console)";
			System.out.println("*** " + this.getName() + " ****");
			System.out.println(msg);
			System.out.println(source);
			System.out.println();
			fail(msg);
		}
	}


	// ********** manipulate annotations **********

	/**
	 * Return the *first* member value pair for the specified annotation element
	 * with the specified name.
	 * Return null if the annotation has no such element.
	 */
	protected MemberValuePair memberValuePair(NormalAnnotation annotation, String elementName) {
		for (MemberValuePair pair : this.values(annotation)) {
			if (pair.getName().getFullyQualifiedName().equals(elementName)) {
				return pair;
			}
		}
		return null;
	}

	/**
	 * minimize the scope of the suppressed warnings
	 */
	@SuppressWarnings("unchecked")
	protected List<MemberValuePair> values(NormalAnnotation na) {
		return na.values();
	}

	/**
	 * check for null member value pair
	 */
	protected Expression value_(MemberValuePair pair) {
		return (pair == null) ? null : pair.getValue();
	}

	/**
	 * Return the value of the *first* annotation element
	 * with the specified name.
	 * Return null if the annotation has no such element.
	 */
	protected Expression annotationElementValue(NormalAnnotation annotation, String elementName) {
		return this.value_(this.memberValuePair(annotation, elementName));
	}

	/**
	 * Return the value of the *first* annotation element
	 * with the specified name.
	 * Return null if the annotation has no such element.
	 */
	protected Expression annotationElementValue(SingleMemberAnnotation annotation, String elementName) {
		return elementName.equals("value") ? annotation.getValue() : null;
	}

	/**
	 * Return the value of the *first* annotation element
	 * with the specified name.
	 * Return null if the annotation has no such element.
	 * (An element name of "value" will return the value of a single
	 * member annotation.)
	 */
	protected Expression annotationElementValue(Annotation annotation, String elementName) {
		if (annotation.isNormalAnnotation()) {
			return this.annotationElementValue((NormalAnnotation) annotation, elementName);
		}
		if (annotation.isSingleMemberAnnotation()) {
			return this.annotationElementValue((SingleMemberAnnotation) annotation, elementName);
		}
		return null;
	}

	/**
	 * Build a number literal and set its initial value to the specified literal.
	 */
	protected NumberLiteral newNumberLiteral(AST ast, int value) {
		return ast.newNumberLiteral(Integer.toString(value));
	}

	/**
	 * Build a string literal and set its initial value.
	 */
	protected StringLiteral newStringLiteral(AST ast, String value) {
		StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(value);
		return stringLiteral;
	}

	protected MemberValuePair newMemberValuePair(AST ast, SimpleName name, Expression value) {
		MemberValuePair pair = ast.newMemberValuePair();
		pair.setName(name);
		pair.setValue(value);
		return pair;
	}

	protected MemberValuePair newMemberValuePair(AST ast, String name, Expression value) {
		return this.newMemberValuePair(ast, ast.newSimpleName(name), value);
	}

	protected MemberValuePair newMemberValuePair(AST ast, String name, String value) {
		return this.newMemberValuePair(ast, name, this.newStringLiteral(ast, value));
	}

	protected MemberValuePair newMemberValuePair(AST ast, String name, int value) {
		return this.newMemberValuePair(ast, name, this.newNumberLiteral(ast, value));
	}

	/**
	 * Add the specified member value pair to the specified annotation.
	 * Return the resulting annotation.
	 */
	protected NormalAnnotation addMemberValuePair(NormalAnnotation annotation, MemberValuePair pair) {
		this.values(annotation).add(pair);
		return annotation;
	}

	/**
	 * Add the specified member value pair to the specified annotation.
	 * Return the resulting annotation.
	 */
	protected NormalAnnotation addMemberValuePair(NormalAnnotation annotation, String name, int value) {
		return this.addMemberValuePair(annotation, this.newMemberValuePair(annotation.getAST(), name, value));
	}

	/**
	 * Add the specified member value pair to the specified annotation.
	 * Return the resulting annotation.
	 */
	protected NormalAnnotation addMemberValuePair(NormalAnnotation annotation, String name, String value) {
		return this.addMemberValuePair(annotation, this.newMemberValuePair(annotation.getAST(), name, value));
	}


	// ********** member classes **********

	public interface AnnotationWriter {
		Iterator<String> imports();
		void appendTypeAnnotationTo(StringBuilder sb);
		void appendExtendsImplementsTo(StringBuilder sb);
		void appendIdFieldAnnotationTo(StringBuilder sb);
		void appendNameFieldAnnotationTo(StringBuilder sb);
		void appendGetIdMethodAnnotationTo(StringBuilder sb);
		void appendSetIdMethodAnnotationTo(StringBuilder sb);
		void appendGetNameMethodAnnotationTo(StringBuilder sb);
		void appendSetNameMethodAnnotationTo(StringBuilder sb);
		void appendMemberTypeTo(StringBuilder sb);
		void appendTopLevelTypesTo(StringBuilder sb);
	}

	public static class DefaultAnnotationWriter implements AnnotationWriter {
		public Iterator<String> imports() {return EmptyIterator.instance();}
		public void appendTypeAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendExtendsImplementsTo(StringBuilder sb) {/* do nothing */}
		public void appendIdFieldAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendNameFieldAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendGetIdMethodAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendSetIdMethodAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendGetNameMethodAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendSetNameMethodAnnotationTo(StringBuilder sb) {/* do nothing */}
		public void appendMemberTypeTo(StringBuilder sb) {/* do nothing */}
		public void appendTopLevelTypesTo(StringBuilder sb) {/* do nothing */}
	}

	public static class AnnotationWriterWrapper implements AnnotationWriter {
		private final AnnotationWriter aw;
		public AnnotationWriterWrapper(AnnotationWriter aw) {
			super();
			this.aw = aw;
		}
		public Iterator<String> imports() {return aw.imports();}
		public void appendTypeAnnotationTo(StringBuilder sb) {aw.appendTypeAnnotationTo(sb);}
		public void appendExtendsImplementsTo(StringBuilder sb) {aw.appendExtendsImplementsTo(sb);}
		public void appendIdFieldAnnotationTo(StringBuilder sb) {aw.appendIdFieldAnnotationTo(sb);}
		public void appendNameFieldAnnotationTo(StringBuilder sb) {aw.appendNameFieldAnnotationTo(sb);}
		public void appendGetIdMethodAnnotationTo(StringBuilder sb) {aw.appendGetIdMethodAnnotationTo(sb);}
		public void appendSetIdMethodAnnotationTo(StringBuilder sb) {aw.appendSetIdMethodAnnotationTo(sb);}
		public void appendGetNameMethodAnnotationTo(StringBuilder sb) {aw.appendGetNameMethodAnnotationTo(sb);}
		public void appendSetNameMethodAnnotationTo(StringBuilder sb) {aw.appendSetNameMethodAnnotationTo(sb);}
		public void appendMemberTypeTo(StringBuilder sb) {aw.appendMemberTypeTo(sb);}
		public void appendTopLevelTypesTo(StringBuilder sb) {aw.appendTopLevelTypesTo(sb);}
	}

	public class AnnotatedSourceWriter implements SourceWriter {
		private AnnotationWriter annotationWriter;
		private String typeName;
		public AnnotatedSourceWriter(AnnotationWriter annotationWriter) {
			this(annotationWriter, TYPE_NAME);
		}
		public AnnotatedSourceWriter(AnnotationWriter annotationWriter, String typeName) {
			super();
			this.annotationWriter = annotationWriter;
			this.typeName = typeName;
		}
		public void appendSourceTo(StringBuilder sb) {
			AnnotationTestCase.this.appendSourceTo(sb, this.annotationWriter, typeName);
		}
	}

}
