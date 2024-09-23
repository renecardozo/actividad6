import javax.swing.*;
import java.awt.*;

public class Activity6App extends JFrame {

    public Activity6App() {
        setTitle("Activity 6 - System Simulation");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // First tab with LemonadeStandSimulation
        JPanel simulationPanel = new JPanel(new BorderLayout());
        LemonadeStandSimulation simulation = new LemonadeStandSimulation();
        simulationPanel.add(simulation, BorderLayout.CENTER);
        tabbedPane.addTab("Lemonade Stand Simulation", simulationPanel);

        // Second tab (can add content here)
        JPanel secondTab = new JPanel(new BorderLayout());
        BroadwayPlaySimulation broadwayPlaySimulation = new BroadwayPlaySimulation();
        secondTab.add(broadwayPlaySimulation, BorderLayout.CENTER);
        tabbedPane.addTab("Broadway Play Profit Simulation", secondTab);

        // Third tab (can add content here)
        JPanel thirdTab = new JPanel(new BorderLayout());
        CopyShopSimulation copyShopSimulation = new CopyShopSimulation();
        thirdTab.add(copyShopSimulation, BorderLayout.CENTER);
        tabbedPane.addTab("Copy Shop Profit Simulation", thirdTab);

        // Add tabbed pane to the main frame
        add(tabbedPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Activity6App app = new Activity6App();
            app.setVisible(true);
        });
    }
}
