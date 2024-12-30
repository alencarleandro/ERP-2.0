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
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Ad3ln0r
 */
public class CadProduto extends javax.swing.JFrame {

    /**
     * Creates new form CadProduto
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    public CadProduto() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    private void setIcon() {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    public void InstanciarCombobox() {
        try {
            cbFornecedor.removeAllItems();
            cbFornecedor.addItem("Selecione");
            String sql = "select nome_fornecedor from tbfornecedor";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbFornecedor.addItem(rs.getString("nome_fornecedor"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void instanciarTabela() {
        String sql = "select idproduto as ID,codigo as Codigo, produto as Produto, valor_compra as Valor_de_Compra, valor_venda as Valor_de_Venda, fornecedor as Fornecedor, obs as Observações,estoque as Estoque,foto as Foto,tipoCodigo as Tipo_Codigo from tbprodutos";
        try {

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbProduto.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void limpar() {
        instanciarTabela();
        txtID.setText(null);
        txtCodigo.setText(null);
        txtCusto.setText("0.00");
        txtPreco.setText("0.00");
        txtDescricao.setText(null);
        txtPesquisa.setText(null);
        cbFornecedor.setSelectedItem(" ");
        cbTipoCodigo.setSelectedItem("ID Comum");
        taProduto.setText(null);
        btnAdicionar.setEnabled(true);
        btnAtualizar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnRemover.setEnabled(false);
        txtFoto.setText(null);
        btnImagem.setIcon(null);

    }

    private void pesquisar_cliente() {
        String sql = "select idproduto as ID,codigo as Codigo, produto as Produto, valor_compra as Valor_de_Compra, valor_venda as Valor_de_Venda, fornecedor as Fornecedor, obs as Observações,estoque as Estoque,foto as Foto,tipoCodigo as Tipo_Codigo from tbprodutos where produto like ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtPesquisa.getText() + "%");
            rs = pst.executeQuery();
            //A Linha Abaixo usa a Biblioteca rs2xml para preencher a tabela

            tbProduto.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void setar_campos() {
        try {
            int setar = tbProduto.getSelectedRow();
            txtID.setText(tbProduto.getModel().getValueAt(setar, 0).toString());
            if (tbProduto.getModel().getValueAt(setar, 1) == null) {
                txtCodigo.setText(null);
            } else {
                txtCodigo.setText(tbProduto.getModel().getValueAt(setar, 1).toString());
            }
            txtDescricao.setText(tbProduto.getModel().getValueAt(setar, 2).toString());
            double custo = Double.parseDouble(tbProduto.getModel().getValueAt(setar, 3).toString().replace(".", "")) / 100;
            double preco = Double.parseDouble(tbProduto.getModel().getValueAt(setar, 4).toString().replace(".", "")) / 100;
            txtCusto.setText(String.valueOf(custo));
            txtPreco.setText(String.valueOf(preco));
            if (tbProduto.getModel().getValueAt(setar, 5).toString() != null) {
                cbFornecedor.setSelectedItem(tbProduto.getModel().getValueAt(setar, 5).toString());
            }
            taProduto.setText(tbProduto.getModel().getValueAt(setar, 6).toString());
            cbEstoque.setSelectedItem(tbProduto.getModel().getValueAt(setar, 7).toString());
            if (tbProduto.getModel().getValueAt(setar, 8) != null) {
                txtFoto.setText(tbProduto.getModel().getValueAt(setar, 8).toString());
                InstanciarFoto();
            }
            cbTipoCodigo.setSelectedItem(tbProduto.getModel().getValueAt(setar, 9).toString());

            //A Linha Abaixo desabilita o botão adicionar
            txtPesquisa.setText(null);
            btnAdicionar.setEnabled(false);
            btnEditar.setEnabled(true);
            btnRemover.setEnabled(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void adicionar() {

        String sql = "insert into tbprodutos(produto,codigo,valor_compra,valor_venda,fornecedor,obs,estoque,quantidade,referencial_compra,referencial_venda,compra_x_venda,foto,tipoCodigo)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {

            pst = conexao.prepareStatement(sql);

            pst.setString(1, txtDescricao.getText());

            if (txtCodigo.getText().isEmpty() == true) {
                pst.setString(2, null);
                pst.setString(13, "ID Comum");
            } else {
                if (cbTipoCodigo.getSelectedItem().toString().equals("ID Comum")) {
                    pst.setString(2, txtCodigo.getText());
                    pst.setString(13, cbTipoCodigo.getSelectedItem().toString());
                } else {
                    if (txtCodigo.getText().length() == 7) {
                        pst.setString(2, txtCodigo.getText());
                        pst.setString(13, cbTipoCodigo.getSelectedItem().toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Para EAN-13, o codigo deve ter 7 digitos. \n(O produto será salvo como Código Comum.)");
                        pst.setString(2, txtCodigo.getText());
                        pst.setString(13, "ID Comum");
                    }
                }
            }

            pst.setString(3, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtCusto.getText().replace(",", "."))).replace(",", "."));
            pst.setString(4, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtPreco.getText().replace(",", "."))).replace(",", "."));

            pst.setString(5, cbFornecedor.getSelectedItem().toString());

            pst.setString(6, taProduto.getText());
            pst.setString(7, cbEstoque.getSelectedItem().toString());
            pst.setString(8, "0.00");
            pst.setString(9, "0.00");
            pst.setString(10, "0.00");
            pst.setString(11, "0.00");
            pst.setString(12, txtFoto.getText());

            if ((txtDescricao.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");

            } else if ((Double.parseDouble(txtCusto.getText().replace(",", ".")) <= 0) || (Double.parseDouble(txtPreco.getText().replace(",", ".")) <= 0)) {
                JOptionPane.showMessageDialog(null, "Valor de Compra ou Valor de Venda Deve ser maior que 0.");
            } else {

                int adicionado = pst.executeUpdate();

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso");
                    limpar();
                }
            }
        } catch (com.mysql.cj.jdbc.exceptions.MysqlDataTruncation n) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar Código de Barras, maximo de 18 caracteres");
            limpar();

        } catch (java.lang.NumberFormatException c) {

            JOptionPane.showMessageDialog(null, "Campo Codigo so aceita Numeros. \n"
                    + "Valor de Compra e Valor de Venda deve ser Salvo no Formato 0.00 .");

            limpar();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Esse codigo de barras já existe.");
            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void alterar() {        
        try {
            String sql = "update tbprodutos set produto=?,"
                + "codigo=?,"
                + "valor_compra=?,"
                + "valor_venda=?,"
                + "fornecedor=?,"
                + "obs=?,"
                + "estoque=?,"
                + "foto=?,"
                + "tipoCodigo=?"
                + " where idproduto=?";

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDescricao.getText());

            if (txtCodigo.getText().isEmpty()) {
                pst.setString(2, null);
                pst.setString(9, "ID Comum");
            } else {
                if (cbTipoCodigo.getSelectedItem().toString().equals("ID Comum")) {
                    pst.setString(2, txtCodigo.getText());
                    pst.setString(9, cbTipoCodigo.getSelectedItem().toString());
                } else {
                    if (txtCodigo.getText().length() == 7) {
                        pst.setString(2, txtCodigo.getText());
                        pst.setString(9, cbTipoCodigo.getSelectedItem().toString());
                    } else {
                        JOptionPane.showMessageDialog(null, "Para EAN-13, o código deve ter 7 digitos. \n(O produto será salvo como Código Comum.)");
                        pst.setString(2, txtCodigo.getText());
                        pst.setString(9, "ID Comum");
                    }
                }
            }

            pst.setString(3, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtCusto.getText().replace(",", "."))).replace(",", "."));
            pst.setString(4, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtPreco.getText().replace(",", "."))).replace(",", "."));

            pst.setString(5, cbFornecedor.getSelectedItem().toString());

            pst.setString(6, taProduto.getText());
            pst.setString(7, cbEstoque.getSelectedItem().toString());
            pst.setString(8, txtFoto.getText());
            pst.setString(10, txtID.getText());

            if ((txtDescricao.getText().isEmpty()) || (txtCusto.getText().isEmpty()) || (txtPreco.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else if ((Double.parseDouble(txtCusto.getText().replace(",", ".")) <= 0) || (Double.parseDouble(txtPreco.getText().replace(",", ".")) <= 0)) {
                JOptionPane.showMessageDialog(null, "Valor de Compra ou Valor de Venda Deve ser maior que 0.");
            } else {

                //A linha abaixo altera os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do produto alterado(s) com sucesso");
                    limpar();

                }
            }
        } catch (com.mysql.cj.jdbc.exceptions.MysqlDataTruncation e) {
            JOptionPane.showMessageDialog(null, "Código de barras deve ter no maximo 18 caracteres.");
            limpar();
        } catch (java.lang.NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Código de barras, Valor de Compra e Valor de Venda não suportam letras. \n"
                    + "Valor de Compra e Valor de Venda deve ser Salvo no Formato 0.00 .");
            limpar();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(null, "Esse codigo de barras já existe.");
            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este produto?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbprodutos where idproduto=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtID.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");
                    tirarId();
                    criarId();
                    JOptionPane.showMessageDialog(null, "Produto removido com sucesso");
                    limpar();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                limpar();
            }
        }
    }

    private void tirarId() {

        String sql = "alter table tbprodutos drop idproduto";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    public void InstanciarFoto() {
        try {
            btnImagem.setIcon(new javax.swing.ImageIcon(txtFoto.getText()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "foto       " + e);
            limpar();
        }
    }

    public void buscarFoto() {
        try {
            JFileChooser arquivo = new JFileChooser();
            arquivo.setDialogTitle("SELECIONE UMA FOTO");
            arquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int op = arquivo.showOpenDialog(this);
            if (op == JFileChooser.APPROVE_OPTION) {
                File file = new File("");
                file = arquivo.getSelectedFile();
                String fileName = file.getAbsolutePath();
                txtFoto.setText(fileName);
            }
            InstanciarFoto();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "foto       " + e);
            limpar();
        }
    }

    private void criarId() {
        String sql = "alter table tbprodutos add idproduto MEDIUMINT NOT NULL AUTO_INCREMENT Primary key";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
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

        txtID = new javax.swing.JTextField();
        lblUsuario = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtDescricao = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        cbEstoque = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        cbFornecedor = new javax.swing.JComboBox<>();
        btnFornecedor = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        cbTipoCodigo = new javax.swing.JComboBox<>();
        jPanel5 = new javax.swing.JPanel();
        txtCusto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtPreco = new javax.swing.JTextField();
        pnOBS = new javax.swing.JPanel();
        scProduto = new javax.swing.JScrollPane();
        taProduto = new javax.swing.JTextArea();
        btnAdicionar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JButton();
        pnProduto = new javax.swing.JPanel();
        scTbProduto = new javax.swing.JScrollPane();
        tbProduto = new javax.swing.JTable();
        lblPesquisa = new javax.swing.JLabel();
        txtPesquisa = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        btnImagem = new javax.swing.JButton();
        txtFoto = new javax.swing.JTextField();
        btnFoto = new javax.swing.JToggleButton();
        lblCamposObrigatorios = new javax.swing.JLabel();

        txtID.setEnabled(false);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela Cadastro de Produtos");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel11.setBackground(java.awt.SystemColor.control);
        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Produto", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescricao)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Estoque", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbEstoque.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Com controle de estoque.", "Sem controle de estoque." }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbEstoque, 0, 258, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Fornecedor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFornecedorActionPerformed(evt);
            }
        });

        btnFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/plus.png"))); // NOI18N
        btnFornecedor.setBorderPainted(false);
        btnFornecedor.setContentAreaFilled(false);
        btnFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFornecedorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(cbFornecedor, 0, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Codigo de Barras", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jPanel10.setBackground(java.awt.SystemColor.control);
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Tipo de Codigo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbTipoCodigo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        cbTipoCodigo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Comum", "EAN-13 com Valor", "EAN-13 peso" }));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbTipoCodigo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbTipoCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Valor Compra(R$)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtCusto.setText("0.00");
        txtCusto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCusto, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Valor Venda(R$)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtPreco.setText("0.00");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPreco, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pnOBS.setBackground(java.awt.SystemColor.control);
        pnOBS.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Observações", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        taProduto.setColumns(20);
        taProduto.setRows(5);
        scProduto.setViewportView(taProduto);

        javax.swing.GroupLayout pnOBSLayout = new javax.swing.GroupLayout(pnOBS);
        pnOBS.setLayout(pnOBSLayout);
        pnOBSLayout.setHorizontalGroup(
            pnOBSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scProduto, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        pnOBSLayout.setVerticalGroup(
            pnOBSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scProduto)
        );

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeAdicionar-removebg-preview.png"))); // NOI18N
        btnAdicionar.setContentAreaFilled(false);
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeEditar-removebg-preview.png"))); // NOI18N
        btnEditar.setContentAreaFilled(false);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEditar.setEnabled(false);
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRemover-removebg-preview.png"))); // NOI18N
        btnRemover.setContentAreaFilled(false);
        btnRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRemover.setEnabled(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRestart-removebg-preview.png"))); // NOI18N
        btnAtualizar.setContentAreaFilled(false);
        btnAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        pnProduto.setBackground(java.awt.SystemColor.control);
        pnProduto.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(153, 153, 153)));

        tbProduto = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbProduto.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tbProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbProduto.setFocusable(false);
        tbProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProdutoMouseClicked(evt);
            }
        });
        scTbProduto.setViewportView(tbProduto);

        lblPesquisa.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblPesquisa.setText("Pesquisa:");

        txtPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPesquisaActionPerformed(evt);
            }
        });
        txtPesquisa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPesquisaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnProdutoLayout = new javax.swing.GroupLayout(pnProduto);
        pnProduto.setLayout(pnProdutoLayout);
        pnProdutoLayout.setHorizontalGroup(
            pnProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnProdutoLayout.createSequentialGroup()
                        .addComponent(lblPesquisa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPesquisa))
                    .addComponent(scTbProduto, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnProdutoLayout.setVerticalGroup(
            pnProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnProdutoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(pnProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPesquisa)
                    .addComponent(txtPesquisa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scTbProduto)
                .addContainerGap())
        );

        jPanel9.setBackground(java.awt.SystemColor.control);
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Caminho da Imagem", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        btnImagem.setBackground(java.awt.SystemColor.control);
        btnImagem.setContentAreaFilled(false);
        jScrollPane1.setViewportView(btnImagem);

        txtFoto.setEnabled(false);
        txtFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFotoActionPerformed(evt);
            }
        });

        btnFoto.setBackground(new java.awt.Color(204, 204, 204));
        btnFoto.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnFoto.setText("Buscar");
        btnFoto.setBorderPainted(false);
        btnFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(txtFoto)
                        .addGap(12, 12, 12)
                        .addComponent(btnFoto)))
                .addGap(0, 0, 0))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFoto)
                    .addComponent(btnFoto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        lblCamposObrigatorios.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblCamposObrigatorios.setText("* Campos Obrigatorios");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(pnProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnOBS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionar)
                        .addGap(8, 8, 8)
                        .addComponent(btnEditar)
                        .addGap(8, 8, 8)
                        .addComponent(btnRemover)
                        .addGap(8, 8, 8)
                        .addComponent(btnAtualizar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCamposObrigatorios)
                        .addGap(16, 16, 16))))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lblCamposObrigatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(16, 16, 16)
                .addComponent(pnOBS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemover)
                    .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1296, 728));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFornecedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbFornecedorActionPerformed

    private void tbProdutoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdutoMouseClicked
        // TODO add your handling code here:
        setar_campos();
    }//GEN-LAST:event_tbProdutoMouseClicked

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        alterar();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // TODO add your handling code here:
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void txtPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPesquisaActionPerformed

    private void txtPesquisaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPesquisaKeyReleased
        // TODO add your handling code here:
        pesquisar_cliente();
    }//GEN-LAST:event_txtPesquisaKeyReleased

    private void txtCustoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustoActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void btnFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFornecedorActionPerformed
        // TODO add your handling code here:
        CadFornecedor fornecedor = new CadFornecedor();
        fornecedor.setVisible(true);
        fornecedor.lblUsuario.setText("Produto");
    }//GEN-LAST:event_btnFornecedorActionPerformed

    private void txtFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFotoActionPerformed
        // TODO add your handling code here:
        txtFoto.setText(null);
    }//GEN-LAST:event_txtFotoActionPerformed

    private void btnFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFotoActionPerformed
        // TODO add your handling code here:
        buscarFoto();
    }//GEN-LAST:event_btnFotoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        limpar();
        InstanciarCombobox();
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(CadProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadProduto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JToggleButton btnFornecedor;
    private javax.swing.JToggleButton btnFoto;
    private javax.swing.JButton btnImagem;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox<String> cbEstoque;
    private javax.swing.JComboBox<String> cbFornecedor;
    private javax.swing.JComboBox<String> cbTipoCodigo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCamposObrigatorios;
    private javax.swing.JLabel lblPesquisa;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel pnOBS;
    private javax.swing.JPanel pnProduto;
    private javax.swing.JScrollPane scProduto;
    private javax.swing.JScrollPane scTbProduto;
    private javax.swing.JTextArea taProduto;
    private javax.swing.JTable tbProduto;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtCusto;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtFoto;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtPesquisa;
    private javax.swing.JTextField txtPreco;
    // End of variables declaration//GEN-END:variables
}
