package org.zaproxy.zap.extension.policyverifier.views;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

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
        assertTrue(Arrays.toString(Arrays.stream(panel.getComponents()).toArray()).contains("<html>"));
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
        System.out.println(fullPanelContent);
        assertTrue(fullPanelContent.contains("JPanel"));
        assertTrue(fullPanelContent.contains("Ok"));
    }


}