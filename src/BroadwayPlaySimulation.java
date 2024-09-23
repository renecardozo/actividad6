import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BroadwayPlaySimulation extends JPanel {
    // UI Components
    private JTextArea resultArea;
    private JTable occupancyTable;
    private JTable priceWeeksMatrix;

    // Constants
    private final double developmentCost = 5_000_000;
    private final int showsPerWeek = 8;
    private final double theaterOpeningCostPerNight = 1_000;
    private final double ticketPrice = 50.00;
    private final double dealerProfitPerTicket = 1.50;
    private final int theaterCapacity = 800;
    private final double expectedOccupancy = 0.80; // 80%
    private double exectedProfitFor80Percet = 0.0;

    public BroadwayPlaySimulation() {
        setSize(900, 600);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Solution A ", createSolutionAPanel());
        tabbedPane.addTab("Solution B", createSolutionBPanel());
        tabbedPane.addTab("Solution C", createSolutionCPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createSolutionAPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Solution A: Weeks for 100% Return"));

        JButton calculateAButton = new JButton("Calculate Weeks for 100% Return");
        calculateAButton.addActionListener(e -> calculateSolutionA());

        // Result area
        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        panel.add(calculateAButton);
        panel.add(resultArea);
        return panel;
    }

    private void calculateSolutionA() {
        double weeks = calculateWeeksFor100PercentReturn();
        resultArea.setText("Weeks needed for 100% return: " + String.format("%.2f", weeks));
    }

    private double calculateWeeksFor100PercentReturn() {
        //Total Revenue Needed=Development Cost×2=5,000,000×2=10,000,000
        double totalRevenueNeeded = developmentCost * 2; // 100% return
       //  Tickets Sold=Theater Capacity×Occupancy Rate=800×0.80=640
        double ticketsSold = theaterCapacity * expectedOccupancy;
        // Revenue per Show=Tickets Sold×Ticket Price=640×50=32,000// 80% occupancy
        double revenuePerShow = ticketsSold * ticketPrice;
        // Total Revenue per Week=Revenue per Show × Shows per Week=32,000×8=256,000
        double revenuePerWeek = revenuePerShow * showsPerWeek;
        /*
        Total Revenue Needed =  totalRevenueNeeded / revenuePerWeek;
         */
        return totalRevenueNeeded / revenuePerWeek;
    }

    private JPanel createSolutionBPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Solution B: Profit by Occupancy"));

        JButton calculateBButton = new JButton("Calculate Profit Table");
        calculateBButton.addActionListener(e -> calculateSolutionB());

        occupancyTable = new JTable(new DefaultTableModel(new Object[]{"% Seats Filled", "Profit"}, 0));
        JScrollPane scrollPane = new JScrollPane(occupancyTable);

        panel.add(calculateBButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void calculateSolutionB() {
        DefaultTableModel model = (DefaultTableModel) occupancyTable.getModel();
        model.setRowCount(0); // Clear previous data

        for (int i = 60; i <= 100; i += 5) {
            double profit = calculateProfitForOccupancy(i / 100.0);
            model.addRow(new Object[]{i + "%", String.format("$%.2f", profit)});
        }
    }

    private double calculateProfitForOccupancy(double occupancy) {
        double numberWeeks = 100;
        double result = numberWeeks *
                (showsPerWeek * theaterCapacity * occupancy* (ticketPrice + dealerProfitPerTicket)
                        - theaterOpeningCostPerNight * 7) - developmentCost;
        if (occupancy == 0.80) {
            exectedProfitFor80Percet = result;
        }
        return result;
    }

    private JPanel createSolutionCPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Solution C: Profit by Price & Weeks"));

        JButton calculateCButton = new JButton("Calculate Profit Matrix");
        calculateCButton.addActionListener(e -> calculateSolutionC());

        priceWeeksMatrix = new JTable(new DefaultTableModel(new Object[]{"Ticket Price", "40 Weeks", "60 Weeks", "80 Weeks", "100 Weeks", "120 Weeks", "140 Weeks", "160 Weeks", "180 Weeks", "200 Weeks"}, 0));
        JScrollPane scrollPane = new JScrollPane(priceWeeksMatrix);

        panel.add(calculateCButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void calculateSolutionC() {
        DefaultTableModel model = (DefaultTableModel) priceWeeksMatrix.getModel();
        model.setRowCount(0); // Clear previous data

        double[] ticketPrices = {30, 35, 40, 45, 50, 55, 60, 65, 70};
        int[] weeks = {40, 60, 80, 100, 120, 140, 160, 180, 200};

        for (double price : ticketPrices) {
            Object[] row = new Object[weeks.length + 1];
            row[0] = String.format("$%.2f", price);
            for (int j = 0; j < weeks.length; j++) {
                double profit = calculateProfitForPriceAndWeeks(price, weeks[j]);
                row[j + 1] = String.format("$%.2f", profit);
            }
            model.addRow(row);
        }
    }

    private double calculateProfitForPriceAndWeeks(double price, int weeks) {
        return weeks * ( showsPerWeek * theaterCapacity * expectedOccupancy * ( price + dealerProfitPerTicket) - theaterOpeningCostPerNight * 7) - developmentCost;
    }

}
