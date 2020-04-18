package com.hybris.platform.mediaperspective.session.impl;


import de.hybris.platform.cockpit.model.search.Query;

class EmptyBrowserFilterImpl extends MimeTypeBrowserFilterImpl
{
	EmptyBrowserFilterImpl(final String label)
	{
		super(label, null);
	}

	@Override
	public void filterQuery(final Query query)
	{
		//intentionally empty
	}
}