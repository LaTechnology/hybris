/**
 *
 */
package com.greenlee.core.comparators;

import java.util.Comparator;

import com.greenlee.greenleefacades.greenleeproduct.data.GreenleeProductMediaData;


/**
 * @author qili
 *
 *         General product documents sorting
 *
 *         Minimum number being highest priority and maximum being lowest priority
 */
public class GreenleeProductDocumentsGeneralComparator implements Comparator<GreenleeProductMediaData>
{

	/*
	 * (non-Javadoc)
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final GreenleeProductMediaData o1, final GreenleeProductMediaData o2)
	{
		final Integer o1Priority = o1.getGeneralPriority();
		final Integer o2Priority = o2.getGeneralPriority();
		if (o1Priority == null && o2Priority == null)
		{
			return 0;
		}
		//if document does not have priority value, it has lowest priority
		if (o1Priority == null)
		{
			return 1;
		}
		if (o2Priority == null)
		{
			return -1;
		}
		return o1Priority.compareTo(o2Priority);
	}

}
