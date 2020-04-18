/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.restrictioneditor;

import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cms2.model.restrictions.CMSUserGroupRestrictionModel;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.collection.CollectionUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;


/**
 * 
 */
public class RestrictionEditorComposer extends GenericForwardComposer
{
	Collection<AbstractRestrictionModel> restrictions;

	@Override
	public void doAfterCompose(final Component comp) throws Exception
	{
		// YTODO Auto-generated method stub
		super.doAfterCompose(comp);

		final Map<String, Object> args = comp.getDesktop().getExecution().getArg();
		restrictions = (Collection<AbstractRestrictionModel>) args.get(RestrictionEditor.VALUE_MAP);


		final CollectionUIEditor userGroupEditor = new CollectionUIEditor(UISessionUtils.getCurrentSession().getTypeService()
				.getObjectType(CMSUserGroupRestrictionModel._TYPECODE));
		final Component redirectComponent = userGroupEditor.createViewComponent(UISessionUtils.getCurrentSession().getTypeService()
				.wrapItems(restrictions), Collections.EMPTY_MAP, new EditorListener()
		{
			@Override
			public void valueChanged(final Object value)
			{
				if (value instanceof ArrayList)
				{
					restrictions.clear();
					for (final Object restriction : (ArrayList) value)
					{
						if (restriction instanceof TypedObject)
						{
							restrictions.add((AbstractRestrictionModel) ((TypedObject) restriction).getObject());
						}
					}
				}
			}

			@Override
			public void actionPerformed(final String actionCode)
			{
				//
			}
		});
		redirectComponent.setParent(comp);
	}
}
