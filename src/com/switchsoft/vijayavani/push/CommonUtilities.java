/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.switchsoft.vijayavani.push;


/**
 * Helper class providing methods and constants common to other classes in the
 * app.
 */
public final class CommonUtilities {

    /**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
	public static final String SERVER_URL = "http://www.thisisswitch.com:8084/ApplyticsService/Service.svc";

    /**
     * Google API project id registered to use GCM.
     */
    public static final String SENDER_ID = "316862302190";

    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GCMDemo";

    /**
     * Intent used to display a message in the screen.
     */
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.switchsoft.vijayavani.DISPLAY_MESSAGE";

    /**
     * Intent's extra that contains the message to be displayed.
     */
    public  static final String EXTRA_MESSAGE = "message";

}
