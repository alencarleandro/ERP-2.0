/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.LeGnusERP.telas;

import java.sql.*;
import br.com.LeGnusERP.dal.ModuloConexao;
import java.awt.Color;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Leandro Clemente
 */
public class TelaLogin extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String Senha;
    String siglaRepresentante;
    int indexRepresentante;
    String zeros = "";

    int Larissa_Altair = 2014;
    int Sebastião_Alex = 1982;
    int Lucas_Pereira = 2008;
    int José_Clemente = 1954;
    int Luzia_Silva = 1980;
    int Vera_Margia = 1959;
    int Nathalia_Rezende = 1997;
    int Leandro_Clemente = 1979;
    int Lara_Margia = 2019;
    int Francisca_Altair = 1938;
    int Luan_Victor = 2010;
    int Leandro_Alencar = 2004;

    /**
     * Creates new form TelaLogin
     */
    public TelaLogin() {
        try {
            initComponents();
            conexao = ModuloConexao.conector();
            //A linha abaixo serve de apoio ao Status da conexão
            //System.out.println(conexao);
            if (conexao != null) {
                lblStatus.setText("Conectado ao Banco de Dados.");
                lblStatus.setForeground(Color.BLUE);
            } else {
                lblStatus.setText("Não Conectado ao MySQL.");
                lblStatus.setForeground(Color.RED);
            }
            lblSenha.setVisible(false);
            txtSenha.setVisible(false);

            lblSenha.setEnabled(false);
            txtSenha.setEnabled(false);
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void logar() {
        try {
            String sql = "select * from tbusuarios where login = ? and senha = ?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtVale.getText());
            String captura = new String(txtSenha.getPassword());
            pst.setString(2, captura);
            rs = pst.executeQuery();

            if (rs.next()) {
                String perfil = rs.getString(6);
                String nome = rs.getString(2);

                //System.out.println(perfil);
                //Linha acima competi a teste e a estrutura abaixo trata do perfil do usuario
                if (perfil.equals("Administrador") == true) {
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);

                    TelaPrincipal.MenCadUsu.setEnabled(true);
                    TelaPrincipal.lblUsuario.setText(nome);
                    TelaPrincipal.lblUsuario.setForeground(Color.red);
                    this.dispose();
                    conexao.close();
                } else if (perfil.equals("Usuario") == true) {

                    TelaLimitada limitada = new TelaLimitada();
                    limitada.setVisible(true);
                    TelaLimitada.btnPDV.setEnabled(false);
                    TelaLimitada.btnCadOS.setEnabled(false);
                    TelaLimitada.btnCadServiço.setEnabled(false);
                    TelaLimitada.lblNome.setText(nome);
                    this.dispose();
                    conexao.close();

                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário e/ou Senha inválido(s)");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void GerenciarSenha() {
        try {
            String sqo = "select senha from tbVale where idVale=1";
            pst = conexao.prepareStatement(sqo);
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));

            if (tbAuxilio.getModel().getValueAt(0, 0).toString().equals("vazio") == true) {
                criarSenha();
            } else {
                Date data = new Date();
                String sqy = "select vencimento from tbVale where idVale=1";
                pst = conexao.prepareStatement(sqy);
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                if (data.after(df.parse(tbAuxilio.getModel().getValueAt(0, 0).toString())) == true) {

                    criarSenha();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void criarSenha() {
        try {

            Date data = new Date();
            
            DateFormat df = new SimpleDateFormat("yyyy-MM-07");
            String mes = new SimpleDateFormat("MM").format(data);
            String ano = new SimpleDateFormat("yyyy").format(data);

            Senha = df.format(data)
                    .replace("-", "")
                    .replace("1", "X")
                    .replace("2", "U")
                    .replace("3", "I")
                    .replace("4", "H")
                    .replace("5", "G")
                    .replace("6", "E")
                    .replace("7", "D")
                    .replace("8", "C")
                    .replace("9", "B")
                    .replace("0", "Z");

            switch (mes) {
                case "01":
                    siglaRepresentante = "LA";
                    indexRepresentante = Integer.parseInt(ano) - Larissa_Altair;
                    break;
                case "02":
                    siglaRepresentante = "SA";
                    indexRepresentante = Integer.parseInt(ano) - Sebastião_Alex;
                    break;
                case "03":
                    siglaRepresentante = "LP";
                    indexRepresentante = Integer.parseInt(ano) - Lucas_Pereira;
                    break;
                case "04":
                    siglaRepresentante = "JC";
                    indexRepresentante = Integer.parseInt(ano) - José_Clemente;
                    break;
                case "05":
                    siglaRepresentante = "LS";
                    indexRepresentante = Integer.parseInt(ano) - Luzia_Silva;
                    break;
                case "06":
                    siglaRepresentante = "VM";
                    indexRepresentante = Integer.parseInt(ano) - Vera_Margia;
                    break;
                case "07":
                    siglaRepresentante = "NR";
                    indexRepresentante = Integer.parseInt(ano) - Nathalia_Rezende;
                    break;
                case "08":
                    siglaRepresentante = "LC";
                    indexRepresentante = Integer.parseInt(ano) - Leandro_Clemente;
                    break;
                case "09":
                    siglaRepresentante = "LM";
                    indexRepresentante = Integer.parseInt(ano) - Lara_Margia;
                    break;
                case "10":
                    siglaRepresentante = "FA";
                    indexRepresentante = Integer.parseInt(ano) - Francisca_Altair;
                    break;
                case "11":
                    siglaRepresentante = "LV";
                    indexRepresentante = Integer.parseInt(ano) - Luan_Victor;
                    break;
                case "12":
                    siglaRepresentante = "LA";
                    indexRepresentante = Integer.parseInt(ano) - Leandro_Alencar;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Erro ao criar mes: " + mes);
                    break;
            }

            Calendar cal = Calendar.getInstance();

            int limite = Integer.parseInt(mes) + 1;

            if (limite > 12) {
                limite = limite - 12;
                ano = String.valueOf(Integer.parseInt(ano) + 1);
            }

            cal.set(Integer.parseInt(ano), limite - 1, 7);

            data = cal.getTime();

            java.sql.Date dSqt = new java.sql.Date(data.getTime());
            df.format(dSqt);

            if (indexRepresentante < 10) {
                zeros = "ZZ";
            } else if (indexRepresentante < 100 && indexRepresentante >= 10) {
                zeros = "Z";
            }

            String sql = "update tbVale set senha=?, vencimento=? where idVale=1";
            pst = conexao.prepareStatement(sql);

            pst.setString(1, zeros + String.valueOf(indexRepresentante).replace("-", "")
                    .replace("1", "X")
                    .replace("2", "U")
                    .replace("3", "I")
                    .replace("4", "H")
                    .replace("5", "G")
                    .replace("6", "E")
                    .replace("7", "D")
                    .replace("8", "C")
                    .replace("9", "B")
                    .replace("0", "Z") + Senha + siglaRepresentante);
            pst.setDate(2, dSqt);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void VerificarSenha() {
        try {
            java.util.Date data = new java.util.Date();
            DateFormat Dia = new SimpleDateFormat("dd");

            String sqy = "select senha from tbVale where idVale=1";
            pst = conexao.prepareStatement(sqy);
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
            //tbauxilio.getModel().getValueAt(i, 2).toString()

            if (txtVale.getText().equals(tbAuxilio.getModel().getValueAt(0, 0).toString()) == true) {

                if (Dia.format(data).equals("04") == true) {
                    JOptionPane.showMessageDialog(null, "Antepenúltimo dia para vencimento da Senha Mensal.");
                }
                if (Dia.format(data).equals("05") == true) {
                    JOptionPane.showMessageDialog(null, "Penúltimo dia para vencimento da Senha Mensal.");
                }
                if (Dia.format(data).equals("06") == true) {
                    JOptionPane.showMessageDialog(null, "Último dia para vencimento da Senha Mensal.");
                }

                lblSenha.setVisible(true);
                txtSenha.setVisible(true);

                lblSenha.setEnabled(true);
                txtSenha.setEnabled(true);
                btnLogin.setText("Logar");

                lblVale.setText("Usuario:");
                txtVale.setText(null);

            } else {
                JOptionPane.showMessageDialog(null, "Senha Invalida entre em contato com \nWhatsApp: 31 97357-3354 \nEmail: legnudevelopment@gmail.com.");
            }

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

        scAuxilio = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        lblStatus = new javax.swing.JLabel();
        btnLogin = new br.com.LeGnusERP.Swing.botaoArredondado();
        txtSenha = new javax.swing.JPasswordField();
        lblSenha = new javax.swing.JLabel();
        txtVale = new javax.swing.JTextField();
        lblVale = new javax.swing.JLabel();

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
        setTitle("LeGnu's_ERP - Tela de Login");
        setBackground(new java.awt.Color(204, 204, 204));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jToggleButton1.setBackground(java.awt.SystemColor.control);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 321x230.png"))); // NOI18N
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblStatus.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblStatus.setText("Status");

        btnLogin.setForeground(new java.awt.Color(0, 0, 204));
        btnLogin.setText("Ativar");
        btnLogin.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        txtSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSenhaActionPerformed(evt);
            }
        });

        lblSenha.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblSenha.setText("Senha:");

        txtVale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtValeActionPerformed(evt);
            }
        });

        lblVale.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblVale.setText("Senha do Mes:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(231, 231, 231)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSenha, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblVale, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtVale, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addComponent(txtSenha))
                .addGap(263, 263, 263))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVale))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSenha))
                .addGap(44, 44, 44)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        setSize(new java.awt.Dimension(816, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        if (conexao != null) {
            GerenciarSenha();
        } else {

        }

    }//GEN-LAST:event_formWindowOpened

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        if (btnLogin.getText().equals("Ativar")) {
            VerificarSenha();
        } else {
            logar();
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void txtValeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtValeActionPerformed
        // TODO add your handling code here:
       if (btnLogin.getText().equals("Ativar")) {
            VerificarSenha();
        } else {
            logar();
        }
    }//GEN-LAST:event_txtValeActionPerformed

    private void txtSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSenhaActionPerformed
        // TODO add your handling code here:
        if (btnLogin.getText().equals("Ativar")) {
            VerificarSenha();
        } else {
            logar();
        }
    }//GEN-LAST:event_txtSenhaActionPerformed

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
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private br.com.LeGnusERP.Swing.botaoArredondado btnLogin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblVale;
    private javax.swing.JScrollPane scAuxilio;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtVale;
    // End of variables declaration//GEN-END:variables
}
