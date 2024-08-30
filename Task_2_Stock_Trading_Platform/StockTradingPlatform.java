/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Task_2_Stock_Trading_Platform;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dell
 */
public class StockTradingPlatform extends javax.swing.JFrame {

    /**
     * Creates new form StockTradingPlatform
     */
    
    public StockTradingPlatform(int userId, String username) {
        initComponents();
    // Initialization code
       if (txtID == null || txtUsername == null) {
        System.err.println("Components are not initialized!");
        return;
    }
    
    txtID.setText(String.valueOf("   "+userId));
    txtUsername.setText("   "+username);
}
    
    public StockTradingPlatform() {
        initComponents();
        
        
        
        setMarketDataToTbl();
        updateStatusBasedOnPanelName();
        
       // Add a ChangeListener to update the status when the tab changes
        tabbedPane.addChangeListener((javax.swing.event.ChangeEvent e) -> {
            updateStatusBasedOnPanelName();
        });
        
        setPortfolioDataToTbl();
    }
    
private void updateStatusBasedOnPanelName() {
    int selectedIndex = tabbedPane.getSelectedIndex();
    String selectedTabTitle = tabbedPane.getTitleAt(selectedIndex).trim(); // Remove any extra spaces for comparison

    switch (selectedTabTitle) {
        case "Market Data":
            txtStatus.setText("You are viewing the market. Please select an action.");
            break;
        case "Buy Stocks":
            txtStatus.setText("Ready to buy stocks. Please enter the stock symbol and quantity.");
            break;
        case "Sell Stocks":
            txtStatus.setText("Ready to sell stocks. Please enter the stock symbol and quantity.");
            break;
        case "Portfolio":
            txtStatus.setText("View your portfolio and manage your investments.");
            break;
        default:
            txtStatus.setText("Welcome to the Stock Trading Platform.");
            break;
    }
}
    
     DefaultTableModel model1;
    // method to display market data in market data tab table
    public void setMarketDataToTbl() {
        try {
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("select * from market_data");
             
             while(rs.next()) {
                String id = rs.getString("stock_id");
                String symbol = rs.getString("symbol");
                String company = rs.getString("company_name");
                String price = rs.getString("price");
                String volume = rs.getString("volume");
                 
                Object obj[] = {id, symbol, company, price, volume};
                model1 = (DefaultTableModel) marketDataTbl.getModel();
                model1.addRow(obj);
                
             }
        }catch (Exception e) {
               e.printStackTrace();
            }
    }
    
