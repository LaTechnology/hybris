package com.greenlee.sapintegration.orderexchange.inbound.events;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greenlee.sapintegration.orderexchange.datahub.inbound.GreenleeDataHubInboundDeliveryHelper;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.sap.orderexchange.constants.DataHubInboundConstants;
import de.hybris.platform.sap.orderexchange.inbound.events.DataHubTranslator;


public class GreenleeDataHubDeliveryTranslator extends DataHubTranslator<GreenleeDataHubInboundDeliveryHelper> 
{
	public static final String HELPER_BEAN = "sapGreenleeDataHubInboundDeliveryHelper";
	private Logger logger =LoggerFactory.getLogger(GreenleeDataHubDeliveryTranslator.class);
	
	@SuppressWarnings("javadoc")
	public GreenleeDataHubDeliveryTranslator()
	{
		super(HELPER_BEAN);
	}

	@Override
	public void performImport(final String delivInfo, final Item processedItem) throws ImpExException
	{
		Map<String, String> map =null;
		try
		{
			map=new LinkedHashMap<String, String>();
			final String orderCode  = processedItem.getAttribute(DataHubInboundConstants.CODE).toString();
			final String[] values=delivInfo.split("#");
			final String[] fields = new String[]{"plant", "deliveryNumber", "delvryEntryNumber", "delQuantity", "consignmentValue", "taxBreakUp",
				"dutyBreakUp", "promotionBreakUp", "freightBreakUp","goodsIssueDate", "trackingNumber", "trackingInfos"};
			for( int i = 0; i <= values.length - 1; i++)
			{
				logger.debug("Non PGI >> GreenleeDataHubDeliveryTranslator >> deliveryData [" +fields[i] +"] Value ["+values[i]+"] Order Code ["+orderCode+"]");
				map.put(fields[i],values[i]);
			}
			logger.error("Non PGI completed." +map);
			if (delivInfo != null && !delivInfo.equals(DataHubInboundConstants.IGNORE))
			{
				final String plant = map.get("plant"); //warehouseId
				final String goodsIssueDate = map.get("goodsIssueDate");
				final String deliveryNumber = map.get("deliveryNumber");
				final String[] delvryEntryNumber = convertStringToArray(map.get("delvryEntryNumber"));
				final Map<Integer, Double> delQuantities = mapItemWithQtyForItemModel(map.get("delQuantity"));
				final Double consignmentValue = Double.valueOf(map.get("consignmentValue"));
				final Double taxBreakUp = Double.valueOf(map.get("taxBreakUp"));
				final Double dutyBreakUp = Double.valueOf(map.get("dutyBreakUp"));
				final Double promotionBreakUp = Double.valueOf(map.get("promotionBreakUp"));
				final Double freightBreakUp = Double.valueOf(map.get("freightBreakUp"));
				final String trackingNumber = map.get("trackingNumber");
				final String trackingInfos = map.get("trackingInfos");
				getInboundHelper().processDeliveryAndGoodsIssue(orderCode,plant,goodsIssueDate,deliveryNumber,delvryEntryNumber,delQuantities,
						consignmentValue,taxBreakUp,dutyBreakUp,promotionBreakUp,freightBreakUp,trackingNumber,trackingInfos);
			}
		}
		catch (final JaloSecurityException exception)
		{
			logger.error("Exception: Non PGI >> GreenleeDataHubDeliveryTranslator >> delivInfo "+ exception.getMessage());
			throw new ImpExException(exception);
		}

	}
	private Double[] convertStringArrayToDouble(String args) {
		String[] argss = args.split("-");
		Double[] arrDouble = new Double[argss.length];
		for (int i = 0; i < argss.length; i++) {
			arrDouble[i] = (Double) ConvertUtils.convert(argss[i], Double.TYPE);
		}
		return arrDouble;
	}
	private Map<Integer, Double> mapItemWithQtyForItemModel(String args) {
		String[] argss = args.split("-");
		Map<Integer, Double> map=new HashMap<Integer, Double>();
		for (int i = 0; i < argss.length; i++) {
			//delQuantities are prefixed with entry number of the items:000003_5.000-000002_5.000-000001_1.000
			if(!argss[i].isEmpty() && argss!=null)
			{
				String[] argv=argss[i].split("_");
				Integer entryNumber=(Integer) ConvertUtils.convert(argv[0], Integer.TYPE);
				Double qty= (Double) ConvertUtils.convert(argv[1], Double.TYPE);
				map.put(entryNumber,qty);
			}
		}
		return map;
	}
	private String[] convertStringToArray(String args) {
		String[] argss = args.split(",");
		String[] strings = new String[argss.length];
		for (int i = 0; i < argss.length; i++) {
			strings[i] = String.valueOf(Integer.valueOf(argss[i]));
		}
		return strings;
	}
}
