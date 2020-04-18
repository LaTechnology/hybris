/**
 * 
 */
package com.hybris.productcomparison.data;

import java.util.LinkedList;
import java.util.Queue;


/** Wrapper class for Product comparison queue */
public class WrapperQueueProductComparison
{

	private Queue queue = new LinkedList<String>();

	/** @return the queue */
	public Queue getQueue()
	{
		return queue;
	}

	/** @param queue
	 *           the queue to set */
	public void setQueue(final Queue queue)
	{
		this.queue = queue;
	}


}