       // Method to display portfolio data in market data tab table
    public void setPortfolioDataToTbl() {
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM portfolio");

        DefaultTableModel model2 = (DefaultTableModel) TBL.getModel();
        
        // Clear existing rows in the table model
        model2.setRowCount(0);

        while (rs.next()) {
            // Retrieve data from the result set
            String userID = rs.getString("userID");
            String symbol = rs.getString("stock_symbol");
            String quantity = rs.getString("quantity");
            String totalAmount = rs.getString("total_amount");
            String purchasePrice = rs.getString("purchase_price");
            String transactionType = rs.getString("transaction_type");

            Object[] obj = {userID, symbol, quantity, totalAmount, purchasePrice, transactionType}; // Removed currentPrice
            model2.addRow(obj);
        }

        // Close resources
        rs.close();
        st.close();
        con.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    // method for refresh data button
    private void refreshData() {
    // Clear existing data in the table
    DefaultTableModel model = (DefaultTableModel) marketDataTbl.getModel();
    model.setRowCount(0);
    
    JOptionPane.showMessageDialog(this, "Data Refreshed!");
     txtStatus.setText("Data refresh completed.");
    // Re-fetch and display updated data
    setMarketDataToTbl();
}

    public void fetchAndDisplayDetails() {
        // Retrieve the company name from user input
        String companyName = txtCompanyName.getText().trim();
        
        // validate input
        if(companyName.isEmpty()) {
            JOptionPane.showMessageDialog(tab1, "Please enter a company name.");
            return;
        }

         try {
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
             String sql = "SELECT * FROM company_details WHERE company_name = ?";
             PreparedStatement st = con.prepareStatement(sql);
             
             st.setString(1, companyName);
             ResultSet rs = st.executeQuery();
        
             if (rs.next()) {
            String id  = rs.getString("company_id");
            String name = rs.getString("company_name");
            String sector = rs.getString("sector");
            String marketCap = rs.getString("market_cap");
            String ceo = rs.getString("ceo");
            String headquarters = rs.getString("headquarters");
            String recentNews = rs.getString("recent_news");
            
            // Format the details
            String details = "Company ID: " + id + "\n" +
                 "Company Name: " + name + "\n" +   
                 "Sector: " + sector + "\n" +
                 "Market Cap: " + marketCap + "\n" +
                 "CEO: " + ceo + "\n" +
                 "Headquarters: " + headquarters + "\n\n" +
                 "Recent News:\n" + recentNews;
            
            // Display details in the text area
            txtDetailArea.setText(details);
            // Update status message to indicate that fetching is complete
            txtStatus.setText("Details fetched successfully for " + companyName + ".");

             }
             else {
            // Display a message if no details are found
            txtDetailArea.setText("No details found for the company: " + companyName);
            JOptionPane.showMessageDialog(this, "No details found for the company");
        }
    }     catch (Exception e) {
            e.printStackTrace();
        }
         }
    
 public class StockInfo {
    private final String companyName;
    private final BigDecimal pricePerShare;

    // Constructor
    public StockInfo(String companyName, BigDecimal pricePerShare) {
        this.companyName = companyName;
        this.pricePerShare = pricePerShare;
    }

    // Getter for company name
    public String getCompanyName() {
        return companyName;
    }

    // Getter for price per share
    public BigDecimal getPricePerShare() {
        return pricePerShare;
    }
}

    // Method for automatically getting company name, price per share, and total amount after entering stock symbol
public StockInfo getStockInfo(String symbol) {
    StockInfo stockInfo = null;
    
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement st = con.prepareStatement("SELECT * FROM market_data WHERE symbol = ?")) {
        
        st.setString(1, symbol);
        ResultSet rs = st.executeQuery();

        if (rs.next()) {
            String companyName = rs.getString("company_name");
            BigDecimal pricePerShare = rs.getBigDecimal("price");
            stockInfo = new StockInfo(companyName, pricePerShare);
        } else {
            // Handle case where stock symbol is not found
            JOptionPane.showMessageDialog(null, "Stock symbol not found.");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return stockInfo;
}

private boolean isUserIDExists(int id) {
    String sql = "SELECT 1 FROM users WHERE user_id = ?";
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement st = con.prepareStatement(sql)) {
        
        st.setInt(1, id);
        try (ResultSet rs = st.executeQuery()) {
            return rs.next(); // Returns true if a row is found
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Return false if an exception occurs
    }
}


// Method to upsert (insert or update) portfolio record 
private void upsertPortfolioRecord(int id, String stockSymbol, int quantity, BigDecimal totalAmount, BigDecimal purchasePrice, String transactionType) throws SQLException {
    // SQL query to check if the stock already exists in the portfolio for the given user
    String selectSql = "SELECT quantity, total_amount FROM portfolio WHERE userID = ? AND stock_symbol = ?";
    
    // SQL query to update an existing portfolio record with new values
    String updateSql = "UPDATE portfolio SET quantity = ?, total_amount = ?, purchase_price = ?, transaction_type = ? WHERE userID = ? AND stock_symbol = ?";

    // SQL query to insert a new portfolio record
    String insertSql = "INSERT INTO portfolio (userID, stock_symbol, quantity, total_amount, purchase_price, transaction_type) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement selectSt = con.prepareStatement(selectSql);
         PreparedStatement updateSt = con.prepareStatement(updateSql);
         PreparedStatement insertSt = con.prepareStatement(insertSql)) {

        // Set parameters for the select query
        selectSt.setInt(1, id);
        selectSt.setString(2, stockSymbol);
        ResultSet rs = selectSt.executeQuery();

        if (rs.next()) {
            // If the stock exists, retrieve the current quantity and total amount
            int existingQuantity = rs.getInt("quantity");
            BigDecimal existingTotalAmount = rs.getBigDecimal("total_amount");

            // Calculate the new quantity and total amount by adding the existing and new values
            int newQuantity = existingQuantity + quantity;
            BigDecimal newTotalAmount = existingTotalAmount.add(totalAmount);

            // Set parameters for the update query
            updateSt.setInt(1, newQuantity);
            updateSt.setBigDecimal(2, newTotalAmount);
            updateSt.setBigDecimal(3, purchasePrice);
            updateSt.setString(4, transactionType);
            updateSt.setInt(5, id);
            updateSt.setString(6, stockSymbol);

            // Execute the update query
            updateSt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Stock Portfolio updated");
        } else {
            // If the stock does not exist, set parameters for the insert query
            insertSt.setInt(1, id);
            insertSt.setString(2, stockSymbol);
            insertSt.setInt(3, quantity);
            insertSt.setBigDecimal(4, totalAmount);
            insertSt.setBigDecimal(5, purchasePrice);
            insertSt.setString(6, transactionType);

            // Execute the insert query
            insertSt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Stock Portfolio added");
        }
    }
}

private void fetchAndDisplayStockInfoBuy() {
     String stockSymbol = txtStockSymbol.getText().trim();
    String numberOfSharesText = txtNumberOfShares.getText().trim();

    if (stockSymbol.isEmpty() || numberOfSharesText.isEmpty()) {
        return; // Don't proceed if any input is missing
    }

    int numberOfShares;
    try {
        numberOfShares = Integer.parseInt(numberOfSharesText); // Parse the number of shares
    } catch (NumberFormatException e) {
        return; // If invalid number of shares just return
    }

    StockInfo stockInfo = getStockInfo(stockSymbol);
    if (stockInfo == null) {
        return; // If stock symbol is invalid just return
    }

    BigDecimal pricePerShare = stockInfo.getPricePerShare();
    BigDecimal totalAmount = pricePerShare.multiply(new BigDecimal(numberOfShares));

    // Update text fields
    companyNameField.setText(stockInfo.getCompanyName());
    txtPricePerShare.setText(pricePerShare.toString());
    txtTotalAmount.setText(totalAmount.toString());
}

private void handleBuyTransaction(String transactionType, int id) {
    //stock info has already been fetched and displayed
    String stockSymbol = txtStockSymbol.getText().trim();
    String numberOfSharesText = txtNumberOfShares.getText().trim();
    String id1 = txtIdBuy.getText().trim();

    // Validate the input
    if (stockSymbol.isEmpty() || numberOfSharesText.isEmpty() || id1.isEmpty()) {
        txtStatus.setText("Error processing transaction");
        JOptionPane.showMessageDialog(this, "Please enter all required fields.");
        return;
    }

    int numberOfShares;
    try {
        numberOfShares = Integer.parseInt(numberOfSharesText); // Parse the number of shares
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid number of shares entered. Please enter a valid number.");
        txtStatus.setText("Error processing transaction");
        return;
    }

    if (!isUserIDExists(id)) {
        JOptionPane.showMessageDialog(this, "User ID does not exist.");
        txtStatus.setText("Error processing transaction");
        return;
    }

    // Fetch stock information (which should already be displayed)
    StockInfo stockInfo = getStockInfo(stockSymbol);
    BigDecimal pricePerShare = new BigDecimal(txtPricePerShare.getText().trim());
    BigDecimal totalAmount = new BigDecimal(txtTotalAmount.getText().trim());

    // Insert transaction into the database
    String sql = "INSERT INTO transactions (transaction_type, stock_symbol, company_name, number_of_shares, price_per_share, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement st = con.prepareStatement(sql)) {
        
        st.setString(1, transactionType);
        st.setString(2, stockSymbol);
        st.setString(3, stockInfo.getCompanyName());
        st.setInt(4, numberOfShares);
        st.setBigDecimal(5, pricePerShare);
        st.setBigDecimal(6, totalAmount);

        st.executeUpdate();

        upsertPortfolioRecord(id, stockSymbol, numberOfShares, totalAmount, pricePerShare, transactionType);

        JOptionPane.showMessageDialog(this, "Buy transaction completed successfully.");
        txtBuyResult.setText("Buy transaction completed successfully. You have purchased " + numberOfShares + " shares of " + stockInfo.getCompanyName() + " at " + pricePerShare + " per share. Total amount: " + totalAmount + ".");
        txtStatus.setText("Buy transaction completed successfully");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error processing transaction");
        txtStatus.setText("buy transaction failed.");
    }  
}

private void fetchAndDisplayStockInfoSell() {
     String stockSymbol = txtStockSymbol1.getText().trim();
    String numberOfSharesText = txtNumberOfShares1.getText().trim();

    if (stockSymbol.isEmpty() || numberOfSharesText.isEmpty()) {
        return; // Don't proceed if any input is missing
    }

    int numberOfShares;
    try {
        numberOfShares = Integer.parseInt(numberOfSharesText); // Parse the number of shares
    } catch (NumberFormatException e) {
        return; // If invalid number of shares just return
    }

    StockInfo stockInfo = getStockInfo(stockSymbol);
    if (stockInfo == null) {
        return; // If stock symbol is invalid just return
    }

    BigDecimal pricePerShare = stockInfo.getPricePerShare();
    BigDecimal totalAmount = pricePerShare.multiply(new BigDecimal(numberOfShares));

    // Update text fields
    companyNameField1.setText(stockInfo.getCompanyName());
    txtPricePerShare1.setText(pricePerShare.toString());
    txtTotalAmount1.setText(totalAmount.toString());
}

private void handleSellTransaction(String transactionType, int id) {
    //stock info has already been fetched and displayed
    String stockSymbol = txtStockSymbol1.getText().trim();
    String numberOfSharesText = txtNumberOfShares1.getText().trim();
    String id2 = txtIdSell.getText().trim();

    // Validate the input
    if (stockSymbol.isEmpty() || numberOfSharesText.isEmpty() || id2.isEmpty()) {
        txtStatus.setText("Error processing transaction");
        JOptionPane.showMessageDialog(this, "Please enter all required fields.");
        return;
    }

    int numberOfShares;
    try {
        numberOfShares = Integer.parseInt(numberOfSharesText); // Parse the number of shares
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid number of shares entered. Please enter a valid number.");
        txtStatus.setText("Error processing transaction");
        return;
    }

    if (!isUserIDExists(id)) {
        JOptionPane.showMessageDialog(this, "User ID does not exist.");
        txtStatus.setText("Error processing transaction");
        return;
    }

    // Fetch stock information (which should already be displayed)
    StockInfo stockInfo = getStockInfo(stockSymbol);
    BigDecimal pricePerShare = new BigDecimal(txtPricePerShare1.getText().trim());
    BigDecimal totalAmount = new BigDecimal(txtTotalAmount1.getText().trim());

    // Insert transaction into the database
    String sql = "INSERT INTO transactions (transaction_type, stock_symbol, company_name, number_of_shares, price_per_share, total_amount) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement st = con.prepareStatement(sql)) {
        
        st.setString(1, transactionType);
        st.setString(2, stockSymbol);
        st.setString(3, stockInfo.getCompanyName());
        st.setInt(4, numberOfShares);
        st.setBigDecimal(5, pricePerShare);
        st.setBigDecimal(6, totalAmount);

        st.executeUpdate();

        upsertPortfolioRecord(id, stockSymbol, numberOfShares, totalAmount, pricePerShare, transactionType);

        JOptionPane.showMessageDialog(this, "Sell transaction completed successfully.");
        txtSellResult.setText("Sell transaction completed successfully. You have sold " + numberOfShares + " shares of " + stockInfo.getCompanyName() + " at " + pricePerShare + " per share. Total amount: " + totalAmount + ".");
        txtStatus.setText("Sell transaction completed successfully");
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error processing transaction");
        txtStatus.setText("Sell transaction failed.");
    }  
}

    private void searchCurrentPrice() {
        String symbol = txtSymbol.getText().trim();
        if (symbol.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a stock symbol.");
            return;
        }
        
        // Query to fetch current price
        String query = "SELECT Price FROM market_data WHERE symbol = ?";
        
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
             PreparedStatement pst = con.prepareStatement(query)) {
             
            pst.setString(1, symbol);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                // Retrieve and display the current price
                String currentPrice = rs.getString("Price");
                txtCurrentPrice.setText(currentPrice);
            } else {
                txtCurrentPrice.setText("Not Found");
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving stock price.");
        }
    }

