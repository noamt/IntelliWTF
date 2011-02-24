/*
 * Copyright 2011 Noam Y. Tenne
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.noam.intelliwtf.actions;

import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;
import soapdust.Client;
import soapdust.ComposedValue;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SubmitWtfDialog extends JDialog {
    private JPanel contentPane;
    private JButton submitButton;
    private JButton buttonCancel;
    private JLabel submitterNameLabel;
    private JTextField submitterNameTextField;
    private JLabel submitterEmailLabel;
    private JTextField submitterEmailTextField;
    private JLabel subjectLabel;
    private JTextField subjectTextField;
    private JLabel messageLabel;
    private JTextArea messageTextArea;
    private JScrollPane messageScrollPane;
    private JLabel codeSnippetLabel;
    private JScrollPane codeSnippetScrollPane;
    private JTextArea codeSnippetTextArea;
    private JCheckBox noPublishCheckBox;

    public SubmitWtfDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submitButton);
        setTitle("Submit to TDWTF");

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setSelectedText(String selectedTextFromEditor) {
        if (StringUtils.isNotBlank(selectedTextFromEditor)) {
            codeSnippetTextArea.setText(selectedTextFromEditor);
        }
    }

    private void onOK() {
        try {
            validateFieldValues();
            Client client = new Client();
            client.setWsdlUrl("http://thedailywtf.com/SubmitWTF.asmx?wsdl");
            client.setEndPoint("http://thedailywtf.com/SubmitWTF.asmx");

            ComposedValue form = new ComposedValue();
            form.put("name", submitterNameTextField.getText());
            form.put("emailAddress", submitterEmailTextField.getText());
            form.put("subject", subjectTextField.getText());
            form.put("comments", messageTextArea.getText());
            form.put("codeSubmission", codeSnippetTextArea.getText());
            form.put("doNotPublish", Boolean.toString(noPublishCheckBox.isSelected()));

            ComposedValue response = client.call("Submit", form);
            if (response != null) {
                Messages.showInfoMessage("Your WTF was successfully submitted", "YAY!");
            }
            dispose();
        } catch (Exception iae) {
            Messages.showErrorDialog(iae.getMessage(), "Error");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void validateFieldValues() {
        if (StringUtils.isBlank(submitterNameTextField.getText())) {
            throw new IllegalArgumentException("Please enter your name.");
        }

        if (StringUtils.isBlank(submitterEmailTextField.getText())) {
            throw new IllegalArgumentException("Please enter your e-mail.");
        }

        if (StringUtils.isBlank(subjectTextField.getText())) {
            throw new IllegalArgumentException("Please enter a subject for the post.");
        }

        if (StringUtils.isBlank(messageTextArea.getText())) {
            throw new IllegalArgumentException("Please enter a message.");
        }

        if (StringUtils.isBlank(codeSnippetTextArea.getText())) {
            throw new IllegalArgumentException("Please enter a WTF-worthy code snippet.");
        }
    }
}
