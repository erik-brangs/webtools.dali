/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.resource.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jpt.core.internal.utility.jdt.JDTFieldAttribute;
import org.eclipse.jpt.core.internal.utility.jdt.JDTMethodAttribute;
import org.eclipse.jpt.core.resource.java.AccessAnnotation;
import org.eclipse.jpt.core.resource.java.AccessType;
import org.eclipse.jpt.core.resource.java.Annotation;
import org.eclipse.jpt.core.resource.java.JavaResourceCompilationUnit;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.utility.jdt.Attribute;
import org.eclipse.jpt.core.utility.jdt.MethodAttribute;
import org.eclipse.jpt.core.utility.jdt.Type;
import org.eclipse.jpt.utility.MethodSignature;
import org.eclipse.jpt.utility.internal.ClassTools;
import org.eclipse.jpt.utility.internal.iterators.CloneIterator;
import org.eclipse.jpt.utility.internal.iterators.CloneListIterator;

/**
 * Java source persistent attribute (field or property)
 */
public class JavaResourcePersistentAttributeImpl
	extends AbstractJavaResourcePersistentMember<Attribute>
	implements JavaResourcePersistentAttribute
{
	private int modifiers;

	private String typeName;

	private boolean typeIsInterface;

	private boolean typeIsEnum;

	private final Vector<String> typeSuperclassNames = new Vector<String>();

	private final Vector<String> typeInterfaceNames = new Vector<String>();

	private final Vector<String> typeTypeArgumentNames = new Vector<String>();


	/**
	 * construct field attribute
	 */
	public static JavaResourcePersistentAttribute newInstance(
			JavaResourcePersistentType parent,
			Type declaringType,
			String name,
			int occurrence,
			JavaResourceCompilationUnit javaResourceCompilationUnit,
			CompilationUnit astRoot) {
		Attribute attribute = new JDTFieldAttribute(
				declaringType,
				name,
				occurrence,
				javaResourceCompilationUnit.getCompilationUnit(),
				javaResourceCompilationUnit.getModifySharedDocumentCommandExecutor(),
				javaResourceCompilationUnit.getAnnotationEditFormatter());
		JavaResourcePersistentAttribute jrpa = new JavaResourcePersistentAttributeImpl(parent, attribute);
		jrpa.initialize(astRoot);
		return jrpa;
	}

	/**
	 * construct property attribute
	 */
	public static JavaResourcePersistentAttribute newInstance(
			JavaResourcePersistentType parent,
			Type declaringType,
			MethodSignature signature,
			int occurrence,
			JavaResourceCompilationUnit javaResourceCompilationUnit,
			CompilationUnit astRoot) {
		Attribute attribute = JDTMethodAttribute.newInstance(
				declaringType,
				signature,
				occurrence,
				javaResourceCompilationUnit.getCompilationUnit(),
				javaResourceCompilationUnit.getModifySharedDocumentCommandExecutor(),
				javaResourceCompilationUnit.getAnnotationEditFormatter());
		JavaResourcePersistentAttribute jrpa = new JavaResourcePersistentAttributeImpl(parent, attribute);
		jrpa.initialize(astRoot);
		return jrpa;
	}

	public JavaResourcePersistentAttributeImpl(JavaResourcePersistentType parent, Attribute attribute){
		super(parent, attribute);
	}

	@Override
	public void initialize(CompilationUnit astRoot) {
		super.initialize(astRoot);
		this.modifiers = this.buildModifiers(astRoot);
		this.typeName = this.buildTypeName(astRoot);
		this.typeIsInterface = this.buildTypeIsInterface(astRoot);
		this.typeIsEnum = this.buildTypeIsEnum(astRoot);
		this.typeSuperclassNames.addAll(this.buildTypeSuperclassNames(astRoot));
		this.typeInterfaceNames.addAll(this.buildTypeInterfaceNames(astRoot));
		this.typeTypeArgumentNames.addAll(this.buildTypeTypeArgumentNames(astRoot));
	}


	// ******** overrides ********

	@Override
	public void resolveTypes(CompilationUnit astRoot) {
		super.resolveTypes(astRoot);
		this.setTypeName(this.buildTypeName(astRoot));
		this.setTypeSuperclassNames(this.buildTypeSuperclassNames(astRoot));
		this.setTypeInterfaceNames(this.buildTypeInterfaceNames(astRoot));
		this.setTypeTypeArgumentNames(this.buildTypeTypeArgumentNames(astRoot));
	}

	@Override
	public void update(CompilationUnit astRoot) {
		super.update(astRoot);
		this.setModifiers(this.buildModifiers(astRoot));
		this.setTypeName(this.buildTypeName(astRoot));
		this.setTypeIsInterface(this.buildTypeIsInterface(astRoot));
		this.setTypeIsEnum(this.buildTypeIsEnum(astRoot));
		this.setTypeSuperclassNames(this.buildTypeSuperclassNames(astRoot));
		this.setTypeInterfaceNames(this.buildTypeInterfaceNames(astRoot));
		this.setTypeTypeArgumentNames(this.buildTypeTypeArgumentNames(astRoot));
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.getName());
	}


	// ******** AbstractJavaResourcePersistentMember implementation ********

	@Override
	protected Annotation buildMappingAnnotation(String mappingAnnotationName) {
		return this.getAnnotationProvider().buildAttributeMappingAnnotation(this, this.getMember(), mappingAnnotationName);
	}

	@Override
	protected Annotation buildSupportingAnnotation(String annotationName) {
		return this.getAnnotationProvider().buildAttributeSupportingAnnotation(this, this.getMember(), annotationName);
	}

	@Override
	protected Annotation buildNullSupportingAnnotation(String annotationName) {
		return this.getAnnotationProvider().buildNullAttributeSupportingAnnotation(this, this.getMember(), annotationName);
	}

	@Override
	protected Annotation buildNullMappingAnnotation(String annotationName) {
		return this.getAnnotationProvider().buildNullAttributeMappingAnnotation(this, this.getMember(), annotationName);
	}

	@Override
	protected ListIterator<String> validMappingAnnotationNames() {
		return this.getAnnotationProvider().attributeMappingAnnotationNames();
	}

	@Override
	protected ListIterator<String> validSupportingAnnotationNames() {
		return this.getAnnotationProvider().attributeSupportingAnnotationNames();
	}

	public boolean isFor(MethodSignature signature, int occurrence) {
		return ((MethodAttribute) this.getMember()).matches(signature, occurrence);
	}


	// ******** JavaResourcePersistentAttribute implementation ********

	public String getName() {
		return this.getMember().getAttributeName();
	}

	public boolean isField() {
		return this.getMember().isField();
	}

	public boolean isProperty() {
		return ! this.isField();
	}

	public boolean hasAnyPersistenceAnnotations() {
		return (this.mappingAnnotationsSize() > 0)
				|| (this.supportingAnnotationsSize() > 0);
	}

	public AccessType getSpecifiedAccess() {
		AccessAnnotation accessAnnotation = (AccessAnnotation) this.getSupportingAnnotation(AccessAnnotation.ANNOTATION_NAME);
		return (accessAnnotation == null) ? null : accessAnnotation.getValue();
	}

	public boolean typeIsSubTypeOf(String tn) {
		if (this.typeName == null) {
			return false;
		}
		return this.typeName.equals(tn)
				|| this.typeInterfaceNames.contains(tn)
				|| this.typeSuperclassNames.contains(tn);
	}

	public boolean typeIsVariablePrimitive() {
		return (this.typeName != null) && ClassTools.classNamedIsVariablePrimitive(this.typeName);
	}

	protected ITypeBinding getTypeBinding(CompilationUnit astRoot) {
		return this.getMember().getTypeBinding(astRoot);
	}

	// ***** modifiers
	public int getModifiers() {
		return this.modifiers;
	}

	protected void setModifiers(int modifiers) {
		int old = this.modifiers;
		this.modifiers = modifiers;
		this.firePropertyChanged(MODIFIERS_PROPERTY, old, modifiers);
	}

	/**
	 * zero seems like a reasonable default...
	 */
	protected int buildModifiers(CompilationUnit astRoot) {
		IBinding binding = this.getMember().getBinding(astRoot);
		return (binding == null) ? 0 : binding.getModifiers();
	}

	// ***** type name
	public String getTypeName() {
		return this.typeName;
	}

	protected void setTypeName(String typeName) {
		String old = this.typeName;
		this.typeName = typeName;
		this.firePropertyChanged(TYPE_NAME_PROPERTY, old, typeName);
	}

	/**
	 * this can be an array (e.g. "java.lang.String[]");
	 * but no generic type arguments
	 */
	protected String buildTypeName(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		return (typeBinding == null) ? null : typeBinding.getTypeDeclaration().getQualifiedName();
	}

	// ***** type is interface
	public boolean typeIsInterface() {
		return this.typeIsInterface;
	}

	protected void setTypeIsInterface(boolean typeIsInterface) {
		boolean old = this.typeIsInterface;
		this.typeIsInterface = typeIsInterface;
		this.firePropertyChanged(TYPE_IS_INTERFACE_PROPERTY, old, typeIsInterface);
	}

	protected boolean buildTypeIsInterface(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		return (typeBinding != null) && ( ! typeBinding.isArray()) && typeBinding.isInterface();
	}

	// ***** type is enum
	public boolean typeIsEnum() {
		return this.typeIsEnum;
	}

	protected void setTypeIsEnum(boolean typeIsEnum) {
		boolean old = this.typeIsEnum;
		this.typeIsEnum = typeIsEnum;
		this.firePropertyChanged(TYPE_IS_ENUM_PROPERTY, old, typeIsEnum);
	}

	protected boolean buildTypeIsEnum(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		return (typeBinding != null) && ( ! typeBinding.isArray()) && typeBinding.isEnum();
	}

	// ***** type superclass hierarchy
	public ListIterator<String> typeSuperclassNames() {
		return new CloneListIterator<String>(this.typeSuperclassNames);
	}

	protected void setTypeSuperclassNames(List<String> typeSuperclassNames) {
		synchronized (this.typeSuperclassNames) {
			this.synchronizeList(typeSuperclassNames, this.typeSuperclassNames, TYPE_SUPERCLASS_NAMES_COLLECTION);
		}
	}

	protected List<String> buildTypeSuperclassNames(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		if (typeBinding == null) {
			return Collections.emptyList();
		}
		ArrayList<String> names = new ArrayList<String>();
		typeBinding = typeBinding.getSuperclass();
		while (typeBinding != null) {
			names.add(typeBinding.getQualifiedName());
			typeBinding = typeBinding.getSuperclass();
		}
		return names;
	}

	// ***** type interface hierarchy
	public Iterator<String> typeInterfaceNames() {
		return new CloneIterator<String>(this.typeInterfaceNames);
	}

	protected boolean typeInterfaceNamesContains(String interfaceName) {
		return this.typeInterfaceNames.contains(interfaceName);
	}

	protected void setTypeInterfaceNames(Collection<String> typeInterfaceNames) {
		synchronized (this.typeInterfaceNames) {
			this.synchronizeCollection(typeInterfaceNames, this.typeInterfaceNames, TYPE_INTERFACE_NAMES_COLLECTION);
		}
	}

	protected Collection<String> buildTypeInterfaceNames(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		if (typeBinding == null) {
			return Collections.emptySet();
		}
		HashSet<String> names = new HashSet<String>();
		while (typeBinding != null) {
			this.addInterfaceNamesTo(typeBinding, names);
			typeBinding = typeBinding.getSuperclass();
		}
		return names;
	}

	protected void addInterfaceNamesTo(ITypeBinding typeBinding, HashSet<String> names) {
		for (ITypeBinding interfaceBinding : typeBinding.getInterfaces()) {
			names.add(interfaceBinding.getQualifiedName());
			this.addInterfaceNamesTo(interfaceBinding, names);  // recurse
		}
	}

	// ***** type type argument names
	public ListIterator<String> typeTypeArgumentNames() {
		return new CloneListIterator<String>(this.typeTypeArgumentNames);
	}

	public int typeTypeArgumentNamesSize() {
		return this.typeTypeArgumentNames.size();
	}

	public String getTypeTypeArgumentName(int index) {
		return this.typeTypeArgumentNames.get(index);
	}

	protected void setTypeTypeArgumentNames(List<String> typeTypeArgumentNames) {
		synchronized (this.typeTypeArgumentNames) {
			this.synchronizeList(typeTypeArgumentNames, this.typeTypeArgumentNames, TYPE_TYPE_ARGUMENT_NAMES_COLLECTION);
		}
	}

	/**
	 * these types can be arrays (e.g. "java.lang.String[]");
	 * but they won't have any further nested generic type arguments
	 * (e.g. "java.util.Collection<java.lang.String>")
	 */
	protected List<String> buildTypeTypeArgumentNames(CompilationUnit astRoot) {
		ITypeBinding typeBinding = this.getTypeBinding(astRoot);
		if (typeBinding == null) {
			return Collections.emptyList();
		}

		ITypeBinding[] typeArguments = typeBinding.getTypeArguments();
		if (typeArguments.length == 0) {
			return Collections.emptyList();
		}

		ArrayList<String> names = new ArrayList<String>(typeArguments.length);
		for (ITypeBinding typeArgument : typeArguments) {
			if (typeArgument == null) {
				names.add(null);
			} else {
				names.add(typeArgument.getTypeDeclaration().getQualifiedName());
			}
		}
		return names;
	}

}
