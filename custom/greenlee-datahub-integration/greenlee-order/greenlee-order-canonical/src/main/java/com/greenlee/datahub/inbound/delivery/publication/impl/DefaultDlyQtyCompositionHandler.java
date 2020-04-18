package com.greenlee.datahub.inbound.delivery.publication.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.greenlee.sapintegration.orderexchange.outbound.impl.DefaultGreenleeB2BPartnerContributor;
import com.hybris.datahub.domain.CanonicalAttributeDefinition;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.CompositionGroup;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.saporder.composition.impl.AbstractCompositionHandler;

public class DefaultDlyQtyCompositionHandler extends
		AbstractCompositionHandler {
	private Logger logger = LoggerFactory.getLogger(DefaultDlyQtyCompositionHandler.class);

	public DefaultDlyQtyCompositionHandler() {
		super("RawDELVRY", "delQuantity",
				"CanonicalDeliveryCreationNotification");
	}

	@Override
	public <T extends CanonicalItem> T compose(
			CanonicalAttributeDefinition arg0,
			CompositionGroup<? extends RawItem> arg1, T canonicalItem) {
		logger.info("RawItem, Number of RawItem: {}"
				+ arg1.getItems().size());
		ImmutableList items = arg1.getItems();
		determineAndSetDeliveryQty(items, canonicalItem);
		return canonicalItem;
	}

	protected <T extends CanonicalItem> void determineAndSetDeliveryQty(
			List<? extends RawItem> items, T canonicalItem) {
		StringBuffer buffer = null;
		try {
			buffer = new StringBuffer();
			int index = 0;
			for (RawItem rawItem : items) {
				String entryNumber = (String) rawItem.getField("E1EDL20-E1EDL24-VGPOS");
				String qtyInString = (String) rawItem.getField("E1EDL20-E1EDL24-LFIMG");
				if (entryNumber != null && qtyInString != null & index >= 1) {
					buffer.append("-");
				}
				buffer.append(entryNumber).append("_").append(qtyInString);
				index++; 
			}
			canonicalItem.setField("delQuantity", buffer.toString()); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			buffer = null;
			items.clear();
		}
	}
}
