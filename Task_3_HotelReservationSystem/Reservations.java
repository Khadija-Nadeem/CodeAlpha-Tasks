/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Task_3_HotelReservationSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


/**
 *
 * @author dell
 */
public class Reservations extends javax.swing.JFrame {
    private String offerName;
    private String roomType;
    private int roomNumber;

    /**
     * Creates new form Reservations
     */
    public Reservations() {
        initComponents();
        
    //invisible labels & text fields
    hideOfferFields();
    }

     // Constructor for when offer details are available
    public Reservations(String offerName, int roomNumber, String roomType) {
    this.offerName = offerName;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    initComponents();
    setOfferName(offerName); // Set offer name and show fields
    setRoomDetails(roomNumber, roomType);
}
    
     // Constructor for when only room details are available
   public Reservations(int roomNumber, String roomType) {
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    initComponents();
    setRoomDetails(roomNumber, roomType);
    hideOfferFields(); // Hide offer fields by default
}
   
     private void hideOfferFields() {
    lblOfferApplied1.setVisible(false);
    lblDiscountPrice1.setVisible(false);
    txtOfferApplied.setVisible(false);
    txtDiscountPrice.setVisible(false);
}

    private void setRoomDetails(int roomNumber, String roomType) {
    txtRoomNumber.setText(String.valueOf(roomNumber)); // Display room number
    txtRoomType.setText(roomType); // Display room type
}
    
    private void setOfferName(String offerName) {
    if (offerName != null && !offerName.isEmpty()) {
        txtOfferApplied.setText(offerName); // Set the offer on the label
        txtOfferApplied.setVisible(true); // Ensure label is visible
        txtDiscountPrice.setVisible(true); // Show the discount price if applicable
        lblOfferApplied1.setVisible(true); // Show "Offer Applied:" label
        lblDiscountPrice1.setVisible(true); // Show "Discounted Price:" label
    } else {
        txtOfferApplied.setVisible(false); // Hide if no offer name
        txtDiscountPrice.setVisible(false); // Hide discount price if no offer
        lblOfferApplied1.setVisible(false); // Hide "Offer Applied:" label
        lblDiscountPrice1.setVisible(false); // Hide "Discounted Price:" label
    }
}
    
