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
import static br.com.LeGnusERP.dal.model.pegarLogo;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Ad3ln0r
 */
public class TelaInventario extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String tipo = "0";

    public TelaInventario() {
        initComponents();
        conexao = ModuloConexao.conector();
       setIcon();        
    }
    
    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }


    public void instanciarTabelaCompra() {
        try {
            tipo = "Compra";
            String sql = "select idproduto as ID, produto as Produto, valor_compra as Valor_de_Compra, quantidade as Quantidade, referencial_compra as Valor_Total from tbprodutos where estoque='Com controle de estoque.'";

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbProdutos.setModel(DbUtils.resultSetToTableModel(rs));

            double preco, x;

            String sqr = "select referencial_compra from tbprodutos where estoque='Com controle de estoque.'";

            preco = 0;

            pst = conexao.prepareStatement(sqr);
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
            for (int i = 0; i < tbAuxilio.getRowCount(); i++) {
                x = Double.parseDouble(tbAuxilio.getModel().getValueAt(i, 0).toString().replace(".", "")) / 100;
                preco = preco + x;
                lblValor.setText(new DecimalFormat("#,##0.00").format(Double.parseDouble(String.valueOf(preco))).replace(",", "."));
                lblNome.setText("Total Compras: R$");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void instanciarTabelaVenda() {
        String sql = "select idproduto as ID, produto as Produto, valor_venda as Valor_de_Venda, quantidade as Quantidade, referencial_venda as Valor_Total from tbprodutos where estoque='Com controle de estoque.'";
        try {
            tipo = "Venda";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbProdutos.setModel(DbUtils.resultSetToTableModel(rs));

            double preco, x;

            String sqr = "select referencial_venda from tbprodutos where estoque='Com controle de estoque.'";

            preco = 0;

            pst = conexao.prepareStatement(sqr);
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
            for (int i = 0; i < tbAuxilio.getRowCount(); i++) {
                x = Double.parseDouble(tbAuxilio.getModel().getValueAt(i, 0).toString().replace(".", "")) / 100;
                preco = preco + x;
                lblValor.setText(new DecimalFormat("#,##0.00").format(Double.parseDouble(String.valueOf(preco))).replace(",", "."));
                lblNome.setText("Total Vendas: R$");

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void instanciarTabelaCompra_X_Venda() {

        try {
            tipo = "CompraXVenda";
            int confirma = JOptionPane.showConfirmDialog(null, "Voce deseja calcular Compra_X_Venda ?", "Atençao", JOptionPane.YES_OPTION);

            if (confirma == JOptionPane.YES_OPTION) {
                tirar();
                criar();

                String squ = "select idproduto as ID, produto as Produto, quantidade as Quantidade,"
                        + " referencial_compra as Referencial_Compra, referencial_venda as Referencial_Venda, compra_x_venda as Lucratividade from tbprodutos where estoque='Com controle de estoque.'";

                pst = conexao.prepareStatement(squ);
                rs = pst.executeQuery();
                tbProdutos.setModel(DbUtils.resultSetToTableModel(rs));

                calculoLucro();

                String sqo = "select idproduto as ID, produto as Produto, quantidade as Quantidade,"
                        + " referencial_compra as Referencial_Compra, referencial_venda as Referencial_Venda, compra_x_venda as Lucratividade from tbprodutos where estoque='Com controle de estoque.'";

                pst = conexao.prepareStatement(sqo);
                rs = pst.executeQuery();
                tbProdutos.setModel(DbUtils.resultSetToTableModel(rs));

                double preco, x;

                String sqr = "select compra_x_venda from tbprodutos where estoque='Com controle de estoque.'";

                preco = 0;

                pst = conexao.prepareStatement(sqr);
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                for (int i = 0; i < tbAuxilio.getRowCount(); i++) {
                    x = Double.parseDouble(tbAuxilio.getModel().getValueAt(i, 0).toString().replace(".", "")) / 100;
                    preco = preco + x;
                    lblValor.setText(new DecimalFormat("#,##0.00").format(Double.parseDouble(String.valueOf(preco))).replace(",", "."));
                    lblNome.setText("Lucro Bruto: R$");

                }
            } else {
                rbReferrencialVenda.setSelected(true);
                instanciarTabelaVenda();
            }

        } catch (java.lang.NullPointerException e) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void imprimir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão do relatorio Referencial de Compra, Venda e Compra_X_Venda?", "Atençao", JOptionPane.YES_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                
                String sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                
                
                //tbAuxilio1.getModel().getValueAt(0, 0).toString()
                HashMap filtro = new HashMap();
                
                filtro.put("empresa", tbAuxilio.getModel().getValueAt(0, 0).toString());
                filtro.put("nome", tbAuxilio.getModel().getValueAt(0, 1).toString());
                filtro.put("email", tbAuxilio.getModel().getValueAt(0, 2).toString());
                filtro.put("descricao", tbAuxilio.getModel().getValueAt(0, 3).toString());
                filtro.put("obs", tbAuxilio.getModel().getValueAt(0, 4).toString());
                filtro.put("numero", tbAuxilio.getModel().getValueAt(0, 5).toString());
                
                filtro.put("imagem", pegarLogo());
                
                filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");
                                
                JasperReport jreport = JasperCompileManager.compileReport("src/reports/Referencial.jrxml");
                
                JasperPrint jprint = JasperFillManager.fillReport(jreport,filtro,conexao);
                
                JDialog tela = new JDialog(this,"LeGnu's - TelaRelatorio", true);
                
                tela.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                tela.setBackground(java.awt.SystemColor.control);
                tela.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
                tela.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                tela.setLocationRelativeTo(null);

                JRViewer painelRelatorio = new JRViewer(jprint);
                tela.getContentPane().add(painelRelatorio);
                tela.setVisible(true);
            }catch (Exception e) {
                               JOptionPane.showMessageDialog(null, "Verifique as Instancias de Relatorio na tela de configuraçoes.\nVerifique se a imagem do relarorio Existe");

                
            }
        }

    }

    public void calculoLucro() {

        try {

            double x;
            double y;
            int t = 1;

            for (int i = 0; i < tbProdutos.getRowCount(); i++) {
                x = Double.parseDouble(String.valueOf(Double.parseDouble(tbProdutos.getModel().getValueAt(i, 3).toString().replace(".", "")) / 100));
                y = Double.parseDouble(String.valueOf(Double.parseDouble(tbProdutos.getModel().getValueAt(i, 4).toString().replace(".", "")) / 100));
                String sqr = "update tbprodutos set compra_x_venda=? where idproduto=?";
                pst = conexao.prepareStatement(sqr);
                pst.setString(1, new DecimalFormat("#,##0.00").format(Double.parseDouble(String.valueOf(y - x))).replace(",", "."));
                pst.setString(2, tbProdutos.getModel().getValueAt(i, 0).toString());
                pst.executeUpdate();
                t++;

            }

        } catch (java.lang.NullPointerException e) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void tirar() {

        String sql = "alter table tbprodutos drop compra_x_venda";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void criar() {
        String sql = "alter table tbprodutos add compra_x_venda varchar(22)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo1 = new javax.swing.ButtonGroup();
        scAuxilio = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        lblUsuario = new javax.swing.JLabel();
        pnPrincipal = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rbVendaCompra = new javax.swing.JRadioButton();
        rbReferrencialVenda = new javax.swing.JRadioButton();
        rbReferencialCompra = new javax.swing.JRadioButton();
        scProdutos = new javax.swing.JScrollPane();
        tbProdutos = new javax.swing.JTable();
        lblNome = new javax.swing.JLabel();
        lblValor = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JToggleButton();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Inventario");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        pnPrincipal.setBackground(java.awt.SystemColor.control);
        pnPrincipal.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Referencia dos Produtos", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        rbVendaCompra.setBackground(java.awt.SystemColor.control);
        grupo1.add(rbVendaCompra);
        rbVendaCompra.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        rbVendaCompra.setText("Venda X Compra");
        rbVendaCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbVendaCompraActionPerformed(evt);
            }
        });

        rbReferrencialVenda.setBackground(java.awt.SystemColor.control);
        grupo1.add(rbReferrencialVenda);
        rbReferrencialVenda.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        rbReferrencialVenda.setText("Em Valor de Venda");
        rbReferrencialVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbReferrencialVendaActionPerformed(evt);
            }
        });

        rbReferencialCompra.setBackground(java.awt.SystemColor.control);
        grupo1.add(rbReferencialCompra);
        rbReferencialCompra.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        rbReferencialCompra.setText("Em Valor de Compra ");
        rbReferencialCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbReferencialCompraActionPerformed(evt);
            }
        });

        tbProdutos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbProdutos.setFocusable(false);
        tbProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProdutosMouseClicked(evt);
            }
        });
        scProdutos.setViewportView(tbProdutos);

        lblNome.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        lblNome.setText("Total: R$");

        lblValor.setFont(new java.awt.Font("Arial", 3, 24)); // NOI18N
        lblValor.setForeground(new java.awt.Color(0, 153, 51));
        lblValor.setText("0.00");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/ImpresoraIcon.png"))); // NOI18N
        btnImprimir.setBorderPainted(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblNome)
                .addGap(0, 0, 0)
                .addComponent(lblValor)
                .addGap(16, 16, 16))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 1242, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(rbReferencialCompra)
                .addGap(8, 8, 8)
                .addComponent(rbReferrencialVenda)
                .addGap(8, 8, 8)
                .addComponent(rbVendaCompra)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbVendaCompra)
                    .addComponent(rbReferrencialVenda)
                    .addComponent(rbReferencialCompra))
                .addGap(8, 8, 8)
                .addComponent(scProdutos, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblNome)
                        .addComponent(lblValor))
                    .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnPrincipalLayout = new javax.swing.GroupLayout(pnPrincipal);
        pnPrincipal.setLayout(pnPrincipalLayout);
        pnPrincipalLayout.setHorizontalGroup(
            pnPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnPrincipalLayout.setVerticalGroup(
            pnPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPrincipalLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1296, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:

    }//GEN-LAST:event_formWindowActivated

    private void tbProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdutosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbProdutosMouseClicked

    private void rbReferencialCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbReferencialCompraActionPerformed
        // TODO add your handling code here:
        instanciarTabelaCompra();
    }//GEN-LAST:event_rbReferencialCompraActionPerformed

    private void rbReferrencialVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbReferrencialVendaActionPerformed
        // TODO add your handling code here:
        instanciarTabelaVenda();
    }//GEN-LAST:event_rbReferrencialVendaActionPerformed

    private void rbVendaCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbVendaCompraActionPerformed
        // TODO add your handling code here:
        instanciarTabelaCompra_X_Venda();
    }//GEN-LAST:event_rbVendaCompraActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        TelaPrincipal principal = new TelaPrincipal();
            principal.setVisible(true);
                        principal.lblUsuario.setText(lblUsuario.getText());

            this.dispose();
    }//GEN-LAST:event_formWindowClosed

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
            java.util.logging.Logger.getLogger(TelaInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInventario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInventario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnImprimir;
    private javax.swing.ButtonGroup grupo1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblNome;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblValor;
    private javax.swing.JPanel pnPrincipal;
    private javax.swing.JRadioButton rbReferencialCompra;
    private javax.swing.JRadioButton rbReferrencialVenda;
    private javax.swing.JRadioButton rbVendaCompra;
    private javax.swing.JScrollPane scAuxilio;
    private javax.swing.JScrollPane scProdutos;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JTable tbProdutos;
    // End of variables declaration//GEN-END:variables
}
