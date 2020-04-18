/**
 *
 */
package com.greenlee.storefront.forms;

import de.hybris.platform.acceleratorstorefrontcommons.forms.ReviewForm;


/**
 * @author tarun.ranka
 *
 */
public class GreenleeReviewForm extends ReviewForm
{
	private boolean agree;

	/**
	 * @return the agree
	 */
	public boolean isAgree()
	{
		return agree;
	}

	/**
	 * @param agree
	 *           the agree to set
	 */
	public void setAgree(final boolean agree)
	{
		this.agree = agree;
	}
}
