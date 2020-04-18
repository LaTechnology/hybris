/**
 *
 */
package com.greenlee.storefront.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.greenlee.core.util.GreenleeUtils;

import reactor.util.StringUtils;


/**
 * @author raja.santhanam
 *
 */
public class GreenleeB2BUnitFormatterTag extends SimpleTagSupport
{

	private String id;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.jsp.tagext.SimpleTagSupport#doTag()
	 */
	@Override
	public void doTag() throws JspException, IOException
	{
		String b2bUnit = GreenleeUtils.getAccNoFromB2BUnitId(id);
		b2bUnit = StringUtils.isEmpty(b2bUnit) ? "-" : b2bUnit;
		getJspContext().getOut().println(b2bUnit);
	}

	/**
	 * @return the id
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id
	 *           the id to set
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

}
