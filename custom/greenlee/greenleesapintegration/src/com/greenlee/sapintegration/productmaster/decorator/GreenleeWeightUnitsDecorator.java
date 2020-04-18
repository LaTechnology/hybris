package com.greenlee.sapintegration.productmaster.decorator;

import java.util.Map;




import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.core.Registry;
import de.hybris.platform.product.UnitService;
import de.hybris.platform.util.CSVCellDecorator;

public class GreenleeWeightUnitsDecorator implements CSVCellDecorator {

	@Override
	public String decorate(int position, Map<Integer, String> srcLine) {
		final String code = (String) srcLine.get(Integer.valueOf(position));
		if (code == null || code.length() == 0)
		{
			return code;
		}
		else
		{
			UnitService  unitService =  (UnitService) Registry.getApplicationContext().getBean("unitService");
			return unitService.getUnitForCode(code).getSapCode();
		}
	}

}
