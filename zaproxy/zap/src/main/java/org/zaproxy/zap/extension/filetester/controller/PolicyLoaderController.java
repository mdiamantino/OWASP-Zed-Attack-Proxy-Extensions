/*
 * Zed Attack Proxy (ZAP) and its related class files.
 *
 * ZAP is an HTTP/HTTPS proxy for assessing web application security.
 *
 * Copyright 2020 The ZAP Development Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zaproxy.zap.extension.filetester.controller;

/**
 * This class manages manages communication between the view and the model It is a singleton,
 * because only one instance is used, by one entity only.
 */
public class PolicyLoaderController {
    private static PolicyLoaderController soleController;
    private String PREFIX = "filetester";

    public PolicyLoaderController() {
        if (soleController != null) {
            throw new RuntimeException(
                    "Use getInstance() method to get the single instance of this class.");
        }
    }

    public static PolicyLoaderController getSingleton() {
        if (soleController == null) {
            soleController = new PolicyLoaderController();
        }
        return soleController;
    }
}