private void updatePortfolioSummary(int userId, JTextField txtTotalValue, 
                                     JTextField txtTotalInvestments, JTextField txtGainsLosses) throws SQLException {
    // SQL query to calculate total value, total investments, and gains/losses
    String summaryQuery = "SELECT " +
                          "COALESCE(SUM(p.quantity * m.price), 0) AS total_value, " +
                          "COALESCE(SUM(p.total_amount), 0) AS total_investments, " +
                          "COALESCE(SUM(CASE WHEN p.transaction_type = 'Sell' THEN (p.quantity * m.price) - p.total_amount ELSE 0 END), 0) AS gains_losses " +
                          "FROM portfolio p " +
                          "JOIN market_data m ON p.stock_symbol = m.symbol " +
                          "WHERE p.userID = ?";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Stock_Trading_Platform", "root", "programmer._.2004");
         PreparedStatement ps = con.prepareStatement(summaryQuery)) {

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            BigDecimal totalValue = rs.getBigDecimal("total_value");
            BigDecimal totalInvestments = rs.getBigDecimal("total_investments");
            BigDecimal gainsLosses = rs.getBigDecimal("gains_losses");

            // Set text fields with formatted values
            txtTotalValue.setText(totalValue != null ? totalValue.setScale(2, RoundingMode.HALF_UP).toString() : "0.00");
            txtTotalInvestments.setText(totalInvestments != null ? totalInvestments.setScale(2, RoundingMode.HALF_UP).toString() : "0.00");

            // Format gains/losses with sign
            if (gainsLosses != null) {
                if (gainsLosses.compareTo(BigDecimal.ZERO) > 0) {
                    txtGainsLosses.setText("+" + gainsLosses.setScale(2, RoundingMode.HALF_UP).toString());
                } else if (gainsLosses.compareTo(BigDecimal.ZERO) < 0) {
                    txtGainsLosses.setText(gainsLosses.setScale(2, RoundingMode.HALF_UP).toString());
                } else {
                    txtGainsLosses.setText("0.00");
                }
            } else {
                txtGainsLosses.setText("0.00");
            }
        }
    }
}



