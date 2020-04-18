/**
 *
 */
package com.greenlee.facades.populators;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.features.Feature;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.commercefacades.product.converters.populator.ProductFeatureListPopulator;
import de.hybris.platform.commercefacades.product.data.ClassificationData;
import de.hybris.platform.commercefacades.product.data.FeatureData;
import de.hybris.platform.commercefacades.product.data.FeatureValueData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Viji Shetty
 *
 */
public class GreenleeProductFeatureListPopulator extends ProductFeatureListPopulator
{

    public static final String DEFAULT_NAME = "-";

    @Override
    protected List<ClassificationData> buildClassificationDataList(final FeatureList source)
    {

        final List<ClassificationData> result = new ArrayList<ClassificationData>();
        final Map<String, ClassificationData> map = new HashMap<String, ClassificationData>();

        for (final Feature feature : source.getFeatures())
        {

            ClassificationData classificationData = null;
            if (feature.getClassAttributeAssignment() != null)
            {
                final ClassificationClassModel classificationClass = feature.getClassAttributeAssignment()
                        .getClassificationClass();
                final String classificationClassCode = classificationClass.getCode();
                if (map.containsKey(classificationClassCode))
                {
                    classificationData = map.get(classificationClassCode);
                }
                else
                {
                    classificationData = (ClassificationData) getClassificationConverter().convert(classificationClass);

                    map.put(classificationClassCode, classificationData);
                    result.add(classificationData);
                }
            }
            if (feature.getValues() != null && !feature.getValues().isEmpty())
            {
                // Create the feature
                final FeatureData newFeature = (FeatureData) getFeatureConverter().convert(feature);

                // Add the feature to the classification
                if (classificationData.getFeatures() == null)
                {
                    classificationData.setFeatures(new ArrayList<FeatureData>(1));
                }
                classificationData.getFeatures().add(newFeature);
            }
            else
            {

                // Create the feature
                final FeatureData newFeature = new FeatureData();
                newFeature.setCode(feature.getCode());
                newFeature.setName(feature.getName());
                newFeature.setComparable(true);

                final List<FeatureValueData> featureValueDataList = new ArrayList<FeatureValueData>();
                final FeatureValueData featureValueData = new FeatureValueData();
                featureValueData.setValue(String.valueOf(DEFAULT_NAME));
                featureValueDataList.add(featureValueData);
                newFeature.setFeatureValues(featureValueDataList);

                // Add the feature to the classification
                if (classificationData.getFeatures() == null)
                {
                    classificationData.setFeatures(new ArrayList<FeatureData>(1));
                }
                classificationData.getFeatures().add(newFeature);
            }
        }

        return result.isEmpty() ? null : result;
    }

}
