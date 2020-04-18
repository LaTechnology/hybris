/**
 *
 */
package com.greenlee.facades.product;

import com.greenlee.core.model.GreenleeProductModel;


/**
 * Facade for product share functionality
 */
public interface ShareProductFacade
{
    /**
     * Send email to the recipient with product details
     *
     * @param greenleeProductModel
     * @param toAddress
     * @param fromAddress
     * @param message
     */
    void shareProduct(GreenleeProductModel greenleeProductModelctModel, String toAddress, String fromAddress, String message);
}
