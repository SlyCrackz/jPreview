package gui;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class WindowFocuser {

    public void focusWindow(long windowId) {
        try {
            String command = "gdbus call --session --dest org.gnome.Shell " +
                    "--object-path /org/gnome/Shell/Extensions/Windows " +
                    "--method org.gnome.Shell.Extensions.Windows.Activate " +
                    windowId;

            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //CALL IT LIKE THIS
//    table.addMouseListener(new MouseAdapter() {
//        public void mouseClicked(MouseEvent e) {
//            if (e.getClickCount() == 1) {
//                JTable target = (JTable)e.getSource();
//                int row = target.getSelectedRow();
//                if (row != -1) {
//                    long windowId = Long.parseLong(tableModel.getValueAt(row, 0).toString());
//                    new WindowFocuser().focusWindow(windowId);
//                }
//            }
//        }
//    });

}


