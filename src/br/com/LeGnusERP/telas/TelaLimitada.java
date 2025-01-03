/*
 * The MIT License
 *
 * Copyright 2022 Ad3ln0r.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.LeGnusERP.telas;

import br.com.LeGnusERP.dal.ModuloConexao;
import java.awt.Toolkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Ad3ln0r
 */

public class TelaLimitada extends javax.swing.JFrame {

    /**
     * Creates new form TelaLimitada
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaLimitada() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();        
    }
    
    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }


    public void instanciarBtn() {
        try {
            String sqo = "select cargo_usuario from tbusuarios where usuario=?";
            pst = conexao.prepareStatement(sqo);
            pst.setString(1, lblNome.getText());
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));

            if (tbAuxilio.getModel().getValueAt(0, 0).toString().equals("Vendedor") == true) {
                
                btnPDV.setEnabled(true);
                btnCadOS.setEnabled(false);
                btnCadServiço.setEnabled(false);
                btnAgendamentos.setEnabled(false);
                btnClientes.setEnabled(true);
                
            } else if (tbAuxilio.getModel().getValueAt(0, 0).toString().equals("Profissional/Tec") == true) {
                btnPDV.setEnabled(false);
                btnCadOS.setEnabled(true);
                btnCadServiço.setEnabled(true);
                btnAgendamentos.setEnabled(true);
                btnClientes.setEnabled(true);
                
            } else if (tbAuxilio.getModel().getValueAt(0, 0).toString().equals("Profissional/Tec e Vendedor") == true) {
                btnPDV.setEnabled(true);
                btnCadOS.setEnabled(true);
                btnCadServiço.setEnabled(true);   
                btnAgendamentos.setEnabled(true);
                btnClientes.setEnabled(true);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void PDV() {
        PontoDeVendas pdv = new PontoDeVendas();
        pdv.setVisible(true);
        PontoDeVendas.lblUsuarioPDV.setText(lblNome.getText());
        this.dispose();
    }
    
    public void Servico(){
        CadServico Servico = new CadServico();
        Servico.setVisible(true);
        Servico.lblUsuario.setText(lblNome.getText());
        this.dispose();
    }
    
    public void gerenciadorServico(){
        TelaServico Servico = new TelaServico();
        Servico.setVisible(true);
        Servico.lblUsuario.setText(lblNome.getText());
        this.dispose();
    }
    
    public void OS(){
        CadOS os = new CadOS();
        os.setVisible(true);
        os.lblUsuario.setText(lblNome.getText());
        this.dispose();
    }
    
    public void Cliente(){
        CadClientes cli = new CadClientes();
        cli.setVisible(true);
        cli.lblUsuario.setText(lblNome.getText());
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNome = new javax.swing.JLabel();
        scAuxilio = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton2 = new javax.swing.JToggleButton();
        btnPDV = new br.com.LeGnusERP.Swing.botaoArredondado();
        btnAgendamentos = new br.com.LeGnusERP.Swing.botaoArredondado();
        btnCadOS = new br.com.LeGnusERP.Swing.botaoArredondado();
        btnCadServiço = new br.com.LeGnusERP.Swing.botaoArredondado();
        btnClientes = new br.com.LeGnusERP.Swing.botaoArredondado();

        lblNome.setToolTipText("");

        tbAuxilio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scAuxilio.setViewportView(tbAuxilio);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LeGnu`s_EPR- Limitada");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 321x230.png"))); // NOI18N
        jToggleButton2.setBorderPainted(false);
        jToggleButton2.setContentAreaFilled(false);

        btnPDV.setText("Ponto de Vendas");
        btnPDV.setEnabled(false);
        btnPDV.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnPDV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDVActionPerformed(evt);
            }
        });

        btnAgendamentos.setText("Agendamentos");
        btnAgendamentos.setEnabled(false);
        btnAgendamentos.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAgendamentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgendamentosActionPerformed(evt);
            }
        });

        btnCadOS.setText("Cadastro de OS");
        btnCadOS.setEnabled(false);
        btnCadOS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCadOS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadOSActionPerformed(evt);
            }
        });

        btnCadServiço.setText("Cadastro de Serviço");
        btnCadServiço.setEnabled(false);
        btnCadServiço.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCadServiço.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCadServiçoActionPerformed(evt);
            }
        });

        btnClientes.setText("Cadastro Cliente");
        btnClientes.setEnabled(false);
        btnClientes.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnPDV, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCadServiço, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCadOS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgendamentos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(btnPDV, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCadServiço, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgendamentos, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCadOS, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        instanciarBtn();
    }//GEN-LAST:event_formWindowOpened

    private void btnPDVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDVActionPerformed
        // TODO add your handling code here:
        PDV();
    }//GEN-LAST:event_btnPDVActionPerformed

    private void btnAgendamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgendamentosActionPerformed
        // TODO add your handling code here:
        
        gerenciadorServico();
    }//GEN-LAST:event_btnAgendamentosActionPerformed

    private void btnCadOSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadOSActionPerformed
        // TODO add your handling code here:
        OS();
    }//GEN-LAST:event_btnCadOSActionPerformed

    private void btnCadServiçoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCadServiçoActionPerformed
        // TODO add your handling code here:
        Servico();
    }//GEN-LAST:event_btnCadServiçoActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // TODO add your handling code here:
        Cliente();
    }//GEN-LAST:event_btnClientesActionPerformed

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
            java.util.logging.Logger.getLogger(TelaLimitada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLimitada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLimitada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLimitada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLimitada().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static br.com.LeGnusERP.Swing.botaoArredondado btnAgendamentos;
    public static br.com.LeGnusERP.Swing.botaoArredondado btnCadOS;
    public static br.com.LeGnusERP.Swing.botaoArredondado btnCadServiço;
    public static br.com.LeGnusERP.Swing.botaoArredondado btnClientes;
    public static br.com.LeGnusERP.Swing.botaoArredondado btnPDV;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton2;
    public static javax.swing.JLabel lblNome;
    private javax.swing.JScrollPane scAuxilio;
    private javax.swing.JTable tbAuxilio;
    // End of variables declaration//GEN-END:variables
}
