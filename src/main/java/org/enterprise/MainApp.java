package org.enterprise;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Enterprise enterprise = new Enterprise();
            EnterpriseGUI enterpriseGUI = new EnterpriseGUI(enterprise);
        });
    }
}
