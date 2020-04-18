package com.hybris.platform.mediaperspective.systemsetup;

/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;

@SystemSetup(extension="mediaperspective")
public class MediaPerspectiveSystemSetup
{
  private static final Logger LOG = Logger.getLogger(MediaPerspectiveSystemSetup.class);

  private PersistentKeyGenerator processCodeGenerator;
  private FlexibleSearchService flexibleSearchService;
  private ModelService modelService;

  @SystemSetup(type=SystemSetup.Type.ESSENTIAL, process=SystemSetup.Process.ALL)
  public void createNumberSeriesForTypes()
  {
    createNumberSeries("Media");
   
  }


  private void createNumberSeries(String key)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("Creating NumberSeries for " + key);
    }
    this.processCodeGenerator.setKey(key);
    this.processCodeGenerator.generate();
  }

  @Required
  public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
  {
    this.flexibleSearchService = flexibleSearchService;
  }

  protected FlexibleSearchService getFlexibleSearchService()
  {
    return this.flexibleSearchService;
  }
  
  @Required
  public void setModelService(ModelService modelService)
  {
    this.modelService = modelService;
  }

  public ModelService getModelService()
  {
    return this.modelService;
  }
  
  @Required
  public void setProcessCodeGenerator(PersistentKeyGenerator processCodeGenerator)
  {
	  this.processCodeGenerator = processCodeGenerator;
  }
}