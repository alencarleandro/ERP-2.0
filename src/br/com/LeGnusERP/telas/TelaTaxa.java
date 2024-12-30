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
import static br.com.LeGnusERP.telas.PontoDeVendas.cbComanda;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Ad3ln0r
 */
public class TelaTaxa extends javax.swing.JFrame {

    /**
     * Creates new form Taxas
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    String sql;

    public TelaTaxa() {
        initComponents();
        conexao = ModuloConexao.conector();
        instanciarTabelaEntregador();
        instanciarTabelaTaxa();
        setIcon();
    }

    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    public void instanciarTabelaEntregador() {
        try {
            sql = "select funcionario as Entregador from tbFuncionarios where especialidade = 'Motorista / Motoboy' ";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbEntregador.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void pesquisarEntregador() {
        try {
            String sql = "select funcionario as Entregador from tbFuncionarios where funcionario like ?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisaEntregador.getText() + "%");
            rs = pst.executeQuery();

            tbEntregador.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void instanciarTabelaHistorico() {
        try {
            sql = "select identificador as ID, cliente as Cliente, dia as Dia, hora as Hora from tbtotalvendas where entrega=? and status_pagamento = 'Pago' order by dia, hora desc";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntregador.getText());
            rs = pst.executeQuery();
            tbHistorico.setModel(DbUtils.resultSetToTableModel(rs));

            instanciarTA("Tabela");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void pesquisarHistorico() {
        try {
            sql = "select identificador as ID, cliente as Cliente, dia as Dia, hora as Hora from tbtotalvendas where entrega=? and  status_pagamento = 'Pago' and dia between '" + new java.sql.Date(dtHistoricoD.getDate().getTime()) + "' and '" + new java.sql.Date(dtHistoricoA.getDate().getTime()) + "' order by dia, hora desc";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtEntregador.getText());
            rs = pst.executeQuery();

            tbHistorico.setModel(DbUtils.resultSetToTableModel(rs));

            instanciarTA("Tabela");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void instanciarTabelaTaxa() {
        try {
            sql = "select id as ID, lugar as Região, preco as Preço from tbTaxa";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbTaxa.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }

    }

    private void pesquisarTaxa() {
        try {
            sql = "select id as ID, lugar as Região, preco as Preço from tbTaxa where lugar like ?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisa.getText() + "%");
            rs = pst.executeQuery();

            tbTaxa.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void instanciarTA(String modo) {
        try {
            if (modo.equals("Tabela")) {
                String taxa = "0.00";
                String valorBruto = "0.00";
                String valorComissao = "0.00";

                for (int i = 0; i < tbHistorico.getRowCount(); i++) {
                    
                    sql = "select valorBruto, valorComissão, taxa from tbEntrega where identificador = ?";
                    pst = conexao.prepareStatement(sql);
                    pst.setString(1, tbHistorico.getModel().getValueAt(i, 0).toString());
                    rs = pst.executeQuery();
                    tbAux.setModel(DbUtils.resultSetToTableModel(rs));

                    valorBruto = new DecimalFormat("#,##0.00").format(
                            (Float.parseFloat(valorBruto.replace(".", "").replace(",", "")) / 100)
                            + (Float.parseFloat(tbAux.getModel().getValueAt(0, 0).toString().replace(".", "").replace(",", "")) / 100)).replace(",", ".");

                    valorComissao = new DecimalFormat("#,##0.00").format(
                            (Float.parseFloat(valorComissao.replace(".", "").replace(",", "")) / 100)
                            + (Float.parseFloat(tbAux.getModel().getValueAt(0, 1).toString().replace(".", "").replace(",", "")) / 100)).replace(",", ".");

                    taxa = new DecimalFormat("#,##0.00").format(
                            (Float.parseFloat(taxa.replace(".", "").replace(",", "")) / 100)
                            + (Float.parseFloat(tbAux.getModel().getValueAt(0, 2).toString().replace(".", "").replace(",", "")) / 100)).replace(",", ".");
                }

                taDetalhe.setText("Descritivo da(s) entrega(s): " + txtEntregador.getText() + "\n\n"
                        + "Nº Vendas: " + tbHistorico.getRowCount() + "\n\n"
                                
                        + "Valor Vendido: R$" + valorBruto + "\n"
                        + "Comissão Adquirida: R$" + valorComissao + "\n"
                        + "Taxa Recebida: R$" + taxa);

            } else if (modo.equals("Setar")) {

                int setar = tbHistorico.getSelectedRow();
                
                sql = "select valorBruto, valorComissão, taxa, endereco, dia from tbEntrega where identificador = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, tbHistorico.getModel().getValueAt(setar, 0).toString());
                rs = pst.executeQuery();
                tbAux.setModel(DbUtils.resultSetToTableModel(rs));
                
                String valorBruto = tbAux.getModel().getValueAt(0, 0).toString();
                String valorComissao = tbAux.getModel().getValueAt(0, 1).toString();
                String taxa = tbAux.getModel().getValueAt(0, 2).toString();
                String endereco = tbAux.getModel().getValueAt(0, 3).toString();
                String dia = tbAux.getModel().getValueAt(0, 4).toString();
                
                sql = "select hora, forma_pagamento, cliente, funcionario from tbtotalvendas where identificador = ?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, tbHistorico.getModel().getValueAt(setar, 0).toString());
                rs = pst.executeQuery();
                tbAux.setModel(DbUtils.resultSetToTableModel(rs));
                
                String hora = tbAux.getModel().getValueAt(0, 0).toString();
                String formaPagamento = tbAux.getModel().getValueAt(0, 1).toString();
                String cliente = tbAux.getModel().getValueAt(0,2).toString();
                String funcionario = tbAux.getModel().getValueAt(0, 3).toString();

                taDetalhe.setText("Descritivo da entrega: " + txtEntregador.getText() + " ID:" + tbHistorico.getModel().getValueAt(setar, 0).toString() + "\n\n"
                        + "Venda para: " + cliente + "\n"
                        + "No dia: " + dia  + " as " + hora + "\n"                        
                        + "No Endereço: " + endereco + "\n\n"
                                
                        + "Vendido por: " + funcionario + "\n"
                        + "Pago em(no): " + formaPagamento + "\n\n"  
                                
                        + "Valor Vendido: R$" + valorBruto + "\n"
                        + "Comissão Adquirida: R$" + valorComissao + "\n"
                        + "Taxa Recebida: R$" + taxa);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void adicionar() {
        try {
            sql = "insert into tbTaxa(lugar,preco)values(?,?)";

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtRegiao.getText());
            pst.setString(2, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtPreco.getText().replace(",", "."))).replace(",", "."));

            int adicionado = pst.executeUpdate();

            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Taxa adicionada com Sucesso!");
                limpar();
            }

        } catch (java.lang.NumberFormatException c) {

            JOptionPane.showMessageDialog(null, "Preco só aceita Numeros.");
            JOptionPane.showMessageDialog(null, "Preco deve ser Salvo no Formato 0.00 .");
            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void alterar() {
        try {
            sql = "update tbTaxa set lugar=?,preco=? where id=?";

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtRegiao.getText());
            pst.setString(2, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtPreco.getText().replace(",", "."))).replace(",", "."));
            pst.setString(3, txtID.getText());

            int adicionado = pst.executeUpdate();
            //A Linha abaixo serve de apoio ao entendimento da logica
            //System.out.println(adicionado);
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Dados da Taxa alterado(s) com Sucesso!");
                limpar();

            }

        } catch (java.lang.NumberFormatException c) {

            JOptionPane.showMessageDialog(null, "Preco só aceita Numeros.");
            JOptionPane.showMessageDialog(null, "Preco deve ser Salvo no Formato 0.00 .");

            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    private void remover() {
        try {
            int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover esta Taxa?", "Atenção", JOptionPane.YES_NO_OPTION);
            if (confirma == JOptionPane.YES_OPTION) {

                sql = "delete from Taxa where id=?";

                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtID.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");
                    JOptionPane.showMessageDialog(null, "Taxa removida com Sucesso!");
                    limpar();
                }

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void setarCampos() {
        try {
            int setar = tbTaxa.getSelectedRow();

            txtID.setText(tbTaxa.getModel().getValueAt(setar, 0).toString());
            txtRegiao.setText(tbTaxa.getModel().getValueAt(setar, 1).toString());
            txtPreco.setText(tbTaxa.getModel().getValueAt(setar, 2).toString());

            txtPesquisa.setText(null);

            btnAdicionar.setEnabled(false);
            btnAlterar.setEnabled(true);
            btnRemover.setEnabled(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void setarCamposEntregador() {
        try {
            int setar = tbEntregador.getSelectedRow();

            txtEntregador.setText(tbEntregador.getModel().getValueAt(setar, 0).toString());

            pnEntrega.setEnabled(true);
            taDetalhe.setEnabled(true);

            pnHistorico.setEnabled(true);
            tbHistorico.setEnabled(true);
            dtHistoricoA.setEnabled(true);
            
            pnDepois.setEnabled(true);
            pnAntes.setEnabled(true);
            dtHistoricoD.setEnabled(true);
            
            btnPesquisar.setEnabled(true);

            instanciarTabelaHistorico();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void setarDetalhes() {
        try {
            int setar = tbHistorico.getSelectedRow();

            txtEntregador.setText(tbHistorico.getModel().getValueAt(setar, 0).toString());

            pnEntrega.setEnabled(true);
            taDetalhe.setEnabled(true);

            pnHistorico.setEnabled(true);
            tbHistorico.setEnabled(true);
            dtHistoricoA.setEnabled(true);
            dtHistoricoD.setEnabled(true);
            pnAntes.setEnabled(true);
            pnDepois.setEnabled(true);

            instanciarTabelaHistorico();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void limpar() {
        pnEntrega.setEnabled(false);
        taDetalhe.setEnabled(false);
        taDetalhe.setText(null);

        pnHistorico.setEnabled(false);
        ((DefaultTableModel) tbHistorico.getModel()).setRowCount(0);
        ((DefaultTableModel) tbHistorico.getModel()).setColumnCount(0);
        tbHistorico.setEnabled(false);
        dtHistoricoA.setEnabled(false);
        dtHistoricoA.setDate(null);
        dtHistoricoD.setEnabled(false);
        dtHistoricoD.setDate(null);
        btnPesquisar.setEnabled(false);
        pnAntes.setEnabled(false);
        pnDepois.setEnabled(false);

        txtPesquisaEntregador.setText(null);
        txtEntregador.setText(null);

        txtID.setText(null);
        txtPesquisa.setText(null);
        txtRegiao.setText(null);
        txtPreco.setText(null);

        btnAdicionar.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnRemover.setEnabled(false);

        instanciarTabelaEntregador();
        instanciarTabelaTaxa();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane5 = new javax.swing.JScrollPane();
        tbAux = new javax.swing.JTable();
        txtID = new javax.swing.JTextField();
        txtEntregador = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        pnTabela3 = new javax.swing.JPanel();
        pnTabela6 = new javax.swing.JPanel();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JToggleButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtRegiao = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtPreco = new javax.swing.JTextField();
        pnTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbTaxa = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        pnTabela2 = new javax.swing.JPanel();
        pnEntrega = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        taDetalhe = new javax.swing.JTextArea();
        pnHistorico = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbHistorico = new javax.swing.JTable();
        btnPesquisar = new javax.swing.JToggleButton();
        pnAntes = new javax.swing.JPanel();
        dtHistoricoA = new com.toedter.calendar.JDateChooser();
        pnDepois = new javax.swing.JPanel();
        dtHistoricoD = new com.toedter.calendar.JDateChooser();
        pnTabela4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtPesquisaEntregador = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbEntregador = new javax.swing.JTable();

        tbAux.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tbAux);

        txtID.setText("jTextField1");

        txtEntregador.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        pnTabela3.setBackground(java.awt.SystemColor.control);
        pnTabela3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));

        pnTabela6.setBackground(java.awt.SystemColor.control);

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

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setText("* Campos Obrigatorios");

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 0, new java.awt.Color(204, 204, 204)), "*Região", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRegiao)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRegiao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 204, 204)), "*Preço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPreco, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout pnTabela6Layout = new javax.swing.GroupLayout(pnTabela6);
        pnTabela6.setLayout(pnTabela6Layout);
        pnTabela6Layout.setHorizontalGroup(
            pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabela6Layout.createSequentialGroup()
                .addGroup(pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabela6Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabela6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnTabela6Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnTabela6Layout.createSequentialGroup()
                                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addComponent(btnAtualizar)))))
                .addGap(8, 8, 8))
        );
        pnTabela6Layout.setVerticalGroup(
            pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabela6Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(pnTabela6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAtualizar)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnTabela.setBackground(java.awt.SystemColor.control);
        pnTabela.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(153, 153, 153)));

        tbTaxa = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbTaxa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbTaxa.setFocusable(false);
        tbTaxa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbTaxaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbTaxa);

        jLabel1.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel1.setText("Pesquisar: ");

        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
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
                        .addComponent(txtPesquisa))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        pnTabelaLayout.setVerticalGroup(
            pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabelaLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnTabela3Layout = new javax.swing.GroupLayout(pnTabela3);
        pnTabela3.setLayout(pnTabela3Layout);
        pnTabela3Layout.setHorizontalGroup(
            pnTabela3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabela3Layout.createSequentialGroup()
                .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnTabela6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnTabela3Layout.setVerticalGroup(
            pnTabela3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnTabela3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTabela6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnTabela2.setBackground(java.awt.SystemColor.control);
        pnTabela2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Informações de Entregadores", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        pnEntrega.setBackground(java.awt.SystemColor.control);
        pnEntrega.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 0, new java.awt.Color(153, 153, 153)), "Detalhamento da Entrega", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnEntrega.setEnabled(false);

        taDetalhe.setEditable(false);
        taDetalhe.setColumns(20);
        taDetalhe.setRows(5);
        taDetalhe.setEnabled(false);
        taDetalhe.setFocusable(false);
        jScrollPane4.setViewportView(taDetalhe);

        javax.swing.GroupLayout pnEntregaLayout = new javax.swing.GroupLayout(pnEntrega);
        pnEntrega.setLayout(pnEntregaLayout);
        pnEntregaLayout.setHorizontalGroup(
            pnEntregaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnEntregaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnEntregaLayout.setVerticalGroup(
            pnEntregaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnEntregaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 273, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pnHistorico.setBackground(java.awt.SystemColor.control);
        pnHistorico.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Historico de Entregas", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnHistorico.setEnabled(false);
        pnHistorico.setFocusCycleRoot(true);

        tbHistorico = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbHistorico.setEnabled(false);
        tbHistorico.setFocusable(false);
        tbHistorico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbHistoricoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbHistorico);

        btnPesquisar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/lupa.png"))); // NOI18N
        btnPesquisar.setSelected(true);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnPesquisar.setEnabled(false);
        btnPesquisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        pnAntes.setBackground(java.awt.SystemColor.control);
        pnAntes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Antes de.", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Arial", 3, 12))); // NOI18N
        pnAntes.setEnabled(false);

        dtHistoricoA.setBackground(java.awt.SystemColor.control);
        dtHistoricoA.setDateFormatString("dd/MM/yyyy");
        dtHistoricoA.setEnabled(false);
        dtHistoricoA.setFocusable(false);
        dtHistoricoA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dtHistoricoAKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnAntesLayout = new javax.swing.GroupLayout(pnAntes);
        pnAntes.setLayout(pnAntesLayout);
        pnAntesLayout.setHorizontalGroup(
            pnAntesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtHistoricoA, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
        );
        pnAntesLayout.setVerticalGroup(
            pnAntesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtHistoricoA, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnDepois.setBackground(java.awt.SystemColor.control);
        pnDepois.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Depois de.", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Arial", 1, 12))); // NOI18N
        pnDepois.setEnabled(false);

        dtHistoricoD.setBackground(java.awt.SystemColor.control);
        dtHistoricoD.setDateFormatString("dd/MM/yyyy");
        dtHistoricoD.setEnabled(false);
        dtHistoricoD.setFocusable(false);
        dtHistoricoD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                dtHistoricoDKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnDepoisLayout = new javax.swing.GroupLayout(pnDepois);
        pnDepois.setLayout(pnDepoisLayout);
        pnDepoisLayout.setHorizontalGroup(
            pnDepoisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtHistoricoD, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        pnDepoisLayout.setVerticalGroup(
            pnDepoisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtHistoricoD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnHistoricoLayout = new javax.swing.GroupLayout(pnHistorico);
        pnHistorico.setLayout(pnHistoricoLayout);
        pnHistoricoLayout.setHorizontalGroup(
            pnHistoricoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnHistoricoLayout.createSequentialGroup()
                .addGroup(pnHistoricoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnHistoricoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(pnHistoricoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pnDepois, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnAntes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnHistoricoLayout.setVerticalGroup(
            pnHistoricoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnHistoricoLayout.createSequentialGroup()
                .addGroup(pnHistoricoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnHistoricoLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(pnHistoricoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnDepois, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnAntes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnHistoricoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnTabela4.setBackground(java.awt.SystemColor.control);
        pnTabela4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 1, new java.awt.Color(153, 153, 153)), "Painel de Escolha", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel3.setText("Escolher Entregador:");

        txtPesquisaEntregador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaEntregadorKeyReleased(evt);
            }
        });

        tbEntregador = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbEntregador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbEntregador.setFocusable(false);
        tbEntregador.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbEntregadorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbEntregador);

        javax.swing.GroupLayout pnTabela4Layout = new javax.swing.GroupLayout(pnTabela4);
        pnTabela4.setLayout(pnTabela4Layout);
        pnTabela4Layout.setHorizontalGroup(
            pnTabela4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabela4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnTabela4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTabela4Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisaEntregador))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnTabela4Layout.setVerticalGroup(
            pnTabela4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabela4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnTabela4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPesquisaEntregador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnTabela2Layout = new javax.swing.GroupLayout(pnTabela2);
        pnTabela2.setLayout(pnTabela2Layout);
        pnTabela2Layout.setHorizontalGroup(
            pnTabela2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabela2Layout.createSequentialGroup()
                .addComponent(pnTabela4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnEntrega, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnTabela2Layout.setVerticalGroup(
            pnTabela2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabela2Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(pnTabela2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnTabela4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnHistorico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnEntrega, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTabela2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnTabela3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnTabela3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnTabela2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
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

    private void tbTaxaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbTaxaMouseClicked
        // TODO add your handling code here:
        setarCampos();
    }//GEN-LAST:event_tbTaxaMouseClicked

    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        // TODO add your handling code here:
        pesquisarTaxa();
    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void tbHistoricoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbHistoricoMouseClicked
        // TODO add your handling code here:
        instanciarTA("Setar");
    }//GEN-LAST:event_tbHistoricoMouseClicked

    private void txtPesquisaEntregadorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaEntregadorKeyReleased
        // TODO add your handling code here:
        pesquisarEntregador();
    }//GEN-LAST:event_txtPesquisaEntregadorKeyReleased

    private void tbEntregadorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEntregadorMouseClicked
        // TODO add your handling code here:
        setarCamposEntregador();
    }//GEN-LAST:event_tbEntregadorMouseClicked

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void dtHistoricoAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtHistoricoAKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dtHistoricoAKeyReleased

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        pesquisarHistorico();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void dtHistoricoDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dtHistoricoDKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_dtHistoricoDKeyReleased

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
            java.util.logging.Logger.getLogger(TelaTaxa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaTaxa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaTaxa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaTaxa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaTaxa().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JToggleButton btnAtualizar;
    private javax.swing.JToggleButton btnPesquisar;
    private javax.swing.JButton btnRemover;
    private com.toedter.calendar.JDateChooser dtHistoricoA;
    private com.toedter.calendar.JDateChooser dtHistoricoD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnAntes;
    private javax.swing.JPanel pnDepois;
    private javax.swing.JPanel pnEntrega;
    private javax.swing.JPanel pnHistorico;
    private javax.swing.JPanel pnTabela;
    private javax.swing.JPanel pnTabela2;
    private javax.swing.JPanel pnTabela3;
    private javax.swing.JPanel pnTabela4;
    private javax.swing.JPanel pnTabela6;
    private javax.swing.JTextArea taDetalhe;
    private javax.swing.JTable tbAux;
    private javax.swing.JTable tbEntregador;
    private javax.swing.JTable tbHistorico;
    private javax.swing.JTable tbTaxa;
    private javax.swing.JTextField txtEntregador;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtPesquisaEntregador;
    private javax.swing.JTextField txtPreco;
    private javax.swing.JTextField txtRegiao;
    // End of variables declaration//GEN-END:variables
}
