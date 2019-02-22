package GUI;

import javax.swing.*;

public class ToolBar extends JToolBar {

    public JButton newButton, resignButton, undoButton, redoButton;

    public JButton settingsButton;

    public ToolBar(String type, GUI.ChessGUI.myButtonListener myButtonListener) {
        if(type.equals("top")) {
            newButton = new JButton("New");
            resignButton = new JButton("Resign");
            undoButton = new JButton("Undo");
            redoButton = new JButton("Redo");
            newButton.addActionListener(myButtonListener);
            resignButton.addActionListener(myButtonListener);
            undoButton.addActionListener(myButtonListener);
            redoButton.addActionListener(myButtonListener);
            this.add(newButton);
            this.addSeparator();
            this.add(resignButton);
            this.add(Box.createHorizontalGlue());
            this.add(undoButton);
            this.add(redoButton);
            this.setFloatable(false);
        } else if(type.equals("bottom")) {
            settingsButton = new JButton("Settings");
            settingsButton.addActionListener(myButtonListener);
            this.add(Box.createHorizontalGlue());
            this.add(settingsButton);
        }
    }

    public JButton getNewButton() {
        return newButton;
    }
}


