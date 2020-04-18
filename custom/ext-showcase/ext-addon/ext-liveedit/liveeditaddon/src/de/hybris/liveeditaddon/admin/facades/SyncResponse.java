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
package de.hybris.liveeditaddon.admin.facades;

public class SyncResponse {

    private SynchronisationStatus synchronisationStatus;
    private MessageType messageType;
    private String message1;
    private String message2;

    public SyncResponse(MessageType messageType, SynchronisationStatus synchronisationStatus, String message1, String message2) {
        this.messageType = messageType;
        this.synchronisationStatus = synchronisationStatus;
        this.message1 = message1;
        this.message2 = message2;
    }

    public static SyncResponse buildInfo(SynchronisationStatus synchronisationStatus, String message1, String message2) {
        return new SyncResponse(MessageType.INFO, synchronisationStatus, message1, message2);
    }

    public static SyncResponse buildWarning(SynchronisationStatus synchronisationStatus, String message1, String message2) {
        return new SyncResponse(MessageType.WARNING, synchronisationStatus, message1, message2);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage1() {
        return message1;
    }

    public String getMessage2() {
        return message2;
    }

    public SynchronisationStatus getSynchronisationStatus() {
        return synchronisationStatus;
    }
}
