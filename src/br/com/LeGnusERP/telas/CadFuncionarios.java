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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Ad3ln0r
 */
public class CadFuncionarios extends javax.swing.JFrame {

    /**
     * Creates new form CadFuncionarios
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String ID;

    public CadFuncionarios() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    public void instanciarTabela() {
        try {
            String sql = "select idFuncionario as ID, funcionario as Funcionario, salario as Salario, comissao as Comissão, contrato as Contrato, especialidade as Especialidade, tipo as Tipo, intervaloPagTaxa as Intervalo, dataPagTaxa as Data_Taxa from tbFuncionarios";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void Pesquisar() {

        String sql = "select idFuncionario as ID, funcionario as Funcionario, salario as Salario, comissao as Comissão, contrato as Contrato, especialidade as Especialidade, tipo as Tipo, intervaloPagTaxa as Intervalo, dataPagTaxa as Data_Taxa  from tbFuncionarios where funcionario like ?";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }

    }

    private void adicionar() {

        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            java.sql.Date dSql = new java.sql.Date(dtContrato.getDate().getTime());
            df.format(dSql);

            /*Intervalo 30 dias*/
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 30);

            Date data = cal.getTime();

            java.sql.Date dSqt = new java.sql.Date(data.getTime());
            df.format(dSqt);
            /**
             * ****************
             */
            Date dataTeste = new Date();
            cal = Calendar.getInstance();
            cal.setTime(dataTeste);

            if (cbIntervalo.getSelectedItem().toString().equals("Semanal")) {
                cal.add(Calendar.DATE, 7);
            } else if (cbIntervalo.getSelectedItem().toString().equals("Quinzena")) {
                cal.add(Calendar.DATE, 15);
            } else if (cbIntervalo.getSelectedItem().toString().equals("Mensal")) {
                cal.add(Calendar.DATE, 30);
            }

            dataTeste = cal.getTime();

            java.sql.Date dtSqt = new java.sql.Date(dataTeste.getTime());
            df.format(dtSqt);

            if ((txtFuncionario.getText().isEmpty() == true || txtSalario.getText().isEmpty() == true)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else if (Integer.parseInt(txtComissao.getText().replace("%", "")) > 100) {
                JOptionPane.showMessageDialog(null, "O maximo e 100%.");
                limpar();
            } else {
                String sql = "insert into tbFuncionarios(funcionario,salario,comissao,contrato,especialidade,tipo,data_pagamento,validade_contrato, intervaloPagTaxa, dataPagTaxa, taxa, valorVendido)values(?,?,?,?,?,?,?,?,?,?,?,?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtFuncionario.getText());
                pst.setString(2, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtSalario.getText().replace(",", "."))).replace(",", "."));
                pst.setString(3, String.valueOf(Integer.parseInt(txtComissao.getText().replace("%", ""))) + "%");
                pst.setDate(4, dSql);
                pst.setString(5, cbFunção.getSelectedItem().toString());
                pst.setString(6, "Funcionario");
                pst.setDate(7, dSqt);
                pst.setString(8, "Contrato em dia");
                pst.setString(9, cbIntervalo.getSelectedItem().toString());
                pst.setDate(10, dtSqt);
                pst.setString(11, "0.00");
                pst.setString(12, "0.00");

                //Validação dos Campos Obrigatorios
                //A linha abaixo atualiza os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Funcionario adicionado com sucesso");
                    limpar();
                }
            }

        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "As Comissão deve ser inserida como: 'numero' + '%'\n\nExemplo: 10%");
            limpar();
        } catch (java.lang.NullPointerException e) {
            if (dtContrato.getDate() == null == true) {
                JOptionPane.showMessageDialog(null, "Adicione uma Data de Contrato Valida.");
                limpar();
            } else {
                JOptionPane.showMessageDialog(null, e);
                limpar();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void alterar() {

        try {
            if ((txtFuncionario.getText().isEmpty() == true || txtSalario.getText().isEmpty() == true)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else if (Integer.parseInt(txtComissao.getText().replace("%", "")) > 100) {
                JOptionPane.showMessageDialog(null, "O maximo e 100%.");
                limpar();
            } else {

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                java.sql.Date dSql = new java.sql.Date(dtContrato.getDate().getTime());
                df.format(dSql);

                java.sql.Date dtSql = new java.sql.Date(dtTaxa.getDate().getTime());
                df.format(dtSql);

                String sql = "update tbFuncionarios set salario=?, comissao=?, contrato=?, especialidade=?, intervaloPagTaxa=?, dataPagTaxa=? where idFuncionario=?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtSalario.getText().replace(",", "."))).replace(",", "."));
                pst.setString(2, String.valueOf(Integer.parseInt(txtComissao.getText().replace("%", ""))) + "%");
                pst.setDate(3, dSql);
                pst.setString(4, cbFunção.getSelectedItem().toString());
                pst.setString(5, cbIntervalo.getSelectedItem().toString());
                pst.setDate(6, dtSql);
                pst.setString(7, ID);

                //A linha abaixo altera os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do Funcionario alterado(s) com sucesso");

                    limpar();

                }
            }

        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "As Comissão deve ser inserida como: 'numero' + '%'\n\nExemplo: 10%");
            limpar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    private void remover() {

        if (tbPrincipal.getModel().getValueAt(tbPrincipal.getSelectedRow(), 0).toString().equals("1")) {
            JOptionPane.showMessageDialog(null, "Este Funcionario é o Administrador.");
            limpar();
        } else if (txtTipo.getText().equals("Funcionario") == false) {
            JOptionPane.showMessageDialog(null, "Este Funcionario é Anexado a um Usuario\nCaso queira deletá-lo utilize a tela de usuarios.");
            limpar();
        } else {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este Funcionario?", "Atenção", JOptionPane.YES_NO_OPTION);

            if (confirma == JOptionPane.YES_OPTION) {

                try {
                    JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");
                    String sql = "delete from tbFuncionarios where idFuncionario=?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, ID);
                    pst.executeUpdate();

                    String sqy = "delete from tbos where funcionario=?";
                    pst = conexao.prepareStatement(sqy);
                    pst.setString(1, txtFuncionario.getText());
                    pst.executeUpdate();

                    String sqr = "delete from tbservicos where funcionario=?";
                    pst = conexao.prepareStatement(sqr);
                    pst.setString(1, txtFuncionario.getText());
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Funcionario removido com sucesso.");
                    limpar();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);
                    limpar();
                }
            }
        }

    }

    public void setar_campos() {
        //idFuncionario as ID, funcionario as Funcionario, salario as Salario, comissao as Comissão, contrato as Contrato, especialidade as Especialidade
        int setar = tbPrincipal.getSelectedRow();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            pnTaxa.setEnabled(true);
            dtTaxa.setEnabled(true);

            /*
            idFuncionario as ID, 
            funcionario as Funcionario, 
            salario as Salario, 
            comissao as Comissão, 
            contrato as Contrato, 
            especialidade as Especialidade, 
            tipo as Tipo, 
            intervaloPagTaxa as Intervalo, 
            dataPagTaxa as Data_Taxa */
            ID = tbPrincipal.getModel().getValueAt(setar, 0).toString();
            txtFuncionario.setEnabled(false);

            txtFuncionario.setText(tbPrincipal.getModel().getValueAt(setar, 1).toString());
            txtSalario.setText(String.valueOf(Double.parseDouble(tbPrincipal.getModel().getValueAt(setar, 2).toString().replace(".", "")) / 100));
            txtComissao.setText(tbPrincipal.getModel().getValueAt(setar, 3).toString());
            dtContrato.setDate(df.parse(tbPrincipal.getModel().getValueAt(setar, 4).toString()));
            cbFunção.setSelectedItem(tbPrincipal.getModel().getValueAt(setar, 5).toString());
            txtTipo.setText(tbPrincipal.getModel().getValueAt(setar, 6).toString());

            cbIntervalo.setSelectedItem(tbPrincipal.getModel().getValueAt(setar, 7).toString());
            dtTaxa.setDate(df.parse(tbPrincipal.getModel().getValueAt(setar, 8).toString()));

            //A Linha Abaixo desabilita o botão adicionar
            btnAdicionar.setEnabled(false);
            btnAtualizar.setEnabled(true);
            btnAlterar.setEnabled(true);
            btnRemover.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    public void limpar() {

        pnTaxa.setEnabled(false);
        dtTaxa.setEnabled(false);
        dtTaxa.setDate(null);

        cbIntervalo.setSelectedItem("Semanal");
        txtComissao.setText(null);
        cbFunção.setSelectedItem("Vendedor");
        txtFuncionario.setText(null);
        txtTipo.setText(null);
        txtFuncionario.setEnabled(true);
        txtSalario.setText("0.00");
        dtContrato.setDate(null);
        ID = null;

        btnAlterar.setEnabled(false);
        btnRemover.setEnabled(false);
        btnAdicionar.setEnabled(true);
        btnAtualizar.setEnabled(true);
        instanciarTabela();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtTipo = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        lblUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        btnRemover = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JToggleButton();
        pnTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPrincipal = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtFuncionario = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        cbFunção = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        cbIntervalo = new javax.swing.JComboBox<>();
        pnTaxa = new javax.swing.JPanel();
        dtTaxa = new com.toedter.calendar.JDateChooser();
        jPanel5 = new javax.swing.JPanel();
        dtContrato = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        txtSalario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtComissao = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        btnAlterar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();

        jScrollPane2.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela Cadastro de Funcionarios");
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
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel8.setBackground(java.awt.SystemColor.control);
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

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

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRestart-removebg-preview.png"))); // NOI18N
        btnAtualizar.setToolTipText("");
        btnAtualizar.setBorderPainted(false);
        btnAtualizar.setContentAreaFilled(false);
        btnAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 746, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Funcionario", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtFuncionario)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Função", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbFunção.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Vendedor", "Profissional/Tec", "Profissional/Tec e Vendedor", "Motorista / Motoboy" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbFunção, 0, 210, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbFunção, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Intervalo Pag.Taxa", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbIntervalo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semanal", "Quinzena", "Mensal" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(cbIntervalo, 0, 173, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbIntervalo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pnTaxa.setBackground(java.awt.SystemColor.control);
        pnTaxa.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Prox Pagamento Taxa", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnTaxa.setEnabled(false);

        dtTaxa.setBackground(java.awt.SystemColor.control);
        dtTaxa.setDateFormatString("dd/MM/yyyy");
        dtTaxa.setEnabled(false);

        javax.swing.GroupLayout pnTaxaLayout = new javax.swing.GroupLayout(pnTaxa);
        pnTaxa.setLayout(pnTaxaLayout);
        pnTaxaLayout.setHorizontalGroup(
            pnTaxaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtTaxa, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
        );
        pnTaxaLayout.setVerticalGroup(
            pnTaxaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtTaxa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Contratado até", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        dtContrato.setBackground(java.awt.SystemColor.control);
        dtContrato.setDateFormatString("dd/MM/yyyy");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtContrato, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Salario", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtSalario.setText("0.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSalario, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Comissão %", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtComissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0%"))));
        txtComissao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtComissaoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtComissao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(pnTaxa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addGap(0, 79, Short.MAX_VALUE)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnAtualizar)
                        .addContainerGap(90, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(16, 16, 16))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnTaxa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_formWindowOpened

    private void txtPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisarKeyReleased
        // TODO add your handling code here:
        Pesquisar();
    }//GEN-LAST:event_txtPesquisarKeyReleased

    private void tbPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseClicked
        // TODO add your handling code here:
        setar_campos();
    }//GEN-LAST:event_tbPrincipalMouseClicked

    private void txtComissaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtComissaoMouseClicked

    }//GEN-LAST:event_txtComissaoMouseClicked

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
            java.util.logging.Logger.getLogger(CadFuncionarios.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadFuncionarios.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadFuncionarios.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadFuncionarios.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadFuncionarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JToggleButton btnAtualizar;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox<String> cbFunção;
    private javax.swing.JComboBox<String> cbIntervalo;
    private com.toedter.calendar.JDateChooser dtContrato;
    private com.toedter.calendar.JDateChooser dtTaxa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnTabela;
    private javax.swing.JPanel pnTaxa;
    private javax.swing.JTable tbPrincipal;
    private javax.swing.JFormattedTextField txtComissao;
    private javax.swing.JTextField txtFuncionario;
    private javax.swing.JTextField txtPesquisar;
    private javax.swing.JTextField txtSalario;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables
}
