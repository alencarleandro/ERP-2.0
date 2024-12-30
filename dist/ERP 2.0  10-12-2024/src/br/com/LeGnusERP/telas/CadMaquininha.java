/*
 * The MIT License
 *
 * Copyright 2023 Ad3ln0r.
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
public class CadMaquininha extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String sql;
    int id;

    /**
     * Creates new form CadMaquininha
     */
    public CadMaquininha() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    private void instanciarTabela() {
        try {
            sql = "Select id as ID,nome as Nome,taxaDebito as Taxa_Debito,taxaCredito as Taxa_Credito,antecipacao as Antecipação,taxaAntecipacao as Taxa_da_Antecipação,compensacaoDebito as Compensação_Debito,compensacaoCredito as Compensação_Credito from tbMaquininha";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();

            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void Pesquisar() {
        try {
            sql = "Select id as ID,nome as Nome,taxaDebito as Taxa_Debito,taxaCredito as Taxa_Credito,antecipacao as Antecipação,taxaAntecipacao as Taxa_da_Antecipação,compensacaoDebito as Compensação_Debito,compensacaoCredito as Compensação_Credito from tbMaquininha where nome like ?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();

            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void adicionar() {
        try {
            sql = "insert into tbMaquininha(nome,taxaDebito,taxaCredito,antecipacao,taxaAntecipacao,compensacaoCredito,compensacaoDebito) values(?,?,?,?,?,?,?)";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtMaquininha.getText());
            pst.setString(2, String.valueOf(Float.parseFloat(txtTaxaDebito.getText().replace("%", ""))) + "%");
            pst.setString(3, String.valueOf(Float.parseFloat(txtTaxaCredito.getText().replace("%", ""))) + "%");
            if (rbSim.isSelected() == true) {
                pst.setString(4, "Sim");
                pst.setString(5, String.valueOf(Float.parseFloat(txtTaxaAntecipacao.getText().replace("%", ""))) + "%");
                pst.setString(6, String.valueOf(Integer.parseInt(txtCompensacaoCredito.getText())));
            } else {
                pst.setString(4, "Não");
                pst.setString(5, "0%");
                pst.setString(6, "Nulo");
            }
            pst.setString(7, String.valueOf(Integer.parseInt(txtCompensacaoDebito.getText())));
            pst.executeUpdate();
            limpar();
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "As taxas e compensação aceitam somente numeros!");
            limpar();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Essa maquininha já existe.");
            limpar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void alterar() {
        try {
            sql = "update tbMaquininha set nome=?,taxaDebito=?,taxaCredito=?,antecipacao=?,taxaAntecipacao=?,compensacaoCredito=?,compensacaoDebito=? where id=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtMaquininha.getText());
            pst.setString(2, String.valueOf(Float.parseFloat(txtTaxaDebito.getText().replace("%", ""))) + "%");
            pst.setString(3, String.valueOf(Float.parseFloat(txtTaxaCredito.getText().replace("%", ""))) + "%");
            if (rbSim.isSelected() == true) {
                pst.setString(4, "Sim");
                pst.setString(5, String.valueOf(Float.parseFloat(txtTaxaAntecipacao.getText().replace("%", ""))) + "%");
                pst.setString(6, String.valueOf(Integer.parseInt(txtCompensacaoCredito.getText())));
            } else {
                pst.setString(4, "Não");
                pst.setString(5, "0%");
                pst.setString(6, "Nulo");
            }
            pst.setString(7, String.valueOf(Integer.parseInt(txtCompensacaoDebito.getText())));
            pst.setString(8, String.valueOf(id));
            pst.executeUpdate();
            limpar();
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "As taxas e compensação aceitam somente numeros!");
            limpar();
        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Essa maquininha já existe.");
            limpar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void setarCampos() {
        int setar = tbPrincipal.getSelectedRow();
        id = Integer.parseInt(tbPrincipal.getModel().getValueAt(setar, 0).toString());
        txtMaquininha.setText(tbPrincipal.getModel().getValueAt(setar, 1).toString());
        txtTaxaDebito.setText(tbPrincipal.getModel().getValueAt(setar, 2).toString());
        txtTaxaCredito.setText(tbPrincipal.getModel().getValueAt(setar, 3).toString());
        if (tbPrincipal.getModel().getValueAt(setar, 4).toString().equals("Sim") == true) {
            rbSim.setSelected(true);
            pnT.setEnabled(true);
            pnC.setEnabled(true);
            txtCompensacaoCredito.setEnabled(true);
            txtTaxaAntecipacao.setEnabled(true);
        } else if (tbPrincipal.getModel().getValueAt(setar, 4).toString().equals("Não") == true) {
            rbNao.setSelected(true);
            pnT.setEnabled(false);
            pnC.setEnabled(false);
            txtCompensacaoCredito.setText(null);
            txtTaxaAntecipacao.setText(null);
            txtCompensacaoCredito.setEnabled(false);
            txtTaxaAntecipacao.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Erro ao setar Antecipação");
        }
        txtTaxaAntecipacao.setText(tbPrincipal.getModel().getValueAt(setar, 5).toString());
        txtCompensacaoDebito.setText(tbPrincipal.getModel().getValueAt(setar, 6).toString());
        txtCompensacaoCredito.setText(tbPrincipal.getModel().getValueAt(setar, 7).toString());

        btnAlterar.setEnabled(true);
        btnRemover.setEnabled(true);
        btnAdicionar.setEnabled(false);
    }

    private void remover() {
        try {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta Maquininha?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (confirma == JOptionPane.YES_OPTION) {

                JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");
                sql = "delete from tbMaquininha where id=?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, String.valueOf(id));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Maquininha removido com sucesso.");
                limpar();

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    private void limpar() {
        try {
            txtMaquininha.setText(null);
            txtTaxaDebito.setText(null);
            txtTaxaCredito.setText(null);
            txtPesquisar.setText(null);
            pnT.setEnabled(false);
            pnC.setEnabled(false);
            txtCompensacaoCredito.setEnabled(false);
            txtTaxaAntecipacao.setEnabled(false);
            txtTaxaAntecipacao.setText(null);
            txtCompensacaoCredito.setText(null);
            txtCompensacaoDebito.setText(null);
            rbNao.setSelected(true);
            btnAdicionar.setEnabled(true);
            btnRemover.setEnabled(false);
            btnAlterar.setEnabled(false);
            instanciarTabela();

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

        buttonGroup1 = new javax.swing.ButtonGroup();
        lblUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnResetar = new javax.swing.JToggleButton();
        btnAdicionar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtTaxaCredito = new javax.swing.JTextField();
        pnT = new javax.swing.JPanel();
        txtTaxaAntecipacao = new javax.swing.JTextField();
        pnC = new javax.swing.JPanel();
        txtCompensacaoCredito = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        rbSim = new javax.swing.JRadioButton();
        rbNao = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtTaxaDebito = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        txtCompensacaoDebito = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtMaquininha = new javax.swing.JTextField();
        pnTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPrincipal = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela Cadastro de Maquininha");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jLabel2.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel2.setText("* Campos Obrigatorios");

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeEditar-removebg-preview.png"))); // NOI18N
        btnAlterar.setToolTipText("");
        btnAlterar.setContentAreaFilled(false);
        btnAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAlterar.setEnabled(false);
        btnAlterar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRemover-removebg-preview.png"))); // NOI18N
        btnRemover.setToolTipText("");
        btnRemover.setContentAreaFilled(false);
        btnRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRemover.setEnabled(false);
        btnRemover.setPreferredSize(new java.awt.Dimension(80, 80));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnResetar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRestart-removebg-preview.png"))); // NOI18N
        btnResetar.setToolTipText("");
        btnResetar.setBorderPainted(false);
        btnResetar.setContentAreaFilled(false);
        btnResetar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnResetar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetarActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeAdicionar-removebg-preview.png"))); // NOI18N
        btnAdicionar.setToolTipText("");
        btnAdicionar.setContentAreaFilled(false);
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Configuração Credito", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*TaxaCredito(%)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTaxaCredito)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(txtTaxaCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pnT.setBackground(java.awt.SystemColor.control);
        pnT.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Taxa_Antecipação(%)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnT.setEnabled(false);

        txtTaxaAntecipacao.setEnabled(false);

        javax.swing.GroupLayout pnTLayout = new javax.swing.GroupLayout(pnT);
        pnT.setLayout(pnTLayout);
        pnTLayout.setHorizontalGroup(
            pnTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTaxaAntecipacao, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE)
        );
        pnTLayout.setVerticalGroup(
            pnTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTaxaAntecipacao)
        );

        pnC.setBackground(java.awt.SystemColor.control);
        pnC.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Compensação_Credito(Em Dias)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnC.setEnabled(false);

        txtCompensacaoCredito.setEnabled(false);

        javax.swing.GroupLayout pnCLayout = new javax.swing.GroupLayout(pnC);
        pnC.setLayout(pnCLayout);
        pnCLayout.setHorizontalGroup(
            pnCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCompensacaoCredito, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
        );
        pnCLayout.setVerticalGroup(
            pnCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCompensacaoCredito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel8.setBackground(java.awt.SystemColor.control);
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Antecipação", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        buttonGroup1.add(rbSim);
        rbSim.setText("Sim");
        rbSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSimActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbNao);
        rbNao.setSelected(true);
        rbNao.setText("Não");
        rbNao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rbSim)
                .addGap(16, 16, 16)
                .addComponent(rbNao)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rbNao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(rbSim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Configuração Debito", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*TaxaDebito(%)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTaxaDebito, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTaxaDebito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel10.setBackground(java.awt.SystemColor.control);
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Compensação_Debito(Em Dias)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCompensacaoDebito)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCompensacaoDebito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Maquininha", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMaquininha)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMaquininha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnTabela.setBackground(java.awt.SystemColor.control);
        pnTabela.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(153, 153, 153)));

        tbPrincipal = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbPrincipal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbPrincipal.setFocusable(false);
        tbPrincipal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPrincipalMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbPrincipal);

        jLabel1.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel1.setText("Pesquisar: ");

        txtPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnTabelaLayout = new javax.swing.GroupLayout(pnTabela);
        pnTabela.setLayout(pnTabelaLayout);
        pnTabelaLayout.setHorizontalGroup(
            pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTabelaLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisar))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 829, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnTabelaLayout.setVerticalGroup(
            pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabelaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnResetar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(16, 16, 16))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 249, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemover, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnResetar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
            .addComponent(pnTabela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

        setSize(new java.awt.Dimension(1296, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tbPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseClicked
        // TODO add your handling code here:
        setarCampos();
    }//GEN-LAST:event_tbPrincipalMouseClicked

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        // TODO add your handling code here:
        Pesquisar();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnResetarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnResetarActionPerformed

    private void rbSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSimActionPerformed
        // TODO add your handling code here:
        pnT.setEnabled(true);
        pnC.setEnabled(true);
        txtCompensacaoCredito.setEnabled(true);
        txtTaxaAntecipacao.setEnabled(true);
    }//GEN-LAST:event_rbSimActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_formWindowOpened

    private void rbNaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNaoActionPerformed
        // TODO add your handling code here:
        pnT.setEnabled(false);
        pnC.setEnabled(false);
        txtCompensacaoCredito.setText(null);
        txtTaxaAntecipacao.setText(null);
        txtCompensacaoCredito.setEnabled(false);
        txtTaxaAntecipacao.setEnabled(false);
    }//GEN-LAST:event_rbNaoActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

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
            java.util.logging.Logger.getLogger(CadMaquininha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadMaquininha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadMaquininha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadMaquininha.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadMaquininha().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JToggleButton btnResetar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnC;
    private javax.swing.JPanel pnT;
    private javax.swing.JPanel pnTabela;
    private javax.swing.JRadioButton rbNao;
    private javax.swing.JRadioButton rbSim;
    private javax.swing.JTable tbPrincipal;
    private javax.swing.JTextField txtCompensacaoCredito;
    private javax.swing.JTextField txtCompensacaoDebito;
    private javax.swing.JTextField txtMaquininha;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtTaxaAntecipacao;
    private javax.swing.JTextField txtTaxaCredito;
    private javax.swing.JTextField txtTaxaDebito;
    // End of variables declaration//GEN-END:variables
}
