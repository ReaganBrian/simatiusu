
package loginandsignup;


public class SignUp extends javax.swing.JFrame {

 
    public SignUp() {
        initComponents();
    }

  
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnRegister = new javax.swing.JButton();
        btnLogin = new javax.swing.JButton();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel10 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sign Up");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(51, 153, 0));

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 204, 0));
        jLabel2.setText("SIMATI USU");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Sistem Peminjaman Ruang Kelas Fasilkom-TI USU");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(137, 137, 137)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel9))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(125, 125, 125)
                        .addComponent(jLabel2)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(129, 129, 129)
                .addComponent(jLabel1)
                .addGap(138, 138, 138)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addContainerGap(283, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 400, 620);

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel4.setBackground(new java.awt.Color(0, 102, 102));
        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 0));
        jLabel4.setText("REGISTER");

        jLabel5.setBackground(new java.awt.Color(102, 102, 102));
        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel5.setText("Nama");

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(102, 102, 102));

        jLabel6.setBackground(new java.awt.Color(102, 102, 102));
        jLabel6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel6.setText("NIDN / NIM");

        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel7.setBackground(new java.awt.Color(102, 102, 102));
        jLabel7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel7.setText("Email");

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jLabel8.setText("Sudah punya akun?");

        btnRegister.setBackground(new java.awt.Color(0, 0, 0));
        btnRegister.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("Register");

        btnLogin.setBackground(new java.awt.Color(0, 153, 0));
        btnLogin.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jPasswordField2.setBackground(new java.awt.Color(255, 255, 255));
        jPasswordField2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jPasswordField2.setForeground(new java.awt.Color(102, 102, 102));

        jLabel10.setBackground(new java.awt.Color(102, 102, 102));
        jLabel10.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel10.setText("Password");

        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel4))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(jTextField1)
                            .addComponent(jLabel6)
                            .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10)
                            .addComponent(jPasswordField2)
                            .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(24, 34, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegister, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLogin)
                    .addComponent(jLabel8))
                .addGap(32, 32, 32))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(400, 0, 410, 620);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        Login LoginFrame = new Login();
        LoginFrame.setVisible(true);
        LoginFrame.pack();
        LoginFrame.setLocationRelativeTo(null); 
        this.dispose();
    }//GEN-LAST:event_btnLoginActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
