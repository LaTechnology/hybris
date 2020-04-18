package com.hybris.addon.common.config;


import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.hybris.platform.converters.impl.AbstractConverter;


public class X implements ApplicationListener<ContextRefreshedEvent> , ApplicationContextAware, BeanFactoryAware {
   
	private static final Logger LOG = Logger.getLogger(X.class);
	
	private ConfigurableListableBeanFactory beanFactory;
	private ApplicationContext applicationContext;

	
	@Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(applicationContext));
		
		String[] beanNames = applicationContext.getBeanNamesForType(AbstractConverter.class);
    	for (String beanName : beanNames)
    	{
    		final BeanDefinition def = beanFactory.getBeanDefinition(beanName);
    		PropertyValue pv = def.getPropertyValues().getPropertyValue("targetClass");
    		if (pv != null)
    		{    			
    			
    			if (pv.getValue() instanceof TypedStringValue)
    			{
    				
    		
    				String expr= StringUtils.strip(((TypedStringValue) pv.getValue()).getValue());
    				if (expr.startsWith("#{"))
    				{
    					expr = StringUtils.replaceOnce(expr, "#{", "@");
    					expr = StringUtils.stripEnd(expr, "}");
    					final Object result = parser.parseExpression(expr).getValue(context);
    					if (result != null)
            			{
            				
            				try
            				{
            					((AbstractConverter)applicationContext.getBean(beanName, AbstractConverter.class)).setTargetClass(ClassUtils.getClass(result.toString()));
            				}
            				catch (ClassNotFoundException e)
            				{
            					LOG.error(beanName + " target class instantiation failed", e);
            				}
            			}
    				}
    				
    				
    				
        			
        			

        			
    			}
    			
    		}
    	}    		
    }

    protected ConfigurableListableBeanFactory getBeanFactory()
	{
		return beanFactory;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		if (!(beanFactory instanceof ConfigurableListableBeanFactory))
		{
			throw new IllegalStateException("X doesn't work with a BeanFactory which does not implement ConfigurableListableBeanFactory: " + beanFactory.getClass());
		}
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	protected ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
