/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Oracle. - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.mappings.details;

import java.util.Collection;
import java.util.Iterator;
import org.eclipse.jpt.core.internal.context.base.ITable;
import org.eclipse.jpt.db.internal.Schema;
import org.eclipse.jpt.db.internal.Table;
import org.eclipse.jpt.ui.internal.IJpaHelpContextIds;
import org.eclipse.jpt.ui.internal.mappings.JptUiMappingsMessages;
import org.eclipse.jpt.ui.internal.mappings.db.CatalogCombo;
import org.eclipse.jpt.ui.internal.mappings.db.SchemaCombo;
import org.eclipse.jpt.ui.internal.mappings.db.TableCombo;
import org.eclipse.jpt.ui.internal.widgets.AbstractFormPane;
import org.eclipse.jpt.ui.internal.widgets.IWidgetFactory;
import org.eclipse.jpt.utility.internal.iterators.EmptyIterator;
import org.eclipse.jpt.utility.internal.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |                                                                           |
 * | - Table ----------------------------------------------------------------- |
 * | |          ------------------------------------------------------------ | |
 * | | Table:   | TableCombo                                               | | |
 * | |          ------------------------------------------------------------ | |
 * | |          ------------------------------------------------------------ | |
 * | | Catalog: | CatalogCombo                                             | | |
 * | |          ------------------------------------------------------------ | |
 * | |          ------------------------------------------------------------ | |
 * | | Schema:  | SchemaCombo                                              | | |
 * | |          ------------------------------------------------------------ | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see ITable
 * @see EntityComposite - The parent container
 * @see TableCombo
 * @see CatalogCombo
 * @see SchemaCombo
 *
 * @TODO repopulate this panel based on the Entity table changing
 *
 * @version 2.0
 * @since 1.0
 */
public class TableComposite extends AbstractFormPane<ITable>
{
	/**
	 * Creates a new <code>TableComposite</code>.
	 *
	 * @param parentPane The parent container of this one
	 * @param subjectHolder The holder of the subject
	 * @param parent The parent container
	 */
	public TableComposite(AbstractFormPane<?> parentPane,
	                      PropertyValueModel<? extends ITable> subjectHolder,
	                      Composite parent) {

		super(parentPane, subjectHolder, parent);
	}

	/**
	 * Creates a new <code>TableComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>ITable</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public TableComposite(PropertyValueModel<? extends ITable> subjectHolder,
	                      Composite parent,
	                      IWidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	private CatalogCombo<ITable> buildCatalogCombo(Composite container) {

		return new CatalogCombo<ITable>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ITable.DEFAULT_CATALOG_PROPERTY);
				propertyNames.add(ITable.SPECIFIED_CATALOG_PROPERTY);
			}

			@Override
			protected String defaultValue() {
				return subject().getDefaultCatalog();
			}

			@Override
			protected void setValue(String value) {
				subject().setSpecifiedCatalog(value);
			}

			@Override
			protected String value() {
				return subject().getSpecifiedCatalog();
			}
		};
	}

	private SchemaCombo<ITable> buildSchemaCombo(Composite container) {

		return new SchemaCombo<ITable>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ITable.DEFAULT_SCHEMA_PROPERTY);
				propertyNames.add(ITable.SPECIFIED_SCHEMA_PROPERTY);
			}

			@Override
			protected String defaultValue() {
				return subject().getDefaultSchema();
			}

			@Override
			protected void setValue(String value) {
				subject().setSpecifiedSchema(value);
			}

			@Override
			protected String value() {
				return subject().getSpecifiedSchema();
			}
		};
	}

	private TableCombo<ITable> buildTableCombo(Composite container) {

		return new TableCombo<ITable>(this, container) {

			@Override
			protected void addPropertyNames(Collection<String> propertyNames) {
				super.addPropertyNames(propertyNames);
				propertyNames.add(ITable.DEFAULT_NAME_PROPERTY);
				propertyNames.add(ITable.SPECIFIED_NAME_PROPERTY);
			}

			@Override
			protected String defaultValue() {
				return subject().getDefaultName();
			}

			@Override
			protected void setValue(String value) {
				subject().setSpecifiedName(value);
			}

			@Override
			protected Table table() {
				return subject().dbTable();
			}

			private Schema tableSchema() {
				return database().schemaNamed(subject().getSchema());
			}

			@Override
			protected String value() {
				return subject().getSpecifiedName();
			}

			@Override
			protected Iterator<String> values() {
				Schema schema = tableSchema();

				if (schema != null) {
					return schema.tableNames();
				}

				return EmptyIterator.instance();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected void initializeLayout(Composite container) {

		// Table group pane
		Group tableGroupPane = buildTitledPane(
			container,
			JptUiMappingsMessages.TableComposite_tableSection
		);

		// Table widgets
		buildLabeledComposite(
			tableGroupPane,
			JptUiMappingsMessages.TableChooser_label,
			buildTableCombo(tableGroupPane),
			IJpaHelpContextIds.ENTITY_TABLE
		);

		// Catalog widgets
		buildLabeledComposite(
			tableGroupPane,
			JptUiMappingsMessages.CatalogChooser_label,
			buildCatalogCombo(tableGroupPane),
			IJpaHelpContextIds.ENTITY_CATALOG
		);

		// Schema widgets
		buildLabeledComposite(
			tableGroupPane,
			JptUiMappingsMessages.SchemaChooser_label,
			buildSchemaCombo(tableGroupPane),
			IJpaHelpContextIds.ENTITY_SCHEMA
		);
	}
}