// Create the main frame
JFrame frame = new JFrame("Stock Trading Platform");

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        tab1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        marketDataTbl = new javax.swing.JTable();
        btnRefreshData = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnFetchDetails = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDetailArea = new javax.swing.JTextArea();
        txtCompanyName = new app.bolivia.swing.JCTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        tab2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtStockSymbol = new app.bolivia.swing.JCTextField();
        txtNumberOfShares = new app.bolivia.swing.JCTextField();
        jButton2 = new javax.swing.JButton();
        companyNameField = new app.bolivia.swing.JCTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPricePerShare = new app.bolivia.swing.JCTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTotalAmount = new app.bolivia.swing.JCTextField();
        jLabel10 = new javax.swing.JLabel();
        txtBuyResult = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtIdBuy = new app.bolivia.swing.JCTextField();
        tab3 = new javax.swing.JPanel();
        tab5 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txtStockSymbol1 = new app.bolivia.swing.JCTextField();
        txtNumberOfShares1 = new app.bolivia.swing.JCTextField();
        companyNameField1 = new app.bolivia.swing.JCTextField();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txtPricePerShare1 = new app.bolivia.swing.JCTextField();
        jLabel34 = new javax.swing.JLabel();
        txtTotalAmount1 = new app.bolivia.swing.JCTextField();
        jLabel35 = new javax.swing.JLabel();
        txtSellResult = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        txtIdSell = new app.bolivia.swing.JCTextField();
        jButton3 = new javax.swing.JButton();
        tab4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtTotalValue = new javax.swing.JTextField();
        txtTotalInvestments = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtGainsLosses = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TBL = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        txtCurrentPrice = new javax.swing.JTextField();
        txtSymbol = new app.bolivia.swing.JCTextField();
        txtid = new app.bolivia.swing.JCTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtID = new app.bolivia.swing.JCTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        txtUsername = new app.bolivia.swing.JCTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jCTextField2 = new app.bolivia.swing.JCTextField();
        txtStatus = new app.bolivia.swing.JCTextField();
        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 215, 0), 2));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Task_2_Stock_Trading_Platform/icons8_Sell_50px.png"))); // NOI18N
        jLabel1.setText("  Welcome to the Stock Trading Platform");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 530, 50));

        jPanel3.setBackground(new java.awt.Color(54, 69, 79));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabbedPane.setBackground(new java.awt.Color(0, 102, 204));
        tabbedPane.setForeground(new java.awt.Color(255, 255, 255));
        tabbedPane.setFont(new java.awt.Font("Sitka Text", 1, 18)); // NOI18N
        tabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tabbedPaneMouseEntered(evt);
            }
        });

        tab1.setBackground(new java.awt.Color(240, 248, 255));
        tab1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tab1MouseEntered(evt);
            }
        });

        marketDataTbl.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        marketDataTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "SYMBOL", "COMPANY ", "PRICE", "VOLUME"
            }
        ));
        marketDataTbl.setShowHorizontalLines(true);
        marketDataTbl.setShowVerticalLines(true);
        jScrollPane1.setViewportView(marketDataTbl);

        btnRefreshData.setBackground(new java.awt.Color(102, 102, 255));
        btnRefreshData.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        btnRefreshData.setText("REFRESH DATA");
        btnRefreshData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshDataActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Company Name :");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Details :");

        btnFetchDetails.setBackground(new java.awt.Color(102, 102, 255));
        btnFetchDetails.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        btnFetchDetails.setText("Fetch Details");
        btnFetchDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFetchDetailsActionPerformed(evt);
            }
        });

        txtDetailArea.setColumns(20);
        txtDetailArea.setRows(5);
        jScrollPane2.setViewportView(txtDetailArea);

        txtCompanyName.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        txtCompanyName.setPlaceholder("Enter company name.....");
        txtCompanyName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCompanyNameMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtCompanyNameMouseEntered(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(0, 102, 204));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Details Panel");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(96, 119, 170));
        jPanel11.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tab1Layout = new javax.swing.GroupLayout(tab1);
        tab1.setLayout(tab1Layout);
        tab1Layout.setHorizontalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addGap(207, 207, 207)
                        .addComponent(btnRefreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(96, 96, 96))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(42, 42, 42)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(37, 37, 37))))
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addGap(130, 130, 130)
                        .addComponent(btnFetchDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        tab1Layout.setVerticalGroup(
            tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCompanyName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addComponent(btnFetchDetails)
                        .addGap(30, 30, 30)
                        .addGroup(tab1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18))
                    .addGroup(tab1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefreshData, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25))))
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
        );

        tabbedPane.addTab("   Market Data          ", tab1);

        tab2.setBackground(new java.awt.Color(255, 248, 255));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel5.setText("Stock Symbol :");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel6.setText("No. Of Shares :");

        txtStockSymbol.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtStockSymbol.setPlaceholder("Enter stock symbol.....");
        txtStockSymbol.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockSymbolFocusLost(evt);
            }
        });
        txtStockSymbol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtStockSymbolMouseClicked(evt);
            }
        });

        txtNumberOfShares.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtNumberOfShares.setPlaceholder("Enter No. Of Shares.....");
        txtNumberOfShares.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberOfSharesFocusLost(evt);
            }
        });
        txtNumberOfShares.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNumberOfSharesMouseClicked(evt);
            }
        });
        txtNumberOfShares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumberOfSharesActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 102, 255));
        jButton2.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton2.setText("BUY");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        companyNameField.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        companyNameField.setPlaceholder("Company's Name.....");
        companyNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNameFieldActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel7.setText("Company Name :");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel8.setText("Price Per Share :");

        txtPricePerShare.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtPricePerShare.setPlaceholder("Price Per Share.....");
        txtPricePerShare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPricePerShareActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel9.setText("Total Amount :");

        txtTotalAmount.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtTotalAmount.setPlaceholder("Total Amount.....");
        txtTotalAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAmountActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setText("Result :");

        txtBuyResult.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel21.setText("User Id :");

        txtIdBuy.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtIdBuy.setPlaceholder("Enter User ID.....");

        javax.swing.GroupLayout tab2Layout = new javax.swing.GroupLayout(tab2);
        tab2.setLayout(tab2Layout);
        tab2Layout.setHorizontalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 918, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab2Layout.createSequentialGroup()
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtPricePerShare, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                                .addComponent(txtIdBuy, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(jLabel7))
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(jLabel6))
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addComponent(jLabel9)))
                        .addGap(18, 18, 18)
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(companyNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                            .addComponent(txtTotalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNumberOfShares, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(56, 56, 56))))
            .addGroup(tab2Layout.createSequentialGroup()
                .addGap(426, 426, 426)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        tab2Layout.setVerticalGroup(
            tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtStockSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtIdBuy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTotalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(txtPricePerShare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(37, 37, 37))
                    .addGroup(tab2Layout.createSequentialGroup()
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNumberOfShares, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(tab2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(companyNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jButton2)
                .addGap(39, 39, 39)
                .addGroup(tab2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuyResult, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(28, 28, 28))
        );

        tabbedPane.addTab("   Buy Stocks          ", tab2);

        tab3.setBackground(new java.awt.Color(240, 248, 255));
        tab3.setBorder(new javax.swing.border.MatteBorder(null));

        tab5.setBackground(new java.awt.Color(255, 248, 255));

        jLabel30.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel30.setText("Stock Symbol :");

        jLabel31.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel31.setText("No. Of Shares :");

        txtStockSymbol1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtStockSymbol1.setPlaceholder("Enter stock symbol.....");
        txtStockSymbol1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStockSymbol1FocusLost(evt);
            }
        });
        txtStockSymbol1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtStockSymbol1MouseClicked(evt);
            }
        });

        txtNumberOfShares1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtNumberOfShares1.setPlaceholder("Enter No. Of Shares.....");
        txtNumberOfShares1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberOfShares1FocusLost(evt);
            }
        });
        txtNumberOfShares1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNumberOfShares1MouseClicked(evt);
            }
        });
        txtNumberOfShares1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumberOfShares1ActionPerformed(evt);
            }
        });

        companyNameField1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        companyNameField1.setPlaceholder("Company's Name.....");
        companyNameField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                companyNameField1ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel32.setText("Company Name :");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel33.setText("Price Per Share :");

        txtPricePerShare1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtPricePerShare1.setPlaceholder("Price Per Share.....");
        txtPricePerShare1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPricePerShare1ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel34.setText("Total Amount :");

        txtTotalAmount1.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtTotalAmount1.setPlaceholder("Total Amount.....");
        txtTotalAmount1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalAmount1ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel35.setText("Result :");

        txtSellResult.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N

        jLabel36.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        jLabel36.setText("User Id :");

        txtIdSell.setFont(new java.awt.Font("Times New Roman", 1, 16)); // NOI18N
        txtIdSell.setPlaceholder("Enter User ID.....");

        jButton3.setBackground(new java.awt.Color(102, 102, 255));
        jButton3.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton3.setText("SELL");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tab5Layout = new javax.swing.GroupLayout(tab5);
        tab5.setLayout(tab5Layout);
        tab5Layout.setHorizontalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab5Layout.createSequentialGroup()
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(jLabel36)
                            .addComponent(jLabel33))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtStockSymbol1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtIdSell, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPricePerShare1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(82, 82, 82)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31)
                            .addComponent(jLabel34))
                        .addGap(26, 26, 26)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(companyNameField1, javax.swing.GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
                            .addComponent(txtNumberOfShares1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTotalAmount1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(tab5Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addGap(18, 18, 18)
                        .addComponent(txtSellResult)))
                .addGap(61, 61, 61))
            .addGroup(tab5Layout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        tab5Layout.setVerticalGroup(
            tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab5Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31)
                            .addComponent(txtNumberOfShares1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tab5Layout.createSequentialGroup()
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel30)
                            .addComponent(txtStockSymbol1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel36)
                            .addComponent(txtIdSell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(companyNameField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel33)
                            .addComponent(txtPricePerShare1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalAmount1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addGap(29, 29, 29)
                        .addComponent(jButton3)
                        .addGap(33, 33, 33)))
                .addGroup(tab5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSellResult, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addGap(27, 27, 27))
        );

        javax.swing.GroupLayout tab3Layout = new javax.swing.GroupLayout(tab3);
        tab3.setLayout(tab3Layout);
        tab3Layout.setHorizontalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tab3Layout.setVerticalGroup(
            tab3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tab5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabbedPane.addTab("   Sell Stocks     ", tab3);

        tab4.setBackground(new java.awt.Color(255, 248, 255));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel17.setText("Total Value");

        txtTotalValue.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtTotalValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalValueActionPerformed(evt);
            }
        });

        txtTotalInvestments.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel18.setText("Total Investments");

        txtGainsLosses.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel19.setText("Gains / Losses");

        jButton4.setBackground(new java.awt.Color(102, 102, 255));
        jButton4.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton4.setText("REFRESH PORTFOLIO");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(96, 119, 170));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Portfolio Details");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(362, 362, 362))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton5.setBackground(new java.awt.Color(102, 102, 255));
        jButton5.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton5.setText("DETAILS ");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        jLabel23.setText("User ID :");

        TBL.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "USER ID", "STOCK SYMBOL", "QUANTITY", "TOTAL AMOUNT", "PURCHASE PRICE", "TRANSCATION TYPE"
            }
        ));
        jScrollPane4.setViewportView(TBL);

        jPanel5.setBackground(new java.awt.Color(0, 102, 204));

        jLabel24.setBackground(new java.awt.Color(0, 102, 204));
        jLabel24.setFont(new java.awt.Font("Arial Black", 1, 15)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Search Current Market Price ");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel24)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel24)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel25.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel25.setText("Stock Symbol :");

        jButton6.setBackground(new java.awt.Color(102, 102, 255));
        jButton6.setFont(new java.awt.Font("Cambria", 1, 20)); // NOI18N
        jButton6.setText("SEARCH");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel26.setText("Current Price :");

        txtCurrentPrice.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        txtSymbol.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        txtSymbol.setPlaceholder("Enter stock Symbol.....");

        txtid.setFont(new java.awt.Font("Times New Roman", 1, 15)); // NOI18N
        txtid.setPlaceholder("Enter User ID.....");
        txtid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidActionPerformed(evt);
            }
        });

        jPanel12.setBackground(new java.awt.Color(96, 119, 170));
        jPanel12.setPreferredSize(new java.awt.Dimension(5, 0));

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 66, Short.MAX_VALUE)
        );

        jPanel13.setBackground(new java.awt.Color(96, 119, 170));
        jPanel13.setPreferredSize(new java.awt.Dimension(5, 263));

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 263, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tab4Layout = new javax.swing.GroupLayout(tab4);
        tab4.setLayout(tab4Layout);
        tab4Layout.setHorizontalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tab4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel23)
                .addGap(18, 18, 18)
                .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTotalInvestments, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtGainsLosses, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(30, 30, 30))
            .addGroup(tab4Layout.createSequentialGroup()
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(228, 228, 228)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(tab4Layout.createSequentialGroup()
                                        .addComponent(jLabel26)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCurrentPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(tab4Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(37, 37, 37))))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(jButton6))))
        );
        tab4Layout.setVerticalGroup(
            tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tab4Layout.createSequentialGroup()
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(txtGainsLosses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19)
                            .addComponent(txtTotalInvestments, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18)
                            .addComponent(txtTotalValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tab4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23))
                        .addGap(18, 18, 18)))
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSymbol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addComponent(jButton6)
                        .addGap(28, 28, 28)
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(txtCurrentPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(tab4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(tab4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(tab4Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31))
                            .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        tabbedPane.addTab("   Portfolio     ", tab4);

        jPanel3.add(tabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 1120, 380));

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        txtID.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 200, 200)));
        txtID.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(96, 135, 170));

        jLabel27.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Your User ID :");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel27)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtUsername.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(200, 200, 200)));
        txtUsername.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(96, 135, 170));

        jLabel28.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Your Username :");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBackground(new java.awt.Color(255, 215, 0));

        jLabel29.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel29.setText(" your User Id will help you in Buyying & Selling Stock!");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1270, 60));

        jPanel6.setBackground(new java.awt.Color(0, 51, 102));

        jCTextField2.setBackground(new java.awt.Color(200, 200, 200));
        jCTextField2.setText(" STATUS :");
        jCTextField2.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        jCTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCTextField2ActionPerformed(evt);
            }
        });

        txtStatus.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txtStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStatusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131)
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 634, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        jPanel3.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 1270, 70));

        jMenuBar2.setBackground(new java.awt.Color(0, 102, 204));
        jMenuBar2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N

        jMenu4.setText("File  ");
        jMenu4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem1.setText("New Account");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        jMenuItem2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem2.setText("User Login");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem3.setText("Logout");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem5.setText("Exit");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem5);

        jMenuBar2.add(jMenu4);

        jMenu1.setText("Trade  ");
        jMenu1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jMenuItem7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem7.setText("View Market Data");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem6.setText("Buy Stocks");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem8.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem8.setText("Sell Stocks");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem9.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem9.setText("View Portfolio");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem9);

        jMenuBar2.add(jMenu1);

        jMenu5.setText("Help  ");
        jMenu5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        jMenuItem4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem4.setText("User Guide");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);

        jMenuItem10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem10.setText("About");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuItem11.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jMenuItem11.setText("Contact Support");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem11);

        jMenuBar2.add(jMenu5);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setSize(new java.awt.Dimension(1270, 653));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Open the New Account page
        NewUserRegistration newAccount = new NewUserRegistration();
        newAccount.setVisible(true);
        frame.dispose();  // Optionally close the current window
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Open the Login page
        UserLogin login = new UserLogin();
        login.setVisible(true);
        frame.dispose(); 
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Show a message to the user
        JOptionPane.showMessageDialog(frame, "Logged out successfully.");
        // Redirect to the login page
        UserLogin login = new UserLogin();
        login.setVisible(true);
        frame.dispose();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnRefreshDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshDataActionPerformed
        // TODO add your handling code here:
        refreshData();
    }//GEN-LAST:event_btnRefreshDataActionPerformed

    private void tab1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tab1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tab1MouseEntered

    private void tabbedPaneMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabbedPaneMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tabbedPaneMouseEntered

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void btnFetchDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFetchDetailsActionPerformed
        // TODO add your handling code here:
        fetchAndDisplayDetails();
    }//GEN-LAST:event_btnFetchDetailsActionPerformed

    private void txtStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusActionPerformed

    private void txtNumberOfSharesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberOfSharesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfSharesActionPerformed

    private void companyNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_companyNameFieldActionPerformed

    private void txtPricePerShareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPricePerShareActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPricePerShareActionPerformed

    private void txtTotalAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalAmountActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    // Retrieve the text from the text field
    String text = txtIdBuy.getText().trim();
    
    // Check if the input is empty
    if (text.isEmpty()) {
        // Handle empty input case
        JOptionPane.showMessageDialog(null, "ID field cannot be empty.");
        return;
    }
    
    try {
        // Convert String to int
        int id = Integer.parseInt(text);
        
        // Handle the buy transaction
        handleBuyTransaction("Buy", id);
    } catch (NumberFormatException e) {
        // Handle invalid number format
        JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a valid number.");
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    // Retrieve the text from the text field
    String text = txtIdSell.getText().trim();
    
    // Check if the input is empty
    if (text.isEmpty()) {
        // Handle empty input case
        JOptionPane.showMessageDialog(null, "ID field cannot be empty.");
        return;
    }
    
    try {
        // Convert String to int
        int id = Integer.parseInt(text);
        
        // Handle the buy transaction
        handleSellTransaction("Sell", id);
    } catch (NumberFormatException e) {
        // Handle invalid number format
        JOptionPane.showMessageDialog(null, "Invalid ID format. Please enter a valid number.");
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jCTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCTextField2ActionPerformed

    private void txtCompanyNameMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCompanyNameMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompanyNameMouseEntered

    private void txtCompanyNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCompanyNameMouseClicked
        // TODO add your handling code here:
             txtStatus.setText("Processing your request to fetch details for company");
    }//GEN-LAST:event_txtCompanyNameMouseClicked

    private void txtStockSymbolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStockSymbolMouseClicked
        // TODO add your handling code here:
        txtStatus.setText("Processing purchase details. Continue entering symbol and Shares.");
    }//GEN-LAST:event_txtStockSymbolMouseClicked

    private void txtNumberOfSharesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNumberOfSharesMouseClicked
        // TODO add your handling code here:
        txtStatus.setText("Processing purchase details. Continue entering symbol and Shares.");
    }//GEN-LAST:event_txtNumberOfSharesMouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
              // Clear the existing rows in the table model
        model1.setRowCount(0);
        JOptionPane.showMessageDialog(this, "Portfolio Refreshed!");
        setPortfolioDataToTbl();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void txtTotalValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalValueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalValueActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
         try {
        int userId = Integer.parseInt(txtid.getText());

        // Check if the user ID exists
        if (isUserIDExists(userId)) {
            // Update the portfolio summary
            updatePortfolioSummary(userId, txtTotalValue, txtTotalInvestments, txtGainsLosses);
        } else {
            // User ID does not exist
            JOptionPane.showMessageDialog(null, "User ID does not exist.");
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null, "Please enter a valid user ID.");
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error fetching data from the database.");
        ex.printStackTrace();
    }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        searchCurrentPrice();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtStockSymbol1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtStockSymbol1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockSymbol1MouseClicked

    private void txtNumberOfShares1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNumberOfShares1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfShares1MouseClicked

    private void txtNumberOfShares1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberOfShares1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberOfShares1ActionPerformed

    private void companyNameField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_companyNameField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_companyNameField1ActionPerformed

    private void txtPricePerShare1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPricePerShare1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPricePerShare1ActionPerformed

    private void txtTotalAmount1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalAmount1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalAmount1ActionPerformed

    private void txtidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
       // Exit the application
        System.exit(0);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(0);   // Switch to "Market Data" tab
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(1); // Switch to "Buy Stocks" tab
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(2); // Switch to "Sell Stocks" tab
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        // TODO add your handling code here:
        tabbedPane.setSelectedIndex(3); // Switch to "Portfolio" tab
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
         // Display a simple dialog with user guide instructions
                JOptionPane.showMessageDialog(frame, 
                    "User Guide:\n\n" +
                    "1. Create a new account or log in.\n" +
                    "2. Use the 'Trade' menu to buy or sell stocks.\n" +
                    "3. View market Data under 'Trade' menu.\n" +        
                    "4. View your portfolio under 'Trade' menu.\n" +
                    "5. Contact support if you need help.",
                    "User Guide", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // Display a dialog with information about the platform
                JOptionPane.showMessageDialog(frame, 
                    "Stock Trading Platform\n" +
                    "Version 1.0\n" +
                    "Developed by KHADIJA NADEEM", 
                    "About", JOptionPane.INFORMATION_MESSAGE); 
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
     // Display a dialog with contact information
           JOptionPane.showMessageDialog(frame, 
                "Contact Support:\n\n" +
                "Email: support@stocktradingplatform.com\n" +
                "Phone: +92-300-1234567\n" +
                "For more help, visit our support page at www.stocktradingplatform.com/support", 
                "Contact Support", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void txtStockSymbol1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockSymbol1FocusLost
        // TODO add your handling code here:
        if (!txtStockSymbol1.getText().trim().isEmpty() && !txtNumberOfShares1.getText().trim().isEmpty()) {
            fetchAndDisplayStockInfoSell();
        }
    }//GEN-LAST:event_txtStockSymbol1FocusLost

    private void txtNumberOfShares1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfShares1FocusLost
        // TODO add your handling code here:
        if (!txtStockSymbol1.getText().trim().isEmpty() && !txtNumberOfShares1.getText().trim().isEmpty()) {
            fetchAndDisplayStockInfoSell();
        }
    }//GEN-LAST:event_txtNumberOfShares1FocusLost

    private void txtStockSymbolFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStockSymbolFocusLost
        // TODO add your handling code here:
          if (!txtStockSymbol.getText().trim().isEmpty() && !txtNumberOfShares.getText().trim().isEmpty()) {
            fetchAndDisplayStockInfoBuy();
        }
    }//GEN-LAST:event_txtStockSymbolFocusLost

    private void txtNumberOfSharesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberOfSharesFocusLost
        // TODO add your handling code here:
          if (!txtStockSymbol.getText().trim().isEmpty() && !txtNumberOfShares.getText().trim().isEmpty()) {
            fetchAndDisplayStockInfoBuy();
        }
    }//GEN-LAST:event_txtNumberOfSharesFocusLost

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(StockTradingPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(StockTradingPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(StockTradingPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(StockTradingPlatform.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                 new StockTradingPlatform().setVisible(true); // Ensure this is called
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TBL;
    private javax.swing.JButton btnFetchDetails;
    private javax.swing.JButton btnRefreshData;
    private app.bolivia.swing.JCTextField companyNameField;
    private app.bolivia.swing.JCTextField companyNameField1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private app.bolivia.swing.JCTextField jCTextField2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable marketDataTbl;
    private javax.swing.JPanel tab1;
    private javax.swing.JPanel tab2;
    private javax.swing.JPanel tab3;
    private javax.swing.JPanel tab4;
    private javax.swing.JPanel tab5;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTextField txtBuyResult;
    private app.bolivia.swing.JCTextField txtCompanyName;
    private javax.swing.JTextField txtCurrentPrice;
    private javax.swing.JTextArea txtDetailArea;
    private javax.swing.JTextField txtGainsLosses;
    private app.bolivia.swing.JCTextField txtID;
    private app.bolivia.swing.JCTextField txtIdBuy;
    private app.bolivia.swing.JCTextField txtIdSell;
    private app.bolivia.swing.JCTextField txtNumberOfShares;
    private app.bolivia.swing.JCTextField txtNumberOfShares1;
    private app.bolivia.swing.JCTextField txtPricePerShare;
    private app.bolivia.swing.JCTextField txtPricePerShare1;
    private javax.swing.JTextField txtSellResult;
    private app.bolivia.swing.JCTextField txtStatus;
    private app.bolivia.swing.JCTextField txtStockSymbol;
    private app.bolivia.swing.JCTextField txtStockSymbol1;
    private app.bolivia.swing.JCTextField txtSymbol;
    private app.bolivia.swing.JCTextField txtTotalAmount;
    private app.bolivia.swing.JCTextField txtTotalAmount1;
    private javax.swing.JTextField txtTotalInvestments;
    private javax.swing.JTextField txtTotalValue;
    private app.bolivia.swing.JCTextField txtUsername;
    private app.bolivia.swing.JCTextField txtid;
    // End of variables declaration//GEN-END:variables
}
