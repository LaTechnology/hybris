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

import java.util.*;

/**
 * Aggregator for navigation generator output. Don't use in multithread env.
 * This class provide possibility to get different version of generated impex (full version, basic version, for specific lang version)
 */
public class OutputResult {

    /**
     * this output contains all impex lines (with localizations)
     */
    public static final String FULL_OUTPUT = "FULL_OUTPUT";

    /**
     * this output contains only basic version of impex(without localizations)
     */
    public static final String BASIC_OUTPUT = "BASIC_OUTPUT";

    private Map<String, StringBuilder> buildersPerLang = new HashMap<String, StringBuilder>();


    public OutputResult(Collection<String> langs) {
        Preconditions.checkNotNull(langs);
        Preconditions.checkArgument(!langs.isEmpty(), "Langs collection cannot be empty.");

        buildersPerLang.put(FULL_OUTPUT, new StringBuilder());
        buildersPerLang.put(BASIC_OUTPUT, new StringBuilder());
        for (String lang : langs) {
            buildersPerLang.put(lang, new StringBuilder());
        }
    }

    public OutputResult append(Object object, String lang) {
        Preconditions.checkNotNull(lang);
        if (!lang.equals(BASIC_OUTPUT)) {
            getStringBuilder(lang).append(object);
        }else {
            getStringBuilder(BASIC_OUTPUT).append(object);
        }
        getStringBuilder(FULL_OUTPUT).append(object);
        return this;
    }

    /**
     * appends given object to all {@link StringBuilder}
     * @param object
     * @return this for api chaining
     */
    public OutputResult appendToAll(Object object) {
        Preconditions.checkNotNull(object);
        for (String keyLang : buildersPerLang.keySet()) {
            getStringBuilder(keyLang).append(object);
        }
        return this;
    }

    /**
     * return {@link StringBuilder} for keyLang, if doesn't exists in buildersPerLang map then create new and put it to this map.
     * @param keyLang - string representation of lang
     * @return this for api chaining
     */
    protected StringBuilder getStringBuilder(String keyLang) {
        StringBuilder result = buildersPerLang.get(keyLang);
        if (null == result) {
            throw new IllegalStateException(String.format("Cannot find StringBuilder for given key. {%s}", keyLang));
        }
        return result;
    }

    public String getImpexScript(String lang) {
        Preconditions.checkNotNull(lang);
        return getStringBuilder(lang).toString();
    }

   public List<String> languageKeys() {
        return Collections.unmodifiableList(new ArrayList<String>(buildersPerLang.keySet()));
   }
}
