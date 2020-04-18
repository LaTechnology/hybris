/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.greenlee.datahub.order.composition.impl;

import com.hybris.datahub.domain.CanonicalAttributeDefinition;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.CompositionGroup;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.saporder.composition.impl.AbstractCompositionHandler;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Composition handler for Cancel Order Inbound (CanonicalOrderCancelNotification).
 */
@SuppressWarnings("javadoc")
public class DefaultGreenleeCompositionHandlerOrderItemCancelInbound extends AbstractCompositionHandler
{
    public DefaultGreenleeCompositionHandlerOrderItemCancelInbound() {
        super("RawORDERS","rejectionReason", "CanonicalOrderItemCancelNotification");
    }

    public <T extends CanonicalItem> T compose(CanonicalAttributeDefinition cad, CompositionGroup<? extends RawItem> cg, T canonicalItem) {
    	setRejectionReason(cg.getItems(), canonicalItem);
        return canonicalItem;
    }

    protected void setRejectionReason(List<? extends RawItem> items, CanonicalItem canonicalItem) {
        String entryNumberString = null;
        String rejectionReason;
        String qty;
        String entry = "";
        List<String> cancelledItems = (List<String>) canonicalItem.getField("cancellationDetails");
        for (RawItem rawItem : items) {
            rejectionReason =  (String)rawItem.getField("E1EDP01-ABGRU");
            entryNumberString = (String)rawItem.getField("E1EDP01-POSEX");
            qty = (String) rawItem.getField("E1EDP01-ZGHS_ORD_ITEMS-ZG_CANCEL_QTY");
            if(rejectionReason != null && !rejectionReason.isEmpty() && entryNumberString != null && !entryNumberString.isEmpty()){
	            if(cancelledItems == null) cancelledItems = new ArrayList<String>();
	            cancelledItems.add((rejectionReason + "#" + entryNumberString + "#" + (new BigDecimal(qty)).longValue()));
	            entry = entry + "#" + entryNumberString;
	            canonicalItem.setField("rejectionReason", rejectionReason + entry);
            }
        }
        canonicalItem.setField("cancellationDetails", cancelledItems); 
    }
}
