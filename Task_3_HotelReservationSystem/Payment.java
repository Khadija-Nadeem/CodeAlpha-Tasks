/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Task_3_HotelReservationSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

/**
 *
 * @author dell
 */
public class Payment extends javax.swing.JFrame {

    /**
     * Creates new form Payment
     */
    public Payment() {
        initComponents();
    }
    
public Payment(String customerName, java.sql.Date checkInDate, java.sql.Date checkOutDate, 
               String roomNumber, String roomType, double originalPrice, String offerApplied, double discountedPrice) {
    initComponents();
    this.customerName = customerName;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.originalPrice = originalPrice;
    this.offerApplied = offerApplied;
    this.discountedPrice = discountedPrice;

    // Display reservation details on the Payment page
    txtCustomerName.setText(customerName);
    txtCheckInDate.setText(checkInDate.toString());
    txtCheckOutDate.setText(checkOutDate.toString());
    txtRoomNumber.setText(roomNumber); 
    txtRoomType.setText(roomType);
    txtOriginalPrice.setText(String.format("PKR %.2f", originalPrice));
    txtOfferApplied.setText(offerApplied);
    txtDiscountPrice.setText(String.format("PKR %.2f", discountedPrice));
    
    // Update status label based on discounted price
    if (discountedPrice == 0.00) {
        lblDiscountStatus.setText("No discount applied");
    } else {
        lblDiscountStatus.setText("Discount applied!");
    }
}

    private String customerName;
    private java.sql.Date checkInDate;
    private java.sql.Date checkOutDate;
    private String roomNumber;
    private String roomType;
    private double originalPrice;
    private String offerApplied;
    private double discountedPrice;

