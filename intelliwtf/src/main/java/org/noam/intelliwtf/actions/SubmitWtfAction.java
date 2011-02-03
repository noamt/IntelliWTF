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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

import java.awt.Dimension;

/**
 * @author Noam Y. Tenne
 */
public class SubmitWtfAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        SelectionModel editorSelectionModel = editor.getSelectionModel();
        String selectedTextFromEditor = editorSelectionModel.getSelectedText();

        SubmitWtfDialog dialog = new SubmitWtfDialog();
        dialog.setSelectedText(selectedTextFromEditor);
        dialog.pack();
        dialog.setSize(700, 500);
        dialog.setMinimumSize(new Dimension(700, 500));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
