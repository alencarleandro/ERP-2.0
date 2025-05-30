/*
 * The MIT License
 *
 * Copyright 2022 Ad3ln0r.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * Permission is hereby granted, free of charge, to any person obtaining a copy
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JRViewer;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.title.Title;

/**
 *
 * @author Ad3ln0r
 */
public class CadOS extends javax.swing.JFrame {

    /**
     * Creates new form CadOS
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private String tipo;

    public CadOS() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    public void InstanciarCombobox() {
        try {
            cbFuncionario.removeAllItems();
            cbFuncionario.addItem("Selecione");
            String sql = "select funcionario from tbFuncionarios where tipo != 'Vendedor'";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbFuncionario.addItem(rs.getString("funcionario"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void fecharOS() {

        String Perfil;
        limpar();
        try {
            if (lblUsuario.getText().equals("")) {
                TelaPrincipal principal = new TelaPrincipal();
                principal.setVisible(true);
                this.dispose();
            } else {
                String sqy = "select perfil from tbusuarios where usuario=?";
                pst = conexao.prepareStatement(sqy);
                pst.setString(1, lblUsuario.getText());
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
                Perfil = tbAuxilio.getModel().getValueAt(0, 0).toString();
                if (Perfil.equals("Usuario") == true) {

                    TelaLimitada limitada = new TelaLimitada();
                    limitada.setVisible(true);
                    TelaLimitada.btnPDV.setEnabled(false);
                    TelaLimitada.btnCadOS.setEnabled(false);
                    TelaLimitada.btnCadServiço.setEnabled(false);
                    TelaLimitada.lblNome.setText(lblUsuario.getText());
                    this.dispose();
                    conexao.close();

                } else {
                    TelaPrincipal principal = new TelaPrincipal();
                    principal.setVisible(true);
                    principal.lblUsuario.setText(lblUsuario.getText());
                    this.dispose();

                }
            }

        } catch (java.lang.ArrayIndexOutOfBoundsException e) {

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void InstanciarTabelaCliente() {
        String sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where idcli > 1";
        try {
            limpar();
            txtCliPesquisar.setText(null);
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));

            tipo = "Cliente";
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void pesquisar_cliente() {
        String sql = "";

        if (rbPorNome.isSelected()) {
            sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where nomecli like ? and idcli > 1";
        } else {
            sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where nomecli like ? and idcli > 1";
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void instanciarTabelaOrcamento() {
        String sql = "select os as Numero_da_Ordem_de_Serviço, data_os as Emição, tipo as Tipo, situacao as Situação, previsao_entreg_os as Previsão_de_Entrega, equipamento as Equipamento, defeito as Defeito, servico as Serviço, funcionario as Funcionario, valor as Valor, cliente as Cliente from tbos ";
        try {
            limpar();
            txtCliPesquisar.setText(null);
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
            tipo = "Orcamento_OS";
            cbFiltroSituacao.setSelectedIndex(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void instanciarTabelaMaquinas(int idcli) {
        try {
            String sql = "select nome, ultimaEntrada, cor, especie_marca from tbsubclientes where tipo='Maquina' and referencia=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, String.valueOf(idcli));
            rs = pst.executeQuery();
            tbEquipamentos.setModel(DbUtils.resultSetToTableModel(rs));

            for (int i = 0; i < tbEquipamentos.getModel().getRowCount(); i++) {
                cbEquipamento.addItem(tbEquipamentos.getModel().getValueAt(i, 0).toString());
                cbDataEquipamentos.addItem(tbEquipamentos.getModel().getValueAt(i, 1).toString());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void instanciarTabelaMaquinasAux(int idcli) {
        try {
            String sql = "select nome,ultimaEntrada,cor,especie_marca,usuario_que_deu_entrada,garantia,idsub from tbsubclientes where tipo='Maquina' and referencia=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, String.valueOf(idcli));
            rs = pst.executeQuery();
            tbEquipamentos.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void pesquisar_orcamento() {

        String sql = "";

        if (rbPorNome.isSelected()) {
            sql = "select os as Numero_da_Ordem_de_Serviço, data_os as Emição, tipo as Tipo, situacao as Situação, previsao_entreg_os as Previsão_de_Entrega, equipamento as Equipamento, defeito as Defeito, servico as Serviço, funcionario as Funcionario, valor as Valor, cliente as Cliente from tbos where cliente like ? ";
        } else {
            sql = "select os as Numero_da_Ordem_de_Serviço, data_os as Emição, tipo as Tipo, situacao as Situação, previsao_entreg_os as Previsão_de_Entrega, equipamento as Equipamento, defeito as Defeito, servico as Serviço, funcionario as Funcionario, valor as Valor, cliente as Cliente from tbos where servico like ? ";
        }

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void pesquisar() {
        if (tipo.equals("Cliente") == true) {
            pesquisar_cliente();
        } else {
            pesquisar_orcamento();
        }
    }

    private void setar_campos() {
        if (tipo.equals("Cliente") == true) {

            int setar = tbPrincipal.getSelectedRow();
            txtCliente.setText(tbPrincipal.getModel().getValueAt(setar, 1).toString());
            txtOsDef.setEnabled(true);
            cbEquipamento.setEnabled(true);
            txtOsServ.setEnabled(true);
            cbFuncionario.setEnabled(true);
            txtOsValor.setEnabled(true);
            cbTipo.setEnabled(true);
            cboOsSit.setEnabled(true);
            dtPrevisao.setEnabled(true);
            btnAdicionar.setEnabled(true);
            btnResetar.setEnabled(true);
            btnRemover.setEnabled(false);
            btnImprimir.setEnabled(false);
            btnEditar.setEnabled(false);

            cbEquipamento.removeAllItems();

            instanciarTabelaMaquinas(Integer.parseInt(tbPrincipal.getModel().getValueAt(setar, 0).toString()));

            if (tbEquipamentos.getModel().getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Cliente com nenhum equipamento registrado.");
                limpar();
            }

        } else {

            try {

                int setar = tbPrincipal.getSelectedRow();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                txtOs.setText(tbPrincipal.getModel().getValueAt(setar, 0).toString());
                cbTipo.setSelectedItem(tbPrincipal.getModel().getValueAt(setar, 2).toString());
                cboOsSit.setSelectedItem(tbPrincipal.getModel().getValueAt(setar, 3).toString());
                dtPrevisao.setDate(df.parse(tbPrincipal.getModel().getValueAt(setar, 4).toString()));

                cbEquipamento.removeAllItems();
                cbEquipamento.addItem(tbPrincipal.getModel().getValueAt(setar, 5).toString());
                cbEquipamento.setSelectedIndex(0);

                txtOsDef.setText(tbPrincipal.getModel().getValueAt(setar, 6).toString());
                txtOsServ.setText(tbPrincipal.getModel().getValueAt(setar, 7).toString());
                cbFuncionario.setSelectedItem(tbPrincipal.getModel().getValueAt(setar, 8).toString());
                double valor = Double.parseDouble(tbPrincipal.getModel().getValueAt(setar, 9).toString().replace(".", "")) / 100;
                txtOsValor.setText(String.valueOf(valor));
                txtCliente.setText(tbPrincipal.getModel().getValueAt(setar, 10).toString());
                txtOsDef.setEnabled(true);
                cbEquipamento.setEnabled(false);
                txtOsServ.setEnabled(true);
                cbFuncionario.setEnabled(true);
                txtOsValor.setEnabled(true);
                cbTipo.setEnabled(true);
                cboOsSit.setEnabled(true);
                dtPrevisao.setEnabled(true);
                btnAdicionar.setEnabled(false);
                btnResetar.setEnabled(true);
                btnRemover.setEnabled(true);
                btnImprimir.setEnabled(true);
                btnEditar.setEnabled(true);

                String sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where idcli > 1";

                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                tbClientes.setModel(DbUtils.resultSetToTableModel(rs));
                SimpleDateFormat dfo = new SimpleDateFormat("yyyy-MM-dd");

                for (int i = 0; i < tbClientes.getModel().getRowCount(); i++) {
                    if (tbClientes.getModel().getValueAt(i, 1).toString().equals(txtCliente.getText())) {
                        instanciarTabelaMaquinasAux(Integer.parseInt(tbClientes.getModel().getValueAt(i, 0).toString()));
                        for (int j = 0; j < tbEquipamentos.getModel().getRowCount(); j++) {
                            if (cbEquipamento.getSelectedItem().toString().equals(tbEquipamentos.getModel().getValueAt(j, 0).toString())) {                           
                                if(tbEquipamentos.getModel().getValueAt(j, 2) != null){
                                    txtCorEquipamento.setText(tbEquipamentos.getModel().getValueAt(j, 2).toString());
                                }else{
                                    txtCorEquipamento.setText("Indefinida");
                                }
                                if(tbEquipamentos.getModel().getValueAt(j, 3) != null){
                                    txtMarcaEquipamento.setText(tbEquipamentos.getModel().getValueAt(j, 3).toString());
                                }else{
                                    txtMarcaEquipamento.setText("Indefinida");
                                }
                                if(tbEquipamentos.getModel().getValueAt(j, 4) != null){
                                    txtUsuarioEntrada.setText(tbEquipamentos.getModel().getValueAt(j, 4).toString());
                                }else{
                                    txtUsuarioEntrada.setText("Administrador");
                                }
                                dtGarantia.setEnabled(true);
                                if (tbEquipamentos.getModel().getValueAt(j, 5) != null) {
                                    dtGarantia.setDate(dfo.parse(tbEquipamentos.getModel().getValueAt(j, 5).toString()));
                                } else {
                                    dtGarantia.setDate(null);
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                limpar();

            }
        }
    }

    private void emitir_os() {

        try {
            int confirma = 0;
            if ((txtCliente.getText().isEmpty()) || (cbEquipamento.getSelectedItem().toString().isEmpty()) || (txtOsDef.getText().isEmpty()) || (txtOsServ.getText().isEmpty()) || (txtOsValor.getText().isEmpty()) || dtPrevisao.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
                confirma = 1;
            } else if (cbFuncionario.getSelectedItem().toString().equals("Selecione") == true) {
                JOptionPane.showMessageDialog(null, "Adicione um Funcionario.");
            } else if ((Double.parseDouble(txtOsValor.getText().replace(",", ".")) <= 0)) {
                JOptionPane.showMessageDialog(null, "Adicione um valor maior que 0.");
            } else if (confirma == 0) {
                Date d = dtPrevisao.getDate();
                java.sql.Date dSql = new java.sql.Date(d.getTime());
                df.format(dSql);

                String sql = "insert into tbos(tipo,situacao,previsao_entreg_os,equipamento,defeito,servico,funcionario,valor,cliente,data_os) values(?,?,?,?,?,?,?,?,?,?)";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, cbTipo.getSelectedItem().toString());
                pst.setString(2, cboOsSit.getSelectedItem().toString());
                pst.setDate(3, dSql);
                pst.setString(4, cbEquipamento.getSelectedItem().toString());
                pst.setString(5, txtOsDef.getText());
                pst.setString(6, txtOsServ.getText());
                pst.setString(7, cbFuncionario.getSelectedItem().toString());
                pst.setString(8, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtOsValor.getText().replace(",", "."))).replace(",", "."));
                pst.setString(9, txtCliente.getText());
                pst.setTimestamp(10, Timestamp.valueOf(cbDataEquipamentos.getItemAt(cbEquipamento.getSelectedIndex())));

                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS emitida com sucesso.");
                    limpar();

                }
            }
        } catch (java.lang.NumberFormatException c) {
            JOptionPane.showMessageDialog(null, "Campo Valor só suporta Numeros.");
            JOptionPane.showMessageDialog(null, "Campo Valor Deve ser salvo no formato 0.00 .");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void alterar_os() {

        try {
            int confirma = 0;
            if ((txtCliente.getText().isEmpty()) || (cbEquipamento.getSelectedItem().toString().isEmpty()) || (txtOsDef.getText().isEmpty()) || (txtOsServ.getText().isEmpty()) || (txtOsValor.getText().isEmpty()) || dtPrevisao.getDate() == null) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios.");
                limpar();
                confirma = 1;
            } else if (cbFuncionario.getSelectedItem().toString().equals("Selecione") == true) {
                JOptionPane.showMessageDialog(null, "Adicione um Funcionario.");
                limpar();
            } else if ((Double.parseDouble(txtOsValor.getText().replace(",", ".")) <= 0)) {
                JOptionPane.showMessageDialog(null, "Adicione um valor maior que 0.");
                limpar();
            } else if (confirma == 0) {
                Date d = dtPrevisao.getDate();
                java.sql.Date dSql = new java.sql.Date(d.getTime());
                df.format(dSql);

                String sql = "update tbos set tipo=?,situacao=?,previsao_entreg_os=?,equipamento=?,defeito=?,servico=?,funcionario=?,valor=? where os=?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, cbTipo.getSelectedItem().toString());
                pst.setString(2, cboOsSit.getSelectedItem().toString());
                pst.setDate(3, dSql);
                pst.setString(4, cbEquipamento.getSelectedItem().toString());
                pst.setString(5, txtOsDef.getText());
                pst.setString(6, txtOsServ.getText());
                pst.setString(7, cbFuncionario.getSelectedItem().toString());
                pst.setString(8, new DecimalFormat("#,##0.00").format(Double.parseDouble(txtOsValor.getText().replace(",", "."))).replace(",", "."));
                pst.setString(9, txtOs.getText());

                int adicionado = pst.executeUpdate();

                sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where idcli > 1";

                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                tbClientes.setModel(DbUtils.resultSetToTableModel(rs));
                SimpleDateFormat dfo = new SimpleDateFormat("yyyy-MM-dd");

                for (int i = 0; i < tbClientes.getModel().getRowCount(); i++) {
                    if (tbClientes.getModel().getValueAt(i, 1).toString().equals(txtCliente.getText())) {
                        instanciarTabelaMaquinasAux(Integer.parseInt(tbClientes.getModel().getValueAt(i, 0).toString()));
                        for (int j = 0; j < tbEquipamentos.getModel().getRowCount(); j++) {
                            if (cbEquipamento.getSelectedItem().toString().equals(tbEquipamentos.getModel().getValueAt(j, 0).toString())) {

                                DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                int id = Integer.parseInt(tbEquipamentos.getModel().getValueAt(j, 6).toString());

                                sql = "UPDATE tbsubclientes SET garantia=? WHERE idsub=?";

                                pst = conexao.prepareStatement(sql);

                                if (dtGarantia.getDate() != null) {
                                    pst.setTimestamp(1, Timestamp.valueOf(df1.format(dtGarantia.getDate())));
                                } else {
                                    pst.setTimestamp(1, null);
                                }
                                pst.setInt(2, id);
                                pst.executeUpdate();

                            }
                        }
                    }
                }

                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "OS alterada com sucesso.");
                    limpar();

                }
            }
        } catch (java.lang.NumberFormatException c) {
            JOptionPane.showMessageDialog(null, "Campo Valor só suporta Numeros.");
            JOptionPane.showMessageDialog(null, "Campo Valor Deve ser salvo no formato 0.00 .");
            limpar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    private void excluir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir esta OS?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbos where os=?";
            try {
                pst = conexao.prepareStatement(sql);
                pst.setString(1, txtOs.getText());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");
                    tirarId();
                    criarId();
                    JOptionPane.showMessageDialog(null, "OS excluida com sucesso.");
                    limpar();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                limpar();

            }

        }
    }

    private void tirarId() {

        String sql = "alter table tbos drop os";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

    }

    private void criarId() {
        String sql = "alter table tbos add os MEDIUMINT NOT NULL AUTO_INCREMENT Primary key";
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void imprimir_os() {
        int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressao desta OS?", "Atençao", JOptionPane.YES_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            try {

                HashMap filtro = new HashMap();
                filtro.put("os", Integer.parseInt(txtOs.getText()));
                String sql = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                pst = conexao.prepareStatement(sql);
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));

                filtro.put("empresa", tbAuxilio.getModel().getValueAt(0, 0).toString());
                filtro.put("nome", tbAuxilio.getModel().getValueAt(0, 1).toString());
                filtro.put("email", tbAuxilio.getModel().getValueAt(0, 2).toString());
                filtro.put("descricao", tbAuxilio.getModel().getValueAt(0, 3).toString());
                filtro.put("obs", tbAuxilio.getModel().getValueAt(0, 4).toString());
                filtro.put("numero", tbAuxilio.getModel().getValueAt(0, 5).toString());
                filtro.put("imagem", pegarLogo());
                filtro.put("cliente", txtCliente.getText());
                filtro.put("Bandeira", "src/br/com/LeGnusERP/icones/bandeira.PNG");
                filtro.put("Background", "src/br/com/LeGnusERP/icones/papelEnvelhecidoMaisClaro.PNG");

                JasperReport jreport = JasperCompileManager.compileReport("src/reports/RelOS.jrxml");

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
                JOptionPane.showMessageDialog(null, e);
                limpar();

            }
        }
    }

    private void limpar() {
        txtCliId.setText(null);
        txtCliPesquisar.setText(null);
        txtCliente.setText(null);
        txtOs.setText(null);
        txtOsDef.setText(null);
        txtCorEquipamento.setText("");
        txtMarcaEquipamento.setText("");
        cbEquipamento.removeAllItems();
        txtOsServ.setText(null);

        txtUsuarioEntrada.setText("");

        txtOsValor.setText("0.00");
        dtPrevisao.setDate(null);
        dtGarantia.setEnabled(false);
        dtGarantia.setDate(null);
        txtCliId.setEnabled(false);
        txtOsDef.setEnabled(false);
        cbEquipamento.setEnabled(false);
        txtOsServ.setEnabled(false);
        cbFuncionario.setEnabled(false);
        txtOsValor.setEnabled(false);
        cbTipo.setEnabled(false);
        cboOsSit.setEnabled(false);
        dtPrevisao.setEnabled(false);
        btnAdicionar.setEnabled(false);
        btnResetar.setEnabled(false);
        btnRemover.setEnabled(false);
        btnImprimir.setEnabled(false);

        btnEditar.setEnabled(false);

        ((DefaultTableModel) tbPrincipal.getModel()).setRowCount(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtCliId = new javax.swing.JTextField();
        txtOs = new javax.swing.JTextField();
        grupoTbPrincipal = new javax.swing.ButtonGroup();
        lblUsuario = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        meioDeBusca = new javax.swing.ButtonGroup();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbEquipamentos = new javax.swing.JTable();
        cbDataEquipamentos = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        lblCamposObrigatorios = new javax.swing.JLabel();
        btnImprimir = new javax.swing.JButton();
        btnRemover = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnResetar = new javax.swing.JButton();
        btnCliente = new javax.swing.JToggleButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtOsDef = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOsServ = new javax.swing.JTextArea();
        jPanel8 = new javax.swing.JPanel();
        cbFuncionario = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        cbTipo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        cboOsSit = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        dtPrevisao = new com.toedter.calendar.JDateChooser();
        jPanel9 = new javax.swing.JPanel();
        txtOsValor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        cbEquipamento = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        txtCliente = new javax.swing.JTextField();
        pnTbPrincipal = new javax.swing.JPanel();
        scTbPrincipal = new javax.swing.JScrollPane();
        tbPrincipal = new javax.swing.JTable();
        lblPesquisar = new javax.swing.JLabel();
        txtCliPesquisar = new javax.swing.JTextField();
        rbClientes = new javax.swing.JRadioButton();
        rbOS_Orcamento = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        rbPorServico = new javax.swing.JRadioButton();
        rbPorNome = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        cbFiltroSituacao = new javax.swing.JComboBox<>();
        jPanel12 = new javax.swing.JPanel();
        txtMarcaEquipamento = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        txtCorEquipamento = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        txtUsuarioEntrada = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        dtGarantia = new com.toedter.calendar.JDateChooser();

        txtCliId.setEditable(false);

        txtOs.setEditable(false);

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
        jScrollPane1.setViewportView(tbAuxilio);

        tbEquipamentos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tbEquipamentos);

        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane5.setViewportView(tbClientes);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela Cadastro de OS");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel10.setBackground(java.awt.SystemColor.control);
        jPanel10.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel11.setBackground(java.awt.SystemColor.control);
        jPanel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        lblCamposObrigatorios.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblCamposObrigatorios.setText("* Campos Obrigatorios");

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/ImpresoraIcon.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir");
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnImprimir.setEnabled(false);
        btnImprimir.setPreferredSize(new java.awt.Dimension(80, 80));
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRemover-removebg-preview.png"))); // NOI18N
        btnRemover.setToolTipText("Deletar");
        btnRemover.setContentAreaFilled(false);
        btnRemover.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnRemover.setEnabled(false);
        btnRemover.setPreferredSize(new java.awt.Dimension(80, 80));
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeEditar-removebg-preview.png"))); // NOI18N
        btnEditar.setToolTipText("Pesquisar");
        btnEditar.setContentAreaFilled(false);
        btnEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnEditar.setEnabled(false);
        btnEditar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeAdicionar-removebg-preview.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar");
        btnAdicionar.setContentAreaFilled(false);
        btnAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAdicionar.setEnabled(false);
        btnAdicionar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnResetar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/iconeRestart-removebg-preview.png"))); // NOI18N
        btnResetar.setToolTipText("Editar");
        btnResetar.setContentAreaFilled(false);
        btnResetar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnResetar.setEnabled(false);
        btnResetar.setPreferredSize(new java.awt.Dimension(80, 80));
        btnResetar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetarActionPerformed(evt);
            }
        });

        btnCliente.setBackground(new java.awt.Color(0, 0, 0));
        btnCliente.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        btnCliente.setForeground(new java.awt.Color(255, 255, 255));
        btnCliente.setText("Cadastrar Cliente");
        btnCliente.setBorderPainted(false);
        btnCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClienteActionPerformed(evt);
            }
        });

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Detalhamento Defeito/Problema", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtOsDef.setColumns(20);
        txtOsDef.setRows(5);
        txtOsDef.setEnabled(false);
        jScrollPane3.setViewportView(txtOsDef);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE))
        );

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Detalhamento Ordem de Serviço", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtOsServ.setColumns(20);
        txtOsServ.setRows(5);
        txtOsServ.setEnabled(false);
        jScrollPane2.setViewportView(txtOsServ);

        jPanel8.setBackground(java.awt.SystemColor.control);
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Funcionario", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbFuncionario.setEnabled(false);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbFuncionario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Tipo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Orçamento", "Ordem de Serviço" }));
        cbTipo.setEnabled(false);
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbTipo, 0, 118, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Situação", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cboOsSit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Na bancada", "Pronto", "Entrega OK", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando peças", "Abandonado pelo cliente", "Retornou" }));
        cboOsSit.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboOsSit, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cboOsSit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Previsão Entrega", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        dtPrevisao.setBackground(java.awt.SystemColor.control);
        dtPrevisao.setDateFormatString("yyyy-MM-dd");
        dtPrevisao.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dtPrevisao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(dtPrevisao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel9.setBackground(java.awt.SystemColor.control);
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "*Valor Total(R$)", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtOsValor.setText("0.00");
        txtOsValor.setEnabled(false);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtOsValor, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtOsValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(8, 8, 8))
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Equipamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbEquipamento.setEnabled(false);
        cbEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEquipamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(cbEquipamento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(cbEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Cliente Selecionado", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtCliente.setEnabled(false);
        txtCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCliente)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
        );

        pnTbPrincipal.setBackground(java.awt.SystemColor.control);
        pnTbPrincipal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(153, 153, 153)));

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
        scTbPrincipal.setViewportView(tbPrincipal);

        lblPesquisar.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblPesquisar.setText("Pesquisar:");

        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        rbClientes.setBackground(java.awt.SystemColor.control);
        grupoTbPrincipal.add(rbClientes);
        rbClientes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        rbClientes.setText("Clientes");
        rbClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbClientesActionPerformed(evt);
            }
        });

        rbOS_Orcamento.setBackground(java.awt.SystemColor.control);
        grupoTbPrincipal.add(rbOS_Orcamento);
        rbOS_Orcamento.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        rbOS_Orcamento.setText("Orçamento / OS");
        rbOS_Orcamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbOS_OrcamentoActionPerformed(evt);
            }
        });

        jPanel14.setBackground(java.awt.SystemColor.control);
        jPanel14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 0, new java.awt.Color(153, 153, 153)));

        jLabel3.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel3.setText("Meio de busca");

        meioDeBusca.add(rbPorServico);
        rbPorServico.setText("Serviço");
        rbPorServico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPorServicoActionPerformed(evt);
            }
        });

        meioDeBusca.add(rbPorNome);
        rbPorNome.setSelected(true);
        rbPorNome.setText("Cliente");
        rbPorNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPorNomeActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Dialog", 3, 14)); // NOI18N
        jLabel4.setText("Filtrar por Situação");

        cbFiltroSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Qualquer", "Na bancada", "Pronto", "Entrega OK", "Orçamento REPROVADO", "Aguardando Aprovação", "Aguardando peças", "Abandonado pelo cliente", "Retornou" }));
        cbFiltroSituacao.setToolTipText("");
        cbFiltroSituacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFiltroSituacaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(rbPorNome)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbPorServico))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(cbFiltroSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(16, 16, 16))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbPorServico)
                    .addComponent(rbPorNome))
                .addGap(1, 1, 1)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbFiltroSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnTbPrincipalLayout = new javax.swing.GroupLayout(pnTbPrincipal);
        pnTbPrincipal.setLayout(pnTbPrincipalLayout);
        pnTbPrincipalLayout.setHorizontalGroup(
            pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTbPrincipalLayout.createSequentialGroup()
                .addGroup(pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTbPrincipalLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(rbClientes)
                        .addGap(18, 18, 18)
                        .addComponent(rbOS_Orcamento))
                    .addGroup(pnTbPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPesquisar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCliPesquisar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnTbPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scTbPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnTbPrincipalLayout.setVerticalGroup(
            pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTbPrincipalLayout.createSequentialGroup()
                .addGroup(pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnTbPrincipalLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPesquisar)
                            .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(pnTbPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbClientes)
                            .addComponent(rbOS_Orcamento)))
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scTbPrincipal)
                .addContainerGap())
        );

        jPanel12.setBackground(java.awt.SystemColor.control);
        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Marca", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtMarcaEquipamento.setEditable(false);
        txtMarcaEquipamento.setEnabled(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txtMarcaEquipamento, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtMarcaEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel15.setBackground(java.awt.SystemColor.control);
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Cor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtCorEquipamento.setEditable(false);
        txtCorEquipamento.setEnabled(false);
        txtCorEquipamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorEquipamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(txtCorEquipamento, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(txtCorEquipamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel16.setBackground(java.awt.SystemColor.control);
        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Usuario que deu entrada", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtUsuarioEntrada.setEnabled(false);
        txtUsuarioEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioEntradaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtUsuarioEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtUsuarioEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
        );

        jPanel17.setBackground(java.awt.SystemColor.control);
        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "* Garantia", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        dtGarantia.setBackground(java.awt.SystemColor.control);
        dtGarantia.setDateFormatString("dd/MM/yyyy");
        dtGarantia.setEnabled(false);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(dtGarantia, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(dtGarantia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(pnTbPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnResetar, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(8, 8, 8)
                        .addComponent(btnCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblCamposObrigatorios)
                        .addGap(16, 16, 16))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(8, 8, 8)
                                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel11Layout.createSequentialGroup()
                                .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnTbPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblCamposObrigatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnResetar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(911, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // TODO add your handling code here:
        emitir_os();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        alterar_os();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnResetarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnResetarActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        // TODO add your handling code here:
        excluir_os();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // TODO add your handling code here:
        pesquisar();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tbPrincipalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPrincipalMouseClicked
        // TODO add your handling code here:
        setar_campos();
    }//GEN-LAST:event_tbPrincipalMouseClicked

    private void txtClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClienteActionPerformed

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoActionPerformed

    private void rbOS_OrcamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbOS_OrcamentoActionPerformed
        // TODO add your handling code here:
        instanciarTabelaOrcamento();
    }//GEN-LAST:event_rbOS_OrcamentoActionPerformed

    private void rbClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbClientesActionPerformed
        // TODO add your handling code here:
        InstanciarTabelaCliente();
    }//GEN-LAST:event_rbClientesActionPerformed

    private void btnClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClienteActionPerformed
        // TODO add your handling code here:
        CadClientes cliente = new CadClientes();
        cliente.setVisible(true);
        cliente.lblUsuario.setText("OS");
    }//GEN-LAST:event_btnClienteActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        imprimir_os();
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        InstanciarCombobox();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        fecharOS();
    }//GEN-LAST:event_formWindowClosed

    private void rbPorNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPorNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbPorNomeActionPerformed

    private void rbPorServicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPorServicoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbPorServicoActionPerformed

    private void cbEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEquipamentoActionPerformed
        try {
            dtGarantia.setDate(null);
            String sql = "select idcli as ID, nomecli as Nome, telefonecli as Telefone from tbclientes where idcli > 1";

            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            tbClientes.setModel(DbUtils.resultSetToTableModel(rs));
            SimpleDateFormat dfo = new SimpleDateFormat("yyyy-MM-dd");

            for (int i = 0; i < tbClientes.getModel().getRowCount(); i++) {
                if (tbClientes.getModel().getValueAt(i, 1).toString().equals(txtCliente.getText())) {
                    instanciarTabelaMaquinasAux(Integer.parseInt(tbClientes.getModel().getValueAt(i, 0).toString()));
                    for (int j = 0; j < tbEquipamentos.getModel().getRowCount(); j++) {
                        if (cbEquipamento.getSelectedItem().toString().equals(tbEquipamentos.getModel().getValueAt(j, 0).toString())) {
                            txtCorEquipamento.setText(tbEquipamentos.getModel().getValueAt(j, 2).toString());
                            txtMarcaEquipamento.setText(tbEquipamentos.getModel().getValueAt(j, 3).toString());
                            txtUsuarioEntrada.setText(tbEquipamentos.getModel().getValueAt(j, 4).toString());
                            dtGarantia.setEnabled(true);
                            dtGarantia.setDate(dfo.parse(tbEquipamentos.getModel().getValueAt(j, 5).toString()));
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }//GEN-LAST:event_cbEquipamentoActionPerformed

    private void txtCorEquipamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorEquipamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorEquipamentoActionPerformed

    private void cbFiltroSituacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFiltroSituacaoActionPerformed
        try {
            String sql;
            if (tipo == "Orcamento_OS") {
                if (!cbFiltroSituacao.getSelectedItem().equals("Qualquer")) {
                    sql = "select os as Numero_da_Ordem_de_Serviço, data_os as Emição, tipo as Tipo, situacao as Situação, previsao_entreg_os as Previsão_de_Entrega, equipamento as Equipamento, defeito as Defeito, servico as Serviço, funcionario as Funcionario, valor as Valor, cliente as Cliente from tbos where situacao = '" + cbFiltroSituacao.getSelectedItem() + "'";

                    pst = conexao.prepareStatement(sql);
                    rs = pst.executeQuery();
                    tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));

                } else {
                    sql = "select os as Numero_da_Ordem_de_Serviço, data_os as Emição, tipo as Tipo, situacao as Situação, previsao_entreg_os as Previsão_de_Entrega, equipamento as Equipamento, defeito as Defeito, servico as Serviço, funcionario as Funcionario, valor as Valor, cliente as Cliente from tbos";

                    pst = conexao.prepareStatement(sql);
                    rs = pst.executeQuery();
                    tbPrincipal.setModel(DbUtils.resultSetToTableModel(rs));
                }

            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }

    }//GEN-LAST:event_cbFiltroSituacaoActionPerformed

    private void txtUsuarioEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioEntradaActionPerformed

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
            java.util.logging.Logger.getLogger(CadOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadOS.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CadOS().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JToggleButton btnCliente;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnRemover;
    private javax.swing.JButton btnResetar;
    private javax.swing.JComboBox<String> cbDataEquipamentos;
    private javax.swing.JComboBox<String> cbEquipamento;
    private javax.swing.JComboBox<String> cbFiltroSituacao;
    private javax.swing.JComboBox<String> cbFuncionario;
    private javax.swing.JComboBox<String> cbTipo;
    private javax.swing.JComboBox<String> cboOsSit;
    private com.toedter.calendar.JDateChooser dtGarantia;
    private com.toedter.calendar.JDateChooser dtPrevisao;
    private javax.swing.ButtonGroup grupoTbPrincipal;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblCamposObrigatorios;
    private javax.swing.JLabel lblPesquisar;
    public javax.swing.JLabel lblUsuario;
    private javax.swing.ButtonGroup meioDeBusca;
    private javax.swing.JPanel pnTbPrincipal;
    private javax.swing.JRadioButton rbClientes;
    private javax.swing.JRadioButton rbOS_Orcamento;
    private javax.swing.JRadioButton rbPorNome;
    private javax.swing.JRadioButton rbPorServico;
    private javax.swing.JScrollPane scTbPrincipal;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTable tbEquipamentos;
    private javax.swing.JTable tbPrincipal;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCorEquipamento;
    private javax.swing.JTextField txtMarcaEquipamento;
    private javax.swing.JTextField txtOs;
    private javax.swing.JTextArea txtOsDef;
    private javax.swing.JTextArea txtOsServ;
    private javax.swing.JTextField txtOsValor;
    private javax.swing.JTextField txtUsuarioEntrada;
    // End of variables declaration//GEN-END:variables
}