    private boolean validatePaymentInformation() {
        // Check if payment method is selected
        if (cbPaymentMethod.getSelectedItem() == null || cbPaymentMethod.getSelectedItem().toString().equals("Select payment method...")) {
            JOptionPane.showMessageDialog(this, "Please select payment method.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
   private void insertReservationDataPending() {
    String sql = "INSERT INTO Reservations (customer_name, check_in_date, check_out_date, room_number, room_type, original_price, discount_applied, discounted_price, payment_method, payment_status, offer_name) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pending', ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel_Reservation_System", "root", "programmer._.2004");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtCustomerName.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtCheckInDate.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtCheckOutDate.getText()));
        pstmt.setString(4, txtRoomNumber.getText()); // Updated to use room number
        pstmt.setString(5, txtRoomType.getText()); // Assuming you have a text field for room type
        pstmt.setDouble(6, Double.parseDouble(txtOriginalPrice.getText().replace("PKR ", "")));
        pstmt.setBoolean(7, !txtOfferApplied.getText().isEmpty());
        pstmt.setDouble(8, Double.parseDouble(txtDiscountPrice.getText().replace("PKR ", "")));
        pstmt.setString(9, cbPaymentMethod.getSelectedItem().toString());
        pstmt.setString(10, txtOfferApplied.getText());

        pstmt.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving reservation details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void insertReservationDataComplete() {
    String sql = "INSERT INTO Reservations (customer_name, check_in_date, check_out_date, room_number, room_type, original_price, discount_applied, discounted_price, payment_method, payment_status, offer_name) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Completed', ?)";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel_Reservation_System", "root", "programmer._.2004");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtCustomerName.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtCheckInDate.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtCheckOutDate.getText()));
        pstmt.setString(4, txtRoomNumber.getText()); // Updated to use room number
        pstmt.setString(5, txtRoomType.getText()); // Assuming you have a text field for room type
        pstmt.setDouble(6, Double.parseDouble(txtOriginalPrice.getText().replace("PKR ", "")));
        pstmt.setBoolean(7, !txtOfferApplied.getText().isEmpty());
        pstmt.setDouble(8, Double.parseDouble(txtDiscountPrice.getText().replace("PKR ", "")));
        pstmt.setString(9, cbPaymentMethod.getSelectedItem().toString());
        pstmt.setString(10, txtOfferApplied.getText());

        pstmt.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving reservation details. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void updateReservationStatusToCompleted() {
    String sql = "UPDATE Reservations SET payment_status = 'Completed' WHERE customer_name = ? AND check_in_date = ? AND check_out_date = ? AND room_number = ? AND payment_status = 'Pending'";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel_Reservation_System", "root", "programmer._.2004");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtCustomerName.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtCheckInDate.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtCheckOutDate.getText()));
        pstmt.setString(4, txtRoomNumber.getText()); // Updated to use room number

        int rowsUpdated = pstmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(this, "Payment completed, reservation status updated.");
        } else {
            JOptionPane.showMessageDialog(this, "No matching reservation found or payment is already completed.");
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error updating reservation status. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private boolean reservationExists() {
    String sql = "SELECT COUNT(*) FROM Reservations WHERE customer_name = ? AND check_in_date = ? AND check_out_date = ? AND room_number = ? AND payment_status = 'Pending'";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel_Reservation_System", "root", "programmer._.2004");
         PreparedStatement pstmt = con.prepareStatement(sql)) {

        pstmt.setString(1, txtCustomerName.getText());
        pstmt.setDate(2, java.sql.Date.valueOf(txtCheckInDate.getText()));
        pstmt.setDate(3, java.sql.Date.valueOf(txtCheckOutDate.getText()));
        pstmt.setString(4, txtRoomNumber.getText()); // Updated to use room number

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error checking reservation status.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    return false;
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        cbPaymentMethod = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtDiscountPrice = new app.bolivia.swing.JCTextField();
        txtCustomerName = new app.bolivia.swing.JCTextField();
        txtOriginalPrice = new app.bolivia.swing.JCTextField();
        txtOfferApplied = new app.bolivia.swing.JCTextField();
        txtRoomNumber = new app.bolivia.swing.JCTextField();
        txtCheckOutDate = new app.bolivia.swing.JCTextField();
        txtCheckInDate = new app.bolivia.swing.JCTextField();
        lblDiscountStatus = new javax.swing.JLabel();
        txtRoomType = new app.bolivia.swing.JCTextField();
        jLabel5 = new javax.swing.JLabel();
        btnConfirmReservation = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnPayNConfirm = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(102, 51, 153));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 27)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(242, 242, 242));
        jLabel17.setText("X");
        jLabel17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel17MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 0, 20, 30));

        jLabel16.setFont(new java.awt.Font("Bodoni MT Black", 1, 35)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Payment Information");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel1.setText("Securely complete your reservation.");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 60, -1, -1));

        jPanel4.setBackground(new java.awt.Color(64, 64, 64));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Serif", 1, 17)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Task_3_HotelReservationSystem/icons8_Rewind_48px.png"))); // NOI18N
        jLabel3.setText("  Back");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 40));

        jPanel1.setBackground(new java.awt.Color(152, 103, 164));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cbPaymentMethod.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        cbPaymentMethod.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select payment method...", "Credit Card", "Cash", "Payoneer", "Skrill", "JazzCash", "Easypaisa", "Bank Transfer" }));
        cbPaymentMethod.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        jPanel1.add(cbPaymentMethod, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 280, 40));

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("RESERVATION SUMMARY : ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("PAYMENT METHOD :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, -1, -1));

        txtDiscountPrice.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtDiscountPrice.setPlaceholder("discount price (if offer applied)..");
        jPanel1.add(txtDiscountPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 490, 340, 40));

        txtCustomerName.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtCustomerName.setPlaceholder("customer name..");
        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });
        jPanel1.add(txtCustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 340, 40));

        txtOriginalPrice.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtOriginalPrice.setPlaceholder("original price..");
        jPanel1.add(txtOriginalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 390, 340, 40));

        txtOfferApplied.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtOfferApplied.setPlaceholder("offer applied (if any)");
        jPanel1.add(txtOfferApplied, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 440, 340, 40));

        txtRoomNumber.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtRoomNumber.setPlaceholder("room number..");
        txtRoomNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomNumberActionPerformed(evt);
            }
        });
        jPanel1.add(txtRoomNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 340, 40));

        txtCheckOutDate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtCheckOutDate.setPlaceholder("check-out date..");
        txtCheckOutDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckOutDateActionPerformed(evt);
            }
        });
        jPanel1.add(txtCheckOutDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 240, 340, 40));

        txtCheckInDate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtCheckInDate.setPlaceholder("check-in date..");
        txtCheckInDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCheckInDateActionPerformed(evt);
            }
        });
        jPanel1.add(txtCheckInDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 340, 40));

        lblDiscountStatus.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        lblDiscountStatus.setForeground(new java.awt.Color(204, 0, 0));
        jPanel1.add(lblDiscountStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 550, 340, 30));

        txtRoomType.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtRoomType.setPlaceholder("room type..");
        txtRoomType.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRoomTypeFocusLost(evt);
            }
        });
        txtRoomType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomTypeActionPerformed(evt);
            }
        });
        jPanel1.add(txtRoomType, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 340, 40));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Task_3_HotelReservationSystem/payment (1).jpg"))); // NOI18N

        btnConfirmReservation.setBackground(new java.awt.Color(132, 34, 102));
        btnConfirmReservation.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnConfirmReservation.setText("CONFIRM RESERVATION");
        btnConfirmReservation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmReservationActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(64, 64, 64));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Serif", 1, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("want to go back to reservations?");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        btnPayNConfirm.setBackground(new java.awt.Color(132, 34, 102));
        btnPayNConfirm.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        btnPayNConfirm.setText("PAY & CONFIRM");
        btnPayNConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayNConfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConfirmReservation)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(btnPayNConfirm)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnConfirmReservation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPayNConfirm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );

        setSize(new java.awt.Dimension(1280, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel17MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jLabel17MouseClicked

    private void txtCustomerNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustomerNameActionPerformed

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        // TODO add your handling code here:
        HotelHomePage home = new HotelHomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void btnConfirmReservationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmReservationActionPerformed
        if (validatePaymentInformation()) {
            insertReservationDataPending();
            JOptionPane.showMessageDialog(this, "Reservation confirmed with Pending status.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }//GEN-LAST:event_btnConfirmReservationActionPerformed

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        Reservations room = new Reservations();
        room.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void txtRoomNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNumberActionPerformed

    private void txtCheckOutDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckOutDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCheckOutDateActionPerformed

    private void txtCheckInDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCheckInDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCheckInDateActionPerformed

    private void btnPayNConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayNConfirmActionPerformed
        if (validatePaymentInformation()) {
            if (reservationExists()) {
                updateReservationStatusToCompleted();
            } else {
                insertReservationDataComplete();
            }
            JOptionPane.showMessageDialog(this, "Reservation and Payment completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }//GEN-LAST:event_btnPayNConfirmActionPerformed

    private void txtRoomTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomTypeActionPerformed

    private void txtRoomTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRoomTypeFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomTypeFocusLost

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
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Payment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmReservation;
    private javax.swing.JButton btnPayNConfirm;
    private javax.swing.JComboBox<String> cbPaymentMethod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblDiscountStatus;
    private app.bolivia.swing.JCTextField txtCheckInDate;
    private app.bolivia.swing.JCTextField txtCheckOutDate;
    private app.bolivia.swing.JCTextField txtCustomerName;
    private app.bolivia.swing.JCTextField txtDiscountPrice;
    private app.bolivia.swing.JCTextField txtOfferApplied;
    private app.bolivia.swing.JCTextField txtOriginalPrice;
    private app.bolivia.swing.JCTextField txtRoomNumber;
    private app.bolivia.swing.JCTextField txtRoomType;
    // End of variables declaration//GEN-END:variables
}
