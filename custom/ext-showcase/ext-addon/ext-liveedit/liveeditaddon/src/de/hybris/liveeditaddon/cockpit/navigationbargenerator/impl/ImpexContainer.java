/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.liveeditaddon.cockpit.navigationbargenerator.impl;


import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class ImpexContainer {

    private String lang = null;
    private String impex = null;

    public ImpexContainer(String lang, String impex) {
        Preconditions.checkNotNull(lang);
        Preconditions.checkNotNull(impex);

        this.lang = lang;
        this.impex = impex;
    }

    public String getLang() {
        return lang;
    }

    public String getImpex() {
        return impex;
    }

    public static List<ImpexContainer> from(OutputResult outputResult) {
        Preconditions.checkNotNull(outputResult);
        List<ImpexContainer> result = new ArrayList<ImpexContainer>(outputResult.languageKeys().size());

        result.add(new ImpexContainer(OutputResult.BASIC_OUTPUT, outputResult.getImpexScript(OutputResult.BASIC_OUTPUT)));
        result.add(new ImpexContainer(OutputResult.FULL_OUTPUT, outputResult.getImpexScript(OutputResult.FULL_OUTPUT)));

        for (String key : outputResult.languageKeys()) {
            if ((OutputResult.BASIC_OUTPUT != key) && (OutputResult.FULL_OUTPUT != key))
            result.add(new ImpexContainer(key, outputResult.getImpexScript(key)));
        }
        return result;
    }

    public String getLabel() {
        if (OutputResult.BASIC_OUTPUT.equals(lang)) {
            return "basic";
        }

        if (OutputResult.FULL_OUTPUT.equals(lang)) {
            return "full";
        }
        return lang;
    }
}
