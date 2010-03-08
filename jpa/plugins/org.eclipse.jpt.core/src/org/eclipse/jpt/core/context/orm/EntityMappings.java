/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.MappingFileRoot;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.persistence.PersistentTypeContainer;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.db.Catalog;
import org.eclipse.jpt.db.Schema;
import org.eclipse.jpt.db.SchemaContainer;
import org.eclipse.jpt.utility.internal.iterables.ListIterable;

/**
 * Context <code>orm.xml</code> entity mappings.
 * Context model corresponding to the
 * XML resource model {@link XmlEntityMappings},
 * which corresponds to the <code>entity-mappings</code> element
 * in the <code>orm.xml</code> file.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 2.3
 * @since 2.0
 */
public interface EntityMappings 
	extends MappingFileRoot, JpaStructureNode, PersistentType.Owner, PersistentTypeContainer
{
	XmlEntityMappings getXmlEntityMappings();
	
	String getVersion();
		
	String getDescription();
	void setDescription(String newDescription);
		String DESCRIPTION_PROPERTY = "description"; //$NON-NLS-1$

	String getPackage();
	void setPackage(String newPackage);
		String PACKAGE_PROPERTY = "package"; //$NON-NLS-1$

	/**
	 * Return the specified access if present, otherwise return the default
	 * access.
	 */
	AccessType getAccess();
	AccessType getSpecifiedAccess();
	void setSpecifiedAccess(AccessType access);
		String SPECIFIED_ACCESS_PROPERTY = "specifiedAccess"; //$NON-NLS-1$
	AccessType getDefaultAccess();
		String DEFAULT_ACCESS_PROPERTY = "defaultAccess"; //$NON-NLS-1$

	/**
	 * Return the database schema container, which can be either a catalog or,
	 * if the database does not support catalogs, the database itself.
	 */
	SchemaContainer getDbSchemaContainer();

	/**
	 * Return the specified catalog if present, otherwise return the default
	 * catalog.
	 */
	String getCatalog();
	String getSpecifiedCatalog();
	void setSpecifiedCatalog(String catalog);
		String SPECIFIED_CATALOG_PROPERTY = "specifiedCatalog"; //$NON-NLS-1$
	String getDefaultCatalog();
		String DEFAULT_CATALOG_PROPERTY = "defaultCatalog"; //$NON-NLS-1$
	Catalog getDbCatalog();

	/**
	 * Return the specified schema if present, otherwise return the default
	 * schema.
	 */
	String getSchema();
	String getSpecifiedSchema();
	void setSpecifiedSchema(String schema);
		String SPECIFIED_SCHEMA_PROPERTY = "specifiedSchema"; //$NON-NLS-1$
	String getDefaultSchema();
		String DEFAULT_SCHEMA_PROPERTY = "defaultSchema"; //$NON-NLS-1$
	Schema getDbSchema();

	PersistenceUnitMetadata getPersistenceUnitMetadata();
	
	ListIterable<OrmPersistentType> getPersistentTypes();
	int getPersistentTypesSize();
	OrmPersistentType addPersistentType(String mappingKey, String className);
	void removePersistentType(int index);
	void removePersistentType(OrmPersistentType persistentType);
	//void movePersistentType(int targetIndex, int sourceIndex);
	boolean containsPersistentType(String className);
		String PERSISTENT_TYPES_LIST = "persistentTypes"; //$NON-NLS-1$
	
	ListIterable<OrmSequenceGenerator> getSequenceGenerators();
	int getSequenceGeneratorsSize();
	OrmSequenceGenerator addSequenceGenerator(int index);
	void removeSequenceGenerator(int index);
	void removeSequenceGenerator(OrmSequenceGenerator sequenceGenerator);
	void moveSequenceGenerator(int targetIndex, int sourceIndex);
		String SEQUENCE_GENERATORS_LIST = "sequenceGenerators"; //$NON-NLS-1$

	ListIterable<OrmTableGenerator> getTableGenerators();
	int getTableGeneratorsSize();
	OrmTableGenerator addTableGenerator(int index);
	void removeTableGenerator(int index);
	void removeTableGenerator(OrmTableGenerator tableGenerator);
	void moveTableGenerator(int targetIndex, int sourceIndex);
		String TABLE_GENERATORS_LIST = "tableGenerators"; //$NON-NLS-1$

	OrmQueryContainer getQueryContainer();
	
	OrmPersistenceUnitDefaults getPersistenceUnitDefaults();
	
	/**
	 * Return the {@link OrmPersistentType) listed in this mapping file
	 * with the given type name. Return null if none exists.
	 */
	OrmPersistentType getPersistentType(String typeName);
	
	/**
	 * Return the default package to be used for persistent types in this context
	 */
	String getDefaultPersistentTypePackage();
	
	/**
	 * Return the default metadata complete value for persistent types in this context
	 */
	boolean isDefaultPersistentTypeMetadataComplete();
	
	void changeMapping(OrmPersistentType ormPersistentType, OrmTypeMapping oldMapping, OrmTypeMapping newMapping);
	
	boolean containsOffset(int textOffset);
	
	/**
	 * Update the EntityMappings context model object to match the XmlEntityMappings 
	 * resource model object. see {@link org.eclipse.jpt.core.JpaProject#update()}
	 */
	void update();
}
