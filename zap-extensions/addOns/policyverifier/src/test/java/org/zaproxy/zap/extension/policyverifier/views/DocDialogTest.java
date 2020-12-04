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
package org.zaproxy.zap.extension.policyverifier.views;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DocDialogTest {

    @Test
    void generateLayoutForOkButton_ValuesAreFixed() {
        DocDialog dialog = Mockito.mock(DocDialog.class);
        when(dialog.generateLayoutForOkButton()).thenCallRealMethod();
        GridBagConstraints gd = dialog.generateLayoutForOkButton();
        assertEquals(1, gd.gridx);
        assertEquals(1, gd.gridy);
        assertEquals(GridBagConstraints.SOUTHEAST, gd.anchor);
    }

    @Test
    void getjButton_ValuesAreFixed() {
        DocDialog dialog = Mockito.mock(DocDialog.class);
        when(dialog.getjButton()).thenCallRealMethod();
        JButton button = dialog.getjButton();
        assertEquals("Ok", button.getText());
    }

    @Test
    void generateHelpPanel_ValuesAreFixed() {
        DocDialog dialog = Mockito.mock(DocDialog.class);
        when(dialog.generateHelpPanel()).thenCallRealMethod();
        JPanel panel = dialog.generateHelpPanel();
        assertTrue(
                Arrays.toString(Arrays.stream(panel.getComponents()).toArray()).contains("<html>"));
    }

    @Test
    void generatePanelForHelpTextBox_ValuesAreFixed() {
        DocDialog dialog = Mockito.mock(DocDialog.class);
        when(dialog.generatePanelForHelpTextBox()).thenCallRealMethod();
        GridBagConstraints gd = dialog.generatePanelForHelpTextBox();
        assertEquals(GridBagConstraints.NORTHWEST, gd.anchor);
        assertEquals(GridBagConstraints.BOTH, gd.fill);
    }

    @Test
    void getMainPanel_CompleteDialog() {
        DocDialog dialog = Mockito.mock(DocDialog.class);
        when(dialog.getMainPanel()).thenCallRealMethod();
        doCallRealMethod().when(dialog).setUpHelpTextBox(any());
        doCallRealMethod().when(dialog).setUpOkButton(any());
        when(dialog.getjButton()).thenCallRealMethod();
        when(dialog.generatePanelForHelpTextBox()).thenCallRealMethod();
        when(dialog.generateHelpPanel()).thenCallRealMethod();
        JPanel panel = dialog.getMainPanel();
        String fullPanelContent = Arrays.toString(Arrays.stream(panel.getComponents()).toArray());
        assertTrue(fullPanelContent.contains("JPanel"));
        assertTrue(fullPanelContent.contains("Ok"));
    }
}
