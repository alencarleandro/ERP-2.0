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

/**
 *
 * @author Ad3ln0r
 */
public class telaGastos extends javax.swing.JFrame {

    /**
     * Creates new form telaSangria
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;    
    String sql;
    String tipo1,tipo2,tipo3,tipo4,data1,data2;
    int estado = 0;
    double x;
    
    String identificadorGasto;
    
    java.sql.Date bInicial;
    java.sql.Date bFinal;

    public telaGastos() {
        initComponents();
        conexao = ModuloConexao.conector();

        setIcon();
    }

    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));

    }

    private void instanciarTabela() {
        try {
            if (estado == 1) {
                if (rbTodos.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00' and data_pagamento between '" + bInicial + "' and '" + bFinal + "';";
                    tipo1 = "Sangria";
                    tipo2 = "Compra";
                    tipo3 = "Comissao";
                    tipo4 = "Pagamento";
                    data1 = String.valueOf(bInicial);
                    data2 = String.valueOf(bFinal);
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) Registrado(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbCompras.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where tipo ='Compra' and valor != '0.00' and data_pagamento between '" + bInicial + "' and '" + bFinal + "';";
                    tipo1 = "Compra";
                    tipo2 = "";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = String.valueOf(bInicial);
                    data2 = String.valueOf(bFinal);
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) em Compra(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbSangria.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where tipo ='Sangria' and valor != '0.00' and data_pagamento between '" + bInicial + "' and '" + bFinal + "';";
                    tipo1 = "Sangria";
                    tipo2 = "";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = String.valueOf(bInicial);
                    data2 = String.valueOf(bFinal);
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Sangria(s) Registrada(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbFuncionarios.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00' and data_pagamento between '" + bInicial + "' and '" + bFinal + "' and tipo = 'Comissao' or tipo = 'Pagamento'";
                    tipo1 = "Comissao";
                    tipo2 = "Pagamento";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = String.valueOf(bInicial);
                    data2 = String.valueOf(bFinal);
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) com Funcionario(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else {
                    rbTodos.setSelected(true);
                    instanciarTabela();
                }
            } else {
                if (rbTodos.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00'";
                    tipo1 = "Sangria";
                    tipo2 = "Compra";
                    tipo3 = "Comissao";
                    tipo4 = "Pagamento";
                    data1 = "0001-07-21";
                    data2 = "9999-07-21";
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) Registrado(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbCompras.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00' and tipo ='Compra'";
                    tipo1 = "Compra";
                    tipo2 = "";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = "0001-07-21";
                    data2 = "9999-07-21";
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) em Compra(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbSangria.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00' and tipo ='Sangria'";
                    tipo1 = "Sangria";
                    tipo2 = "";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = "0001-07-21";
                    data2 = "9999-07-21";
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Sangria(s) Registrada(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else if (rbFuncionarios.isSelected() == true) {
                    sql = "select idgastos as ID, nome as Nome, data_pagamento as Data_Pagamento, forma_pagamento as Forma_de_Pagamento, status_pagamento as Status_Pagamento, valor as Valor_Gasto,tipo as Tipo from tbgastos where valor != '0.00' and tipo ='Comissao' or tipo = 'Pagamento'";
                    tipo1 = "Comissao";
                    tipo2 = "Pagamento";
                    tipo3 = "";
                    tipo4 = "";
                    data1 = "0001-07-21";
                    data2 = "9999-07-21";
                    jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gasto(s) com Funcionario(s)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

                } else {
                    rbTodos.setSelected(true);
                    instanciarTabela();
                }
            }
            
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbGastos.setModel(DbUtils.resultSetToTableModel(rs));
            valorTotal();
            txtSangria.setText(null);
            txtDescricao.setText(null);
            estado = 0;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    private void valorTotal(){
        x = 0;
        for(int i = 0; i < tbGastos.getRowCount(); i++){
        x = x + Double.parseDouble(String.valueOf(Double.parseDouble(tbGastos.getModel().getValueAt(i, 5).toString().replace(".", "")) / 100).replace(",", "."));
        }
        lblValorTotal.setText(new DecimalFormat("#,##0.00").format(x).replace(",", "."));
    }

    private void validarData() {
        try {
            java.util.Date aInicial = DaInicial.getDate();
            java.util.Date aFinal = DaFinal.getDate();
            bInicial = new java.sql.Date(aInicial.getTime());
            bFinal = new java.sql.Date(aFinal.getTime());
            estado = 1;
            instanciarTabela();
        } catch (java.lang.NullPointerException e) {
            JOptionPane.showMessageDialog(null, "Adicione uma Data inicial e final.");
            DaFinal.setDate(null);
            DaInicial.setDate(null);            
            estado = 0;
            instanciarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void adicionar() {

        try {
            Date d = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date dSql = new java.sql.Date(d.getTime());
            df.format(dSql);
            sql = "insert into tbgastos(nome, data_pagamento, status_pagamento, valor, tipo)values(?,?,?,?,?)";
            pst = conexao.prepareStatement(sql);

            if (txtDescricao.getText().isEmpty()) {
                pst.setString(1, "Sangria");
            } else {
                pst.setString(1, txtDescricao.getText());
            }

            pst.setDate(2, dSql);
            pst.setString(3, "Pago");
            pst.setString(4, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtSangria.getText().replace(",", "."))).replace(",", "."));
            pst.setString(5, "Sangria");

            //Validação dos Campos Obrigatorios
            if ((txtSangria.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Adicione uma Sangria.");
                limpar();

            } else if (Double.parseDouble(txtSangria.getText().replace(",", ".")) <= 0) {
                JOptionPane.showMessageDialog(null, "Adicione uma Sangria maior que 0.");
                limpar();

            } else {

                //A linha abaixo atualiza os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Sangria adicionada com sucesso.");
                    instanciarTabela();
                }
            }

        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Somente Numeros.");
            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }
    
    public void Pagar() {
        int confirma;
        String opcao = "Vazio";
        String data;
        Date d = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date dSql = new java.sql.Date(d.getTime());
        df.format(dSql);

        try {
            int setar = tbGastos.getSelectedRow();
            opcao = JOptionPane.showInputDialog("Pagar Conta >>> Digite 'P'.\n"
                    + "Alterar Data >>> Digite 'A'.\n"
                    + "Alterar Valor >>> Digite 'V'.\n"
                    + "Deletar Conta >>> Digite 'D'.\n\n"
                    + "OBS:Caracteres aceitados são somente P,A,V,D.");

            if (opcao.equals("P") == true || opcao.equals("p") == true) {
                confirma = JOptionPane.showConfirmDialog(null, "Deseja Pagar esta Conta.", "Atençao", JOptionPane.YES_OPTION);
            } else if (opcao.equals("A") == true || opcao.equals("a") == true) {
                confirma = JOptionPane.showConfirmDialog(null, "Deseja Alterar o data desta Conta.", "Atençao", JOptionPane.YES_OPTION);
            } else if (opcao.equals("V") == true || opcao.equals("v") == true) {
                confirma = JOptionPane.showConfirmDialog(null, "Deseja Alterar a valor desta Conta.", "Atençao", JOptionPane.YES_OPTION);
            } else if (opcao.equals("D") == true || opcao.equals("d") == true) {
                confirma = JOptionPane.showConfirmDialog(null, "Deseja Deletar esta Conta.", "Atençao", JOptionPane.YES_OPTION);
            } else {
                JOptionPane.showMessageDialog(null, "Alternativa invalida, as Disponiveis são P,A,V,D.");
                confirma = 1;
            }

            if (confirma == JOptionPane.YES_OPTION) {
                try {
                    String id = tbGastos.getModel().getValueAt(setar, 0).toString();

                    if (opcao.equals("P") == true || opcao.equals("p")) {
                        String sqo = "update tbgastos set status_pagamento='Pago', data_pagamento=? where idgastos=?";

                        pst = conexao.prepareStatement(sqo);
                        pst.setDate(1, dSql);
                        pst.setString(2, id);
                        pst.executeUpdate();

                    } else if (opcao.equals("A") == true || opcao.equals("a") == true) {
                        try {
                            data = JOptionPane.showInputDialog("Insira uma nova data no formato yyyy-MM-dd.");

                            Date alterada = df.parse(data);
                            java.sql.Date dSqt = new java.sql.Date(alterada.getTime());
                            df.format(dSqt);

                            String sqo = "update tbgastos set data_pagamento=? where idgastos=?";

                            pst = conexao.prepareStatement(sqo);
                            pst.setDate(1, dSqt);
                            pst.setString(2, id);
                            pst.executeUpdate();
                        } catch (java.text.ParseException e) {
                            JOptionPane.showMessageDialog(null, "Data deve ser salva no formato yyyy-MM-dd.");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "Data deve ser salva no formato yyyy-MM-dd.");

                        }

                    } else if (opcao.equals("V") == true || opcao.equals("v") == true) {
                        try {

                            String justificativa = JOptionPane.showInputDialog("Adicione uma Justificativa para a alteração do valor.");
                            String valor = JOptionPane.showInputDialog("Adicione o novo valor.");

                            String sqo = "update tbgastos set nome=?, valor=? where idgastos=?";
                            pst = conexao.prepareStatement(sqo);
                            pst.setString(1, tbGastos.getModel().getValueAt(setar, 1).toString() + "(" + justificativa + ")");
                            pst.setString(2, new DecimalFormat("#,##0.00").format(Float.parseFloat(valor)).replace(",", "."));
                            pst.setString(3, id);
                            pst.executeUpdate();
                        } catch (java.lang.NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Valor aceita somente Numeros.");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e);

                        }

                    } else if (opcao.equals("D") == true || opcao.equals("d") == true) {
                        String sqo = "delete from tbgastos where idgastos=?";
                        pst = conexao.prepareStatement(sqo);
                        pst.setString(1, id);
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Clique no Ok e Aguarde.");

                        JOptionPane.showMessageDialog(null, "Concluido.");

                    }
                    instanciarTabela();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e);

                }
            }
        } catch (java.lang.NullPointerException e) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    private void Imprimir(){
        
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressao deste relatorio?", "Atençao", JOptionPane.YES_OPTION);
        
        if (confirma == JOptionPane.YES_OPTION) {
            try {
               // Container container = null;
                sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
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
                filtro.put("tipo1", tipo1);  
                filtro.put("tipo2", tipo2);
                filtro.put("tipo3", tipo3);
                filtro.put("tipo4", tipo4);
                filtro.put("data1", data1);
                filtro.put("data2", data2);
                filtro.put("total", x);
                filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");
                                
                JasperReport jreport = JasperCompileManager.compileReport("src/reports/Gastos.jrxml");
                
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
            } catch (Exception e) {
                                JOptionPane.showMessageDialog(null, "Verifique as Instancias de Relatorio na tela de configuraçoes.\nVerifique se a imagem do relarorio Existe");

            }
        }
    }

    public void limpar() {
        txtDescricao.setText(null);
        txtSangria.setText(null);
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        lblUsuario = new javax.swing.JLabel();
        pnPrincipalSangria = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        DaInicial = new com.toedter.calendar.JDateChooser();
        jPanel6 = new javax.swing.JPanel();
        DaFinal = new com.toedter.calendar.JDateChooser();
        btnPesquisar = new javax.swing.JToggleButton();
        scGastos = new javax.swing.JScrollPane();
        tbGastos = new javax.swing.JTable();
        rbTodos = new javax.swing.JRadioButton();
        rbCompras = new javax.swing.JRadioButton();
        rbSangria = new javax.swing.JRadioButton();
        rbFuncionarios = new javax.swing.JRadioButton();
        lblValorTotal = new javax.swing.JLabel();
        lblValorTotal1 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btnAdicionarSangria = new br.com.LeGnusERP.Swing.botaoArredondado();
        jPanel1 = new javax.swing.JPanel();
        txtSangria = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescricao = new javax.swing.JTextArea();

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
        jScrollPane2.setViewportView(tbAuxilio);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela de Gastos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        pnPrincipalSangria.setBackground(java.awt.SystemColor.control);
        pnPrincipalSangria.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Gastos Registrados", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Data Inicial", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        DaInicial.setBackground(java.awt.SystemColor.control);
        DaInicial.setDateFormatString("y-MM-dd");
        DaInicial.setFocusable(false);
        DaInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                DaInicialKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaInicial, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Data Final", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        DaFinal.setBackground(java.awt.SystemColor.control);
        DaFinal.setDateFormatString("y-MM-dd");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DaFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnPesquisar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btnPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/lupa.png"))); // NOI18N
        btnPesquisar.setSelected(true);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPesquisar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPesquisarActionPerformed(evt);
            }
        });

        tbGastos = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbGastos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbGastos.setFocusable(false);
        tbGastos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbGastosMouseClicked(evt);
            }
        });
        scGastos.setViewportView(tbGastos);

        buttonGroup1.add(rbTodos);
        rbTodos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        rbTodos.setSelected(true);
        rbTodos.setText("Todos");
        rbTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbTodosActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbCompras);
        rbCompras.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        rbCompras.setText("Compras");
        rbCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbComprasActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbSangria);
        rbSangria.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        rbSangria.setText("Sangria");
        rbSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSangriaActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbFuncionarios);
        rbFuncionarios.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        rbFuncionarios.setText("Funcionarios");
        rbFuncionarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFuncionariosActionPerformed(evt);
            }
        });

        lblValorTotal.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblValorTotal.setForeground(new java.awt.Color(153, 0, 0));
        lblValorTotal.setText("##");

        lblValorTotal1.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblValorTotal1.setText("Valor Total: R$");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblValorTotal1)
                        .addGap(0, 0, 0)
                        .addComponent(lblValorTotal))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(rbTodos)
                        .addGap(16, 16, 16)
                        .addComponent(rbCompras)
                        .addGap(16, 16, 16)
                        .addComponent(rbSangria)
                        .addGap(16, 16, 16)
                        .addComponent(rbFuncionarios)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(16, 16, 16))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(scGastos, javax.swing.GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
                .addGap(8, 8, 8))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnPesquisar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPesquisar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbTodos)
                    .addComponent(rbCompras)
                    .addComponent(rbSangria)
                    .addComponent(rbFuncionarios))
                .addGap(16, 16, 16)
                .addComponent(scGastos, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValorTotal)
                    .addComponent(lblValorTotal1))
                .addContainerGap())
        );

        jPanel8.setBackground(java.awt.SystemColor.control);
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(153, 153, 153)));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/ImpresoraIcon.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Sangria", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        btnAdicionarSangria.setForeground(new java.awt.Color(153, 0, 0));
        btnAdicionarSangria.setText("Adicionar Sangria");
        btnAdicionarSangria.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdicionarSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarSangriaActionPerformed(evt);
            }
        });

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Valor da Sangria", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Dialog", 1, 12))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSangria)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtSangria, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Descrição da Sangria", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtDescricao.setColumns(20);
        txtDescricao.setRows(5);
        jScrollPane1.setViewportView(txtDescricao);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnAdicionarSangria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnAdicionarSangria, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnPrincipalSangriaLayout = new javax.swing.GroupLayout(pnPrincipalSangria);
        pnPrincipalSangria.setLayout(pnPrincipalSangriaLayout);
        pnPrincipalSangriaLayout.setHorizontalGroup(
            pnPrincipalSangriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnPrincipalSangriaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnPrincipalSangriaLayout.setVerticalGroup(
            pnPrincipalSangriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnPrincipalSangriaLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnPrincipalSangria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnPrincipalSangria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1296, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarSangriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarSangriaActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAdicionarSangriaActionPerformed

    private void DaInicialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DaInicialKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_DaInicialKeyReleased

    private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPesquisarActionPerformed
        // TODO add your handling code here:
        validarData();
    }//GEN-LAST:event_btnPesquisarActionPerformed

    private void tbGastosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbGastosMouseClicked
        // TODO add your handling code here:
        Pagar();
    }//GEN-LAST:event_tbGastosMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Imprimir();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_formWindowOpened

    private void rbTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbTodosActionPerformed
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_rbTodosActionPerformed

    private void rbComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbComprasActionPerformed
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_rbComprasActionPerformed

    private void rbSangriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSangriaActionPerformed
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_rbSangriaActionPerformed

    private void rbFuncionariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFuncionariosActionPerformed
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_rbFuncionariosActionPerformed

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
            java.util.logging.Logger.getLogger(telaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(telaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(telaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(telaGastos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaGastos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DaFinal;
    private com.toedter.calendar.JDateChooser DaInicial;
    private br.com.LeGnusERP.Swing.botaoArredondado btnAdicionarSangria;
    private javax.swing.JToggleButton btnPesquisar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
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
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JLabel lblValorTotal;
    private javax.swing.JLabel lblValorTotal1;
    private javax.swing.JPanel pnPrincipalSangria;
    private javax.swing.JRadioButton rbCompras;
    private javax.swing.JRadioButton rbFuncionarios;
    private javax.swing.JRadioButton rbSangria;
    private javax.swing.JRadioButton rbTodos;
    private javax.swing.JScrollPane scGastos;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JTable tbGastos;
    private javax.swing.JTextArea txtDescricao;
    private javax.swing.JTextField txtSangria;
    // End of variables declaration//GEN-END:variables
}
