import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;

public class CopyShopSimulation extends JPanel {

    private JTextField demandField;
    private JTextArea resultArea;
    private JTable profitTable;
    private JPanel chartPanel;
    private JPanel solutionPanel;

    private final double copyMachineRented = 3.0;
    private final double annualRentalCost = 5000.0;
    private final double otherMonthlyFixedCosts = 400.0;
    private final double yearInMonths = 12.0;
    private final double copyCharges = 0.10;
    private final double costPerCopy = 0.03;

    public CopyShopSimulation() {
//        setTitle("Copy Shop Profit Simulation");
        setSize(900, 700);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main Panel to switch between different solutions
        solutionPanel = new JPanel(new CardLayout());

        // Add buttons to switch between solutions
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton solutionAButton = new JButton("Solution A");
        solutionAButton.addActionListener(e -> switchToSolution("SolutionA"));

        JButton solutionBButton = new JButton("Solution B");
        solutionBButton.addActionListener(e -> switchToSolution("SolutionB"));

        JButton solutionCButton = new JButton("Solution C");
        solutionCButton.addActionListener(e -> switchToSolution("SolutionC"));

        buttonPanel.add(solutionAButton);
        buttonPanel.add(solutionBButton);
        buttonPanel.add(solutionCButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(solutionPanel, BorderLayout.CENTER);

        // Add solution panels
        solutionPanel.add(createSolutionAPanel(), "SolutionA");
        solutionPanel.add(createSolutionBPanel(), "SolutionB");
        solutionPanel.add(createSolutionCPanel(), "SolutionC");

        setVisible(true);
    }

    private JPanel createSolutionAPanel() {
        JPanel panelA = new JPanel(new BorderLayout());

        // Create the table data
        String[] columnNames = {"Copiers (1)", "Copiers (2)", "Copiers (3)", "Copiers (4)", "Copiers (5)"};
        Object[][] data = new Object[4][5];  // 4 rows (demands) and 5 columns (copiers)

        // Initialize the row headers with daily demands
        int[] dailyDemands = {500, 1000, 1500, 2000};

        // Fill the table with recalculated data
        for (int i = 0; i < dailyDemands.length; i++) {
            int dailyDemand = dailyDemands[i];
            for (int numCopiers = 1; numCopiers <= 5; numCopiers++) {
                data[i][numCopiers - 1] = calculateProfitForCopiers(numCopiers, dailyDemand);
            }
        }

        // Create a JTable with the data
        JTable profitTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(profitTable);

        // Add the table to the panel
        panelA.add(scrollPane, BorderLayout.CENTER);

        // Add a button to refresh the calculations
        JButton refreshButton = new JButton("Recalculate Profit");
        panelA.add(refreshButton, BorderLayout.SOUTH);

        // Add action listener for the refresh button
        refreshButton.addActionListener(e -> {
            // Recalculate the profits and update the table
            for (int i = 0; i < dailyDemands.length; i++) {
                int dailyDemand = dailyDemands[i];
                for (int numCopiers = 1; numCopiers <= 5; numCopiers++) {
                    profitTable.setValueAt(calculateProfitForCopiers(numCopiers, dailyDemand), i, numCopiers - 1);
                }
            }
        });

        return panelA;
    }

    private double calculateProfitForCopiers(int numCopiers, int dailyDemand) {
        int annualDemand = dailyDemand * 365;
        int copiesPerCopier = Math.min(100000, annualDemand / numCopiers);
        int totalCopies = copiesPerCopier * numCopiers;

        double totalRevenue = totalCopies * 0.10; // Revenue per copy
        double totalCost = (numCopiers * 5000) + 4800 + (totalCopies * 0.03); // Costs
        return totalRevenue - totalCost;
    }

    private double getCopyMade() {
        return (copyMachineRented * annualRentalCost + yearInMonths * otherMonthlyFixedCosts)/(copyCharges-costPerCopy);
    }
    private JPanel createSolutionBPanel() {
        JPanel panelB = new JPanel(new BorderLayout());

        // Input panel for rented copiers and daily demand
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JTextField rentedCopiersField = new JTextField();
        JTextField dailyDemandField = new JTextField();

        JButton calculateButton = new JButton("Calculate");

        inputPanel.add(new JLabel("Rented Copiers:"));
        inputPanel.add(rentedCopiersField);
//        inputPanel.add(new JLabel("Daily Demand:"));
//        inputPanel.add(dailyDemandField);
        inputPanel.add(calculateButton);

        panelB.add(inputPanel, BorderLayout.NORTH);

        // Table for results
        String[] columnNames = {"Rented Copiers", "Daily Demand", "Copies Made", "Annual Profit"};
        Object[][] data = new Object[1][4]; // One row for results
        JTable resultTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        panelB.add(scrollPane, BorderLayout.CENTER);

        // Action listener for the calculate button
        calculateButton.addActionListener(e -> {
            try {
                int rentedCopiers = Integer.parseInt(rentedCopiersField.getText());
                double copiesMade = getCopyMade();

                double dailyDemand = copiesMade / 365;
                double annualProfit = calculateAnnualProfit(rentedCopiers, dailyDemand);

                // Update the table with results
                data[0][0] = rentedCopiers;
                data[0][1] = dailyDemand;
                data[0][2] = copiesMade;
                data[0][3] = annualProfit;
                resultTable.setValueAt(data[0][0], 0, 0);
                resultTable.setValueAt(data[0][1], 0, 1);
                resultTable.setValueAt(data[0][2], 0, 2);
                resultTable.setValueAt(data[0][3], 0, 3);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panelB, "Please enter valid numbers for rented copiers and daily demand.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panelB;
    }

    private double calculateAnnualProfit(double rentedCopiers, double dailyDemand) {
        double annualDemand = dailyDemand * 365;
        double copiesMade = Math.min(annualDemand, rentedCopiers * 100000);
        double totalRevenue = copiesMade * 0.10;
        double totalCost = (rentedCopiers * 5000) + (400 * 12) + (copiesMade * 0.03);
        return totalRevenue - totalCost;
    }

    private JPanel createSolutionCPanel() {
        JPanel panelC = new JPanel(new BorderLayout());

        // Instruction area
        JTextArea infoArea = new JTextArea(5, 30);
        infoArea.setText("This panel will display a graph showing the profit as a function of the number of copiers.");
        infoArea.setEditable(false);
        panelC.add(new JScrollPane(infoArea), BorderLayout.CENTER);

        // Chart panel for visualizing the profit
        chartPanel = new JPanel();
        chartPanel.setLayout(new BorderLayout());
        panelC.add(chartPanel, BorderLayout.SOUTH);

        JButton chartButton = new JButton("Display Profit Chart");
        panelC.add(chartButton, BorderLayout.NORTH);

        // Action listener for solution C
        chartButton.addActionListener(e -> displayProfitChart());

        return panelC;
    }

    // Method to switch between solutions
    private void switchToSolution(String solution) {
        CardLayout cl = (CardLayout) (solutionPanel.getLayout());
        cl.show(solutionPanel, solution);
    }

    // Method to calculate Solution A (Task A: Annual Profits)
    private void calculateSolutionA() {
        int dailyDemand = Integer.parseInt(demandField.getText());

        // Calculate profits and update the result area
        String profitResults = calculateProfits(dailyDemand);
        resultArea.setText(profitResults);
    }

    // Method to calculate profits (Task A)
    private String calculateProfits(int dailyDemand) {
        StringBuilder result = new StringBuilder("Annual Profits for Various Copiers:\n\n");

        for (int numCopiers = 1; numCopiers <= 5; numCopiers++) {
            int annualDemand = dailyDemand * 365;
            int copiesPerCopier = Math.min(100000, annualDemand / numCopiers);
            int totalCopies = copiesPerCopier * numCopiers;

            double totalRevenue = totalCopies * 0.10; // Revenue per copy
            double totalCost = (numCopiers * 5000) + 4800 + (totalCopies * 0.03); // Costs
            double profit = totalRevenue - totalCost;

            result.append("Number of Copiers: ").append(numCopiers)
                    .append(" | Daily Demand: ").append(dailyDemand)
                    .append(" | Annual Profit: $").append(String.format("%.2f", profit)).append("\n");
        }

        return result.toString();
    }

    // Method to calculate break-even point (Solution B)
    private void calculateSolutionB() {
        // Implement the logic to calculate break-even point for 3 copiers
        int dailyDemand = 0;
        for (dailyDemand = 500; dailyDemand <= 2000; dailyDemand += 500) {
            int annualDemand = dailyDemand * 365;
            int copiesPerCopier = Math.min(100000, annualDemand / 3);
            int totalCopies = copiesPerCopier * 3;

            double totalRevenue = totalCopies * 0.10;
            double totalCost = (3 * 5000) + 4800 + (totalCopies * 0.03);

            if (totalRevenue >= totalCost) {
                resultArea.setText("Break-even achieved at daily demand of: " + dailyDemand + " copies/day.");
                break;
            }
        }
    }

    // Method to create and display the profit chart (Solution C)
    private void displayProfitChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(calculateProfit(1, 500), "500 copies/day", "1 Copier");
        dataset.addValue(calculateProfit(2, 500), "500 copies/day", "2 Copiers");
        dataset.addValue(calculateProfit(3, 500), "500 copies/day", "3 Copiers");
        dataset.addValue(calculateProfit(4, 500), "500 copies/day", "4 Copiers");
        dataset.addValue(calculateProfit(5, 500), "500 copies/day", "5 Copiers");

        dataset.addValue(calculateProfit(1, 2000), "2000 copies/day", "1 Copier");
        dataset.addValue(calculateProfit(2, 2000), "2000 copies/day", "2 Copiers");
        dataset.addValue(calculateProfit(3, 2000), "2000 copies/day", "3 Copiers");
        dataset.addValue(calculateProfit(4, 2000), "2000 copies/day", "4 Copiers");
        dataset.addValue(calculateProfit(5, 2000), "2000 copies/day", "5 Copiers");

        JFreeChart chart = ChartFactory.createLineChart(
                "Profit vs Number of Copiers",
                "Number of Copiers",
                "Profit ($)",
                dataset
        );

        ChartPanel chartPanelComponent = new ChartPanel(chart);
        chartPanel.removeAll();
        chartPanel.add(chartPanelComponent, BorderLayout.CENTER);
        chartPanel.validate();
    }

    // Helper function to calculate profit for Solution C
    private double calculateProfit(int numCopiers, int dailyDemand) {
        int annualDemand = dailyDemand * 365;
        int copiesPerCopier = Math.min(100000, annualDemand / numCopiers);
        int totalCopies = copiesPerCopier * numCopiers;

        double totalRevenue = totalCopies * 0.10; // Revenue per copy
        double totalCost = (numCopiers * 5000) + 4800 + (totalCopies * 0.03); // Costs
        return totalRevenue - totalCost;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(CopyShopSimulation::new);
//    }
}
