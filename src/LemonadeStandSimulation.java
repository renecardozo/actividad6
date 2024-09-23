import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LemonadeStandSimulation extends JPanel {
    // UI components for manual input and result display
    private JTextField salesVolumeField;
    private JTextField variableCostField;
    private JTextArea resultArea;

    // UI components for tables
    private JTable profitTable, profitMatrixTable;
    private JButton calculateProfitBtn, resetBtn;

    // Constants
    private final double fixedCost = 50.00;  // Fixed cost per week
    private final double pricePerCup = 0.50;  // Price per cup
    private final double defaultVariableCost = 0.20;  // Default variable cost
    private final int[] salesVolumes = {100, 150, 200, 250, 300, 350, 400, 450, 500}; // Sales volumes array
    private final double[] variableCosts = {0.10, 0.15, 0.20, 0.25, 0.30, 0.35, 0.40, 0.45}; // Variable costs array

    public LemonadeStandSimulation() {
        setSize(900, 500);
        setLayout(new BorderLayout());

        // Create Tabbed Pane for tables and manual input
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add the manual input panel as the first tab
        tabbedPane.addTab("Manual Input", createManualInputPanel());

        // Add the profit table tab
        tabbedPane.addTab("Sales Volume vs Profit", createProfitTablePanel());

        // Add the profit matrix table tab
        tabbedPane.addTab("Sales Volume and Var. Cost Matrix", createProfitMatrixPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // Method to create manual input panel
    private JPanel createManualInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Sales Volume (cups):"));
        salesVolumeField = new JTextField();
        formPanel.add(salesVolumeField);

        formPanel.add(new JLabel("Variable Cost per Cup (Bs):"));
        variableCostField = new JTextField(String.valueOf(defaultVariableCost));
        formPanel.add(variableCostField);

        JButton calculateButton = new JButton("Calculate Profit");
        formPanel.add(calculateButton);

        inputPanel.add(formPanel, BorderLayout.NORTH);

        // Result Area
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        inputPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        // Action listener for the Calculate button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateProfit();
            }
        });

        return inputPanel;
    }

    // Method to create profit table panel
    private JPanel createProfitTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Profit table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Sales Volume");
        tableModel.addColumn("Profit");

        profitTable = new JTable(tableModel);

        // Add sales volumes and calculate profits
        for (int salesVolume : salesVolumes) {
            Object[] row = new Object[2];
            row[0] = salesVolume;
            row[1] = String.format("%.2f", calculateProfit(salesVolume, 0.20));  // Using default variable cost (0.20)
            tableModel.addRow(row);
        }

        // Scroll Pane for the table
        JScrollPane scrollPane = new JScrollPane(profitTable);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        calculateProfitBtn = new JButton("Recalculate Profits");
        resetBtn = new JButton("Reset");

        buttonsPanel.add(calculateProfitBtn);
        buttonsPanel.add(resetBtn);

        // Add action listener for recalculating profits
        calculateProfitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recalculateProfits(0.20); // Recalculate with the default variable cost
            }
        });

        // Reset button action
        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTable();
            }
        });

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Method to create profit matrix table panel
    private JPanel createProfitMatrixPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Matrix table
        DefaultTableModel matrixTableModel = new DefaultTableModel();
        matrixTableModel.addColumn("Sales Volume / Var Cost");

        // Add column headers (Variable Costs)
        for (double variableCost : variableCosts) {
            matrixTableModel.addColumn(String.format("%.2f", variableCost));
        }

        // Fill the table with rows (Sales Volumes and profits for different variable costs)
        for (int salesVolume : salesVolumes) {
            Object[] row = new Object[variableCosts.length + 1];
            row[0] = salesVolume; // First column is Sales Volume
            for (int j = 0; j < variableCosts.length; j++) {
                row[j + 1] = String.format("%.2f", calculateProfit(salesVolume, variableCosts[j]));
            }
            matrixTableModel.addRow(row);
        }

        // Create the JTable and add it to a JScrollPane
        profitMatrixTable = new JTable(matrixTableModel);
        JScrollPane scrollPane = new JScrollPane(profitMatrixTable);

        // Add the scroll pane to the panel
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Method to calculate profit for a specific sales volume and variable cost
    private double calculateProfit(int salesVolume, double variableCost) {
        double revenue = salesVolume * pricePerCup;
        double totalVariableCost = salesVolume * variableCost;
        double totalCost = fixedCost + totalVariableCost;
        return revenue - totalCost;  // Profit
    }

    // Method to recalculate profits in the profit table
    private void recalculateProfits(double variableCost) {
        DefaultTableModel tableModel = (DefaultTableModel) profitTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int salesVolume = (int) tableModel.getValueAt(i, 0);
            double profit = calculateProfit(salesVolume, variableCost);
            tableModel.setValueAt(String.format("%.2f", profit), i, 1);
        }
    }

    // Method to reset the profit table
    private void resetTable() {
        DefaultTableModel tableModel = (DefaultTableModel) profitTable.getModel();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 1);
        }
    }

    // Method to calculate profit and update the resultArea
    private void calculateProfit() {
        try {
            // Get user inputs
            int salesVolume = Integer.parseInt(salesVolumeField.getText());
            double variableCost = Double.parseDouble(variableCostField.getText());

            // Profit calculation
            double revenue = salesVolume * pricePerCup;
            double totalVariableCost = salesVolume * variableCost;
            double totalCost = fixedCost + totalVariableCost;
            double profit = revenue - totalCost;

            // Break-even volume calculation
            int breakEvenVolume = (int) Math.ceil(fixedCost / (pricePerCup - variableCost));

            // Display results
            resultArea.setText("Results:\n");
            resultArea.append("Total Revenue: Bs " + String.format("%.2f", revenue) + "\n");
            resultArea.append("Total Variable Cost: Bs " + String.format("%.2f", totalVariableCost) + "\n");
            resultArea.append("Total Cost (Fixed + Variable): Bs " + String.format("%.2f", totalCost) + "\n");
            resultArea.append("Profit: Bs " + String.format("%.2f", profit) + "\n");
            resultArea.append("Break-even sales volume: " + breakEvenVolume + " cups\n");

            // Discuss sales volume and profits
            resultArea.append("\nDiscussion:\n");
            if (salesVolume >= breakEvenVolume) {
                resultArea.append("Profit is positive above break-even volume.\n");
            } else {
                resultArea.append("Sales volume below break-even results in a loss.\n");
            }
            resultArea.append("Increasing sales volume increases profit as long as variable cost remains lower than price per cup.\n");

        } catch (NumberFormatException ex) {
            resultArea.setText("Error: Please enter valid numbers for sales volume and variable cost.");
        }
    }
}