    private void calculateDiscountedPrice(double originalPrice) {
    String offerName = txtOfferApplied.getText().trim();
    double discountedPrice = originalPrice;

    // Apply discount based on the offer name
    if (offerName.equalsIgnoreCase("Summer Sale Extravaganza")) {
        discountedPrice = originalPrice * 0.75; // 25% discount
    } else if (offerName.equalsIgnoreCase("Winter Wonderland Package")) {
        discountedPrice = (originalPrice / 4) * 3; // Price for 3 nights out of 4
    } else if (offerName.equalsIgnoreCase("Weekend Getaway Special")) {
        discountedPrice = originalPrice * 0.90; // 10% discount
        lblDiscountPrice1.setText("10% Discount Applied:"); // Change label text
    } else if (offerName.equalsIgnoreCase("Early Bird Discount")) {
        discountedPrice = originalPrice * 0.80; // 20% discount
    } else {
        // If the offer name is not recognized, handle it accordingly
        JOptionPane.showMessageDialog(this, "Unknown offer name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Set and display discounted price in PKR
    txtDiscountPrice.setText(String.format("PKR %.2f", discountedPrice));
    lblDiscountPrice1.setVisible(true); // Show "Discounted Price:" label
    txtDiscountPrice.setVisible(true); // Show discounted price text field
}

    private long getNumberOfNights() {
    LocalDate checkInDate = txtCheckInDate.getDatoFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate checkOutDate = txtCheckOutDate.getDatoFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
}
   private double getPricePerNight(String roomType) {
    double pricePerNight = 0.0;
    String query = "SELECT price_per_night FROM rooms WHERE room_type = ? AND availability_status = 'Available' LIMIT 1";

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Hotel_Reservation_System", "root", "programmer._.2004");
         PreparedStatement pst = con.prepareStatement(query)) {

        // Set the parameter for the room type
        pst.setString(1, roomType);

        // Execute the query and process the result
        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                pricePerNight = rs.getDouble("price_per_night");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return pricePerNight;
}

    private void calculateOriginalPrice() {
    if (!validateReservationFields()) {
        return; // Ensure fields are valid before calculation
    }

    String roomType = txtRoomType.getText().trim();
    long numberOfNights = getNumberOfNights();
    double pricePerNight = getPricePerNight(roomType);
    double originalPrice = pricePerNight * numberOfNights;

    txtOriginalPrice.setText(String.format("PKR %.2f", originalPrice));

    // Hide the discount fields if no offer is applied
    if (txtOfferApplied.getText().trim().isEmpty()) {
        lblDiscountPrice1.setVisible(false);
        txtDiscountPrice.setVisible(false);
    } else {
        // Show discount fields if an offer is applied
        calculateDiscountedPrice(originalPrice);
    }
}

    // Validates the reservation fields 
    private boolean validateReservationFields() {
    if (txtCustomerName.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the customer name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    if (txtCheckInDate.getDatoFecha() == null) {
        JOptionPane.showMessageDialog(this, "Please select a check-in date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    if (txtCheckOutDate.getDatoFecha() == null) {
        JOptionPane.showMessageDialog(this, "Please select a check-out date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    if (txtRoomNumber.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a room number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    if (txtRoomType.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter a room type.", "Validation Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    return true;
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
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCheckInDate = new rojeru_san.componentes.RSDateChooser();
        txtCheckOutDate = new rojeru_san.componentes.RSDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblOfferApplied1 = new javax.swing.JLabel();
        lblDiscountPrice1 = new javax.swing.JLabel();
        txtCustomerName = new app.bolivia.swing.JCTextField();
        txtOriginalPrice = new app.bolivia.swing.JCTextField();
        txtDiscountPrice = new app.bolivia.swing.JCTextField();
        txtOfferApplied = new app.bolivia.swing.JCTextField();
        jLabel10 = new javax.swing.JLabel();
        txtRoomType = new app.bolivia.swing.JCTextField();
        txtRoomNumber = new app.bolivia.swing.JCTextField();
        jButton3 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();

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
        jLabel16.setText("MAKE RESERVATIONS");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));

        jLabel1.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        jLabel1.setText("Book your stay with us.");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 60, -1, -1));

        jPanel4.setBackground(new java.awt.Color(64, 64, 64));
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Serif", 1, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Task_3_HotelReservationSystem/icons8_Rewind_48px.png"))); // NOI18N
        jLabel4.setText("  Back");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 40));

        jPanel1.setBackground(new java.awt.Color(152, 103, 164));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("CHECK-IN DATE :");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("CUSTOMER NAME :");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, -1, -1));

        txtCheckInDate.setColorBackground(new java.awt.Color(102, 51, 153));
        txtCheckInDate.setColorButtonHover(new java.awt.Color(102, 51, 153));
        txtCheckInDate.setColorDiaActual(new java.awt.Color(152, 103, 164));
        txtCheckInDate.setColorForeground(new java.awt.Color(102, 51, 153));
        txtCheckInDate.setPlaceholder("Select check-in Date...");
        txtCheckInDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtCheckInDatePropertyChange(evt);
            }
        });
        jPanel1.add(txtCheckInDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 80, 390, -1));

        txtCheckOutDate.setColorBackground(new java.awt.Color(102, 51, 153));
        txtCheckOutDate.setColorButtonHover(new java.awt.Color(102, 51, 153));
        txtCheckOutDate.setColorDiaActual(new java.awt.Color(152, 103, 164));
        txtCheckOutDate.setColorForeground(new java.awt.Color(102, 51, 153));
        txtCheckOutDate.setPlaceholder("Select check-out Date...");
        txtCheckOutDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtCheckOutDatePropertyChange(evt);
            }
        });
        jPanel1.add(txtCheckOutDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 390, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("CHECK-OUT DATE :");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("ROOM TYPE :");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("ORIGINAL PRICE :");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, -1, -1));

        lblOfferApplied1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblOfferApplied1.setText("OFFER APPLIED :");
        jPanel1.add(lblOfferApplied1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 430, -1, -1));

        lblDiscountPrice1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblDiscountPrice1.setText("DISCOUNTED PRICE :");
        jPanel1.add(lblDiscountPrice1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, -1, -1));

        txtCustomerName.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtCustomerName.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtCustomerName.setPlaceholder("Enter your name...");
        txtCustomerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerNameActionPerformed(evt);
            }
        });
        jPanel1.add(txtCustomerName, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 10, 390, 40));

        txtOriginalPrice.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtOriginalPrice.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtOriginalPrice.setPlaceholder("Click calculate total button...");
        txtOriginalPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOriginalPriceActionPerformed(evt);
            }
        });
        jPanel1.add(txtOriginalPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, 390, 40));

        txtDiscountPrice.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtDiscountPrice.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtDiscountPrice.setPlaceholder("Click calculate total button...");
        txtDiscountPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiscountPriceActionPerformed(evt);
            }
        });
        jPanel1.add(txtDiscountPrice, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 490, 390, 40));

        txtOfferApplied.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtOfferApplied.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtOfferApplied.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOfferAppliedActionPerformed(evt);
            }
        });
        jPanel1.add(txtOfferApplied, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 420, 390, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("ROOM NUMBER :");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, -1));

        txtRoomType.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtRoomType.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtRoomType.setPlaceholder("Enter your room type...");
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
        jPanel1.add(txtRoomType, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 280, 390, 40));

        txtRoomNumber.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 51, 153)));
        txtRoomNumber.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        txtRoomNumber.setPlaceholder("Enter your room number...");
        txtRoomNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRoomNumberActionPerformed(evt);
            }
        });
        jPanel1.add(txtRoomNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 390, 40));

        jButton3.setBackground(new java.awt.Color(132, 34, 102));
        jButton3.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jButton3.setText("CALCULATE TOTAL");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 570, -1, -1));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Task_3_HotelReservationSystem/reservation (1).jpg"))); // NOI18N

        jButton1.setBackground(new java.awt.Color(132, 34, 102));
        jButton1.setFont(new java.awt.Font("Serif", 1, 24)); // NOI18N
        jButton1.setText("PROCEED TO PAYMENT");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(64, 64, 64));
        jPanel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel5MouseClicked(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Serif", 1, 17)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("want to refresh Price? Click here.");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(23, 23, 23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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

    private void txtOriginalPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOriginalPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOriginalPriceActionPerformed

    private void txtDiscountPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiscountPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDiscountPriceActionPerformed

    private void txtOfferAppliedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOfferAppliedActionPerformed
        // TODO add your handling code here:
        System.out.println("Offer Applied changed");
        double originalPrice = Double.parseDouble(txtOriginalPrice.getText().replace("PKR ", "").trim());
    calculateDiscountedPrice(originalPrice); // Recalculate discount price when offer is applied
    }//GEN-LAST:event_txtOfferAppliedActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // TODO add your handling code here:
        HotelHomePage home = new HotelHomePage();
        home.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     // Validate the reservation fields first
    if (validateReservationFields()) {
        // Collect data from the reservation form
        String customerName = txtCustomerName.getText();

        // Get the date values from JDateChooser
        java.util.Date checkInDateUtil = txtCheckInDate.getDatoFecha(); // Assuming txtCheckInDate is a JDateChooser
        java.util.Date checkOutDateUtil = txtCheckOutDate.getDatoFecha(); // Assuming txtCheckOutDate is a JDateChooser

        // Convert java.util.Date to java.sql.Date
        java.sql.Date checkInDate = new java.sql.Date(checkInDateUtil.getTime());
        java.sql.Date checkOutDate = new java.sql.Date(checkOutDateUtil.getTime());

        // Get the room type from text field
        String roomType = txtRoomType.getText().trim(); // Assuming txtRoomType is a JTextField

        // Get the room number from text field and convert it to String
        String roomNumberText = txtRoomNumber.getText().trim(); // Assuming txtRoomNumber is a JTextField

        // Ensure the original price and discounted price fields are not empty
        String originalPriceText = txtOriginalPrice.getText().replace("PKR ", "").trim();
        String discountedPriceText = txtDiscountPrice.getText().replace("PKR ", "").trim();

        double originalPrice = originalPriceText.isEmpty() ? 0.0 : Double.parseDouble(originalPriceText);
        double discountedPrice = discountedPriceText.isEmpty() ? 0.0 : Double.parseDouble(discountedPriceText);

        // Get the offer applied (if any)
        String offerApplied = txtOfferApplied.getText().trim();

        // Create a new Payment instance and pass the collected data
        Payment paymentPage = new Payment(customerName, checkInDate, checkOutDate, roomNumberText, roomType, originalPrice, offerApplied, discountedPrice);
        paymentPage.setVisible(true);

        // Optionally, close the Reservation page
        this.dispose();
    } else {
        // If validation fails, show an error message or handle it accordingly
        JOptionPane.showMessageDialog(this, "Please fill in all required fields correctly.", "Validation Error", JOptionPane.ERROR_MESSAGE);
    } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCheckInDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtCheckInDatePropertyChange
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtCheckInDatePropertyChange

    private void txtCheckOutDatePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtCheckOutDatePropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtCheckOutDatePropertyChange

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jLabel9MouseClicked

    private void jPanel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5MouseClicked

    private void txtRoomTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomTypeActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_txtRoomTypeActionPerformed

    private void txtRoomNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRoomNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRoomNumberActionPerformed

    private void txtRoomTypeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRoomTypeFocusLost
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtRoomTypeFocusLost

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if (validateReservationFields()) {
        calculateOriginalPrice(); // Calculate original price
        
        double originalPrice = Double.parseDouble(txtOriginalPrice.getText().replace("PKR ", "").trim());
        if (txtOfferApplied.isVisible() && !txtOfferApplied.getText().trim().isEmpty()) {
            calculateDiscountedPrice(originalPrice); // Calculate discounted price if an offer is applied
        }
    }
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reservations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reservations().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblDiscountPrice1;
    private javax.swing.JLabel lblOfferApplied1;
    private rojeru_san.componentes.RSDateChooser txtCheckInDate;
    private rojeru_san.componentes.RSDateChooser txtCheckOutDate;
    private app.bolivia.swing.JCTextField txtCustomerName;
    private app.bolivia.swing.JCTextField txtDiscountPrice;
    private app.bolivia.swing.JCTextField txtOfferApplied;
    private app.bolivia.swing.JCTextField txtOriginalPrice;
    private app.bolivia.swing.JCTextField txtRoomNumber;
    private app.bolivia.swing.JCTextField txtRoomType;
    // End of variables declaration//GEN-END:variables
}
