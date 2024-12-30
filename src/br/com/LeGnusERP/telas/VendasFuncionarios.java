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
import static br.com.LeGnusERP.telas.PontoDeVendas.lblValorTotal;
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
public class VendasFuncionarios extends javax.swing.JFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public VendasFuncionarios() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    public void instanciarTabela() {
        try {
            String sql = "select funcionario as Funcionario,especialidade as Especialidade from tbFuncionarios where especialidade != 'Motorista / Motoboy'";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }

    }

    private void setarVendas() {
        try {
            DaFinal.setDate(null);
            DaInicial.setDate(null);

            String sql = "select nome as Venda,tipo as Tipo,quantidade as QTDE,preco as Preço,comissao as Comissão_De,porcentagem as Ganho_Comissão,emicao as Emissão from tbvenda where (vendedor = ? or comissao = ? ) and tipo != 'Taxa'";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, tbPrincipal.getModel().getValueAt(tbPrincipal.getSelectedRow(), 0).toString());
            pst.setString(2, tbPrincipal.getModel().getValueAt(tbPrincipal.getSelectedRow(), 0).toString());
            rs = pst.executeQuery();
            tbVendas.setModel(DbUtils.resultSetToTableModel(rs));

            txtFuncionario.setText(tbPrincipal.getModel().getValueAt(tbPrincipal.getSelectedRow(), 0).toString());

            valorTotal();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void imprimir() {

        try {

            java.util.Date aInicial = DaInicial.getDate();
            java.sql.Date bInicial = new java.sql.Date(aInicial.getTime());
            java.util.Date aFinal = DaFinal.getDate();
            java.sql.Date bFinal = new java.sql.Date(aFinal.getTime());

            int setar = tbPrincipal.getSelectedRow();
            String clase = tbPrincipal.getModel().getValueAt(setar, 2).toString();

            if (clase.equals("Vendedor") == true) {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressao desta Nota?", "Atençao", JOptionPane.YES_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    try {

                        String sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                        pst = conexao.prepareStatement(sql);
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        HashMap filtro = new HashMap();

                        filtro.put("empresa", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("nome", tbAuxilio.getModel().getValueAt(0, 1).toString());
                        filtro.put("email", tbAuxilio.getModel().getValueAt(0, 2).toString());
                        filtro.put("descricao", tbAuxilio.getModel().getValueAt(0, 3).toString());
                        filtro.put("obs", tbAuxilio.getModel().getValueAt(0, 4).toString());
                        filtro.put("numero", tbAuxilio.getModel().getValueAt(0, 5).toString());
                        filtro.put("imagem", pegarLogo());
                        filtro.put("Inicial", bInicial);
                        filtro.put("Final", bFinal);
                        filtro.put("comissao", tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        String sqh = "select comissao from tbusuarios where usuario=?";
                        pst = conexao.prepareStatement(sqh);
                        pst.setString(1, tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        filtro.put("Porcentagem", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("Decimal", String.valueOf(Double.parseDouble(tbAuxilio.getModel().getValueAt(0, 0).toString().replace("%", "")) / 100));
                        filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                        filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");

                        JasperReport jreport = JasperCompileManager.compileReport("src/reports/VendasFuncionarioProdutos_Servico.jrxml");

                        JasperPrint jprint = JasperFillManager.fillReport(jreport, filtro, conexao);

                        JDialog tela = new JDialog(this, "LeGnu's - TelaRelatorio", true);

                        tela.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                        tela.setBackground(java.awt.SystemColor.control);
                        tela.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
                        tela.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                        tela.setLocationRelativeTo(null);

                        JRViewer painelRelatorio = new JRViewer(jprint);
                        tela.getContentPane().add(painelRelatorio);
                        tela.setVisible(true);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Verifique as Instancias de Relatorio na tela de configuraçoes.\nVerifique se a imagem do relarorio Existe");

                    }
                }
            } else if (clase.equals("Profissional/Tec") == true || clase.equals("Funcionario") == true) {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressao desta Nota?", "Atençao", JOptionPane.YES_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    try {

                        String sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                        pst = conexao.prepareStatement(sql);
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        HashMap filtro = new HashMap();

                        filtro.put("empresa", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("nome", tbAuxilio.getModel().getValueAt(0, 1).toString());
                        filtro.put("email", tbAuxilio.getModel().getValueAt(0, 2).toString());
                        filtro.put("descricao", tbAuxilio.getModel().getValueAt(0, 3).toString());
                        filtro.put("obs", tbAuxilio.getModel().getValueAt(0, 4).toString());
                        filtro.put("numero", tbAuxilio.getModel().getValueAt(0, 5).toString());
                        filtro.put("imagem", pegarLogo());
                        filtro.put("Inicial", bInicial);
                        filtro.put("Final", bFinal);
                        filtro.put("comissao", tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        String sqh = "select comissao from tbFuncionarios where funcionario=?";
                        pst = conexao.prepareStatement(sqh);
                        pst.setString(1, tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        filtro.put("Porcentagem", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("Decimal", String.valueOf(Double.parseDouble(tbAuxilio.getModel().getValueAt(0, 0).toString().replace("%", "")) / 100));

                        filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                        filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");

                        JasperReport jreport = JasperCompileManager.compileReport("src/reports/VendasFuncionarioProdutos_Servico.jrxml");

                        JasperPrint jprint = JasperFillManager.fillReport(jreport, filtro, conexao);

                        JDialog tela = new JDialog(this, "LeGnu's - TelaRelatorio", true);

                        tela.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                        tela.setBackground(java.awt.SystemColor.control);
                        tela.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
                        tela.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                        tela.setLocationRelativeTo(null);

                        JRViewer painelRelatorio = new JRViewer(jprint);
                        tela.getContentPane().add(painelRelatorio);
                        tela.setVisible(true);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Verifique as Instancias de Relatorio na tela de configuraçoes.\nVerifique se a imagem do relarorio Existe");

                    }
                }
            } else if (clase.equals("Administrador") == true || clase.equals("Profissional/Tec e Vendedor") == true) {
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressao desta Nota?", "Atençao", JOptionPane.YES_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    try {

                        String sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                        pst = conexao.prepareStatement(sql);
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        HashMap filtro = new HashMap();

                        filtro.put("empresa", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("nome", tbAuxilio.getModel().getValueAt(0, 1).toString());
                        filtro.put("email", tbAuxilio.getModel().getValueAt(0, 2).toString());
                        filtro.put("descricao", tbAuxilio.getModel().getValueAt(0, 3).toString());
                        filtro.put("obs", tbAuxilio.getModel().getValueAt(0, 4).toString());
                        filtro.put("numero", tbAuxilio.getModel().getValueAt(0, 5).toString());
                        filtro.put("imagem", pegarLogo());
                        filtro.put("Inicial", bInicial);
                        filtro.put("Final", bFinal);
                        filtro.put("comissao", tbPrincipal.getModel().getValueAt(setar, 0).toString());

                        String sqh = "select comissao from tbFuncionarios where funcionario=?";
                        pst = conexao.prepareStatement(sqh);
                        pst.setString(1, tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        filtro.put("porcentagem2", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("Decimal2", String.valueOf(Double.parseDouble(tbAuxilio.getModel().getValueAt(0, 0).toString().replace("%", "")) / 100));

                        String sqg = "select comissao from tbusuarios where usuario=?";
                        pst = conexao.prepareStatement(sqg);
                        pst.setString(1, tbPrincipal.getModel().getValueAt(setar, 0).toString());
                        rs = pst.executeQuery();
                        tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                        filtro.put("Porcentagem", tbAuxilio.getModel().getValueAt(0, 0).toString());
                        filtro.put("Decimal", String.valueOf(Double.parseDouble(tbAuxilio.getModel().getValueAt(0, 0).toString().replace("%", "")) / 100));

                        filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                        filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");

                        JasperReport jreport = JasperCompileManager.compileReport("src/reports/VendasFuncionario.jrxml");

                        JasperPrint jprint = JasperFillManager.fillReport(jreport, filtro, conexao);

                        JDialog tela = new JDialog(this, "LeGnu's - TelaRelatorio", true);

                        tela.setSize(Toolkit.getDefaultToolkit().getScreenSize());
                        tela.setBackground(java.awt.SystemColor.control);
                        tela.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
                        tela.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
                        tela.setLocationRelativeTo(null);

                        JRViewer painelRelatorio = new JRViewer(jprint);
                        tela.getContentPane().add(painelRelatorio);
                        tela.setVisible(true);

                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Verifique as Instancias de Relatorio na tela de configuraçoes.\nVerifique se a imagem do relarorio Existe");

                    }
                }
            }

            setarPorData();

        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Adicione uma Data Inicial e Final.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    public void valorTotal() {
        try {
            if (tbVendas.getRowCount() > 0) {
                String x = "0.00";
                for (int i = 0; i < tbVendas.getRowCount(); i++) {
                    x = new DecimalFormat("#,##0.00").format(
                            (Float.parseFloat(x.replace(".", "")) / 100) + (Float.parseFloat(tbVendas.getModel().getValueAt(i, 3).toString().replace(".", "")) / 100)
                    ).replace(",", ".");
                }
                lblValorVendido.setText("Valor vendido: R$" + new DecimalFormat("#,##0.00").format((Float.parseFloat(x.replace(".", "")) / 100)).replace(",", "."));

                x = "0.00";
                for (int i = 0; i < tbVendas.getRowCount(); i++) {
                    if (tbVendas.getModel().getValueAt(i, 4).toString().equals(txtFuncionario.getText())) {
                        x = new DecimalFormat("#,##0.00").format(
                                (Float.parseFloat(x.replace(".", "")) / 100) + ((Float.parseFloat(tbVendas.getModel().getValueAt(i, 3).toString().replace(".", "")) / 100) * (Float.parseFloat(tbVendas.getModel().getValueAt(i, 5).toString().replace("%", "")) / 100))
                        ).replace(",", ".");
                    }
                }
                lblComissaoFuncionario.setText("Comissão do funcionario escolhido: R$" + new DecimalFormat("#,##0.00").format((Float.parseFloat(x.replace(".", "")) / 100)).replace(",", "."));

                x = "0.00";
                for (int i = 0; i < tbVendas.getRowCount(); i++) {
                    if ((tbVendas.getModel().getValueAt(i, 4).toString().equals(txtFuncionario.getText())) == false) {
                        x = new DecimalFormat("#,##0.00").format(
                                (Float.parseFloat(x.replace(".", "")) / 100) + ((Float.parseFloat(tbVendas.getModel().getValueAt(i, 3).toString().replace(".", "")) / 100) * (Float.parseFloat(tbVendas.getModel().getValueAt(i, 5).toString().replace("%", "")) / 100))
                        ).replace(",", ".");
                    }
                }
                lblComissaoOutros.setText("Comissão de outros funcionario(s) envolvido(s) nas venda(s): R$" + new DecimalFormat("#,##0.00").format((Float.parseFloat(x.replace(".", "")) / 100)).replace(",", "."));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    //and  dia between '" + DaInicial + "' and '" + DaFinal + "'
    public void setarPorData() {
        try {
            if (txtFuncionario.getText().equals("") == false) {
                String sql = "select nome as Venda,tipo as Tipo,quantidade as QTDE,preco as Preço,comissao as Comissão_De,porcentagem as Ganho_Comissão,emicao as Emição from tbvenda where vendedor = ? and tipo != 'Taxa' and emicao between '" + new java.sql.Date(DaInicial.getDate().getTime()) + "' and '" + new java.sql.Date(DaFinal.getDate().getTime()) + "'";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtFuncionario.getText());
                rs = pst.executeQuery();
                tbVendas.setModel(DbUtils.resultSetToTableModel(rs));

                txtFuncionario.setText(tbPrincipal.getModel().getValueAt(tbPrincipal.getSelectedRow(), 0).toString());
            }else {
                JOptionPane.showMessageDialog(null, "Selecione um Funcionario.");
            }
        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Adicione uma Data Inicial e Final.");
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
        txtFuncionario = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblValorVendido = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbVendas = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        DaInicial = new com.toedter.calendar.JDateChooser();
        jPanel7 = new javax.swing.JPanel();
        DaFinal = new com.toedter.calendar.JDateChooser();
        btnSetar1 = new javax.swing.JToggleButton();
        lblComissaoFuncionario = new javax.swing.JLabel();
        lblComissaoOutros = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbPrincipal = new javax.swing.JTable();
        txtPesquisar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

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

        txtFuncionario.setToolTipText("");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR- Vendas Funcionarios.");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(153, 153, 153)));

        jPanel3.setBackground(java.awt.SystemColor.control);

        lblValorVendido.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblValorVendido.setText("Valor vendido: R$");

        tbPrincipal = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbVendas.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tbVendas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbVendas.setFocusable(false);
        tbVendas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbVendasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbVendas);

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Data Inicial", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        DaInicial.setBackground(java.awt.SystemColor.control);
        DaInicial.setDateFormatString("y-MM-dd");
        DaInicial.setFocusable(false);
        DaInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DaInicialKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(DaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Data Final", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        DaFinal.setBackground(java.awt.SystemColor.control);
        DaFinal.setDateFormatString("y-MM-dd");
        DaFinal.setFocusable(false);
        DaFinal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DaFinalKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(DaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        btnSetar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/lupa.png"))); // NOI18N
        btnSetar1.setBorderPainted(false);
        btnSetar1.setContentAreaFilled(false);
        btnSetar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnSetar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetar1ActionPerformed(evt);
            }
        });

        lblComissaoFuncionario.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblComissaoFuncionario.setText("Comissão do funcionario escolhido: R$");

        lblComissaoOutros.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblComissaoOutros.setText("Comissão de outros funcionario(s) envolvido(s) nas venda(s): R$");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblValorVendido)
                                    .addComponent(lblComissaoFuncionario)
                                    .addComponent(lblComissaoOutros)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(btnSetar1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 273, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSetar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addComponent(lblValorVendido)
                .addGap(8, 8, 8)
                .addComponent(lblComissaoFuncionario)
                .addGap(8, 8, 8)
                .addComponent(lblComissaoOutros)
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jPanel4.setBackground(java.awt.SystemColor.control);

        tbPrincipal = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbPrincipal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
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

        jLabel1.setBackground(java.awt.SystemColor.control);
        jLabel1.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel1.setText("Pesquisar: ");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
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
        instanciarTabela();
    }//GEN-LAST:event_formWindowOpened

    private void tbPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseClicked
        // TODO add your handling code here:
        setarVendas();
    }//GEN-LAST:event_tbPrincipalMouseClicked

    private void tbVendasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVendasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbVendasMouseClicked

    private void DaInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DaInicialKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_DaInicialKeyReleased

    private void DaFinalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DaFinalKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_DaFinalKeyReleased

    private void btnSetar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetar1ActionPerformed
        // TODO add your handling code here:
        setarPorData();
    }//GEN-LAST:event_btnSetar1ActionPerformed

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
            java.util.logging.Logger.getLogger(VendasFuncionarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VendasFuncionarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VendasFuncionarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VendasFuncionarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VendasFuncionarios().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DaFinal;
    private com.toedter.calendar.JDateChooser DaInicial;
    private javax.swing.JToggleButton btnSetar1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblComissaoFuncionario;
    private javax.swing.JLabel lblComissaoOutros;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblValorVendido;
    private javax.swing.JScrollPane scAuxilio;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JTable tbPrincipal;
    private javax.swing.JTable tbVendas;
    private javax.swing.JTextField txtFuncionario;
    private javax.swing.JTextField txtPesquisar;
    // End of variables declaration//GEN-END:variables
}
