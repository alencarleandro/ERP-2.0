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
import static br.com.LeGnusERP.telas.SubClienteAnimal.IdCliente;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Ad3ln0r
 */
public class SubClienteMaquina extends javax.swing.JFrame {

    /**
     * Creates new form SubClienteMaquina
     */
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SubClienteMaquina() {
        initComponents();
        conexao = ModuloConexao.conector();
        setIcon();
        InstanciarComboboxUsuario();
    }

    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/br/com/LeGnusERP/icones/Logo - Legnu 's INFORTEC - 81x58.png")));
    }

    private void adicionar() {
        String sql = "insert into tbsubclientes(nome,especie_marca,raca_modelo,ano,cor,tamanho,referencia,obs,tipo,ultimaEntrada,usuario_que_deu_entrada)values(?,?,?,?,?,?,?,?,?,?,?)";
        try {

            Date d = new Date();
            java.sql.Date dSql = new java.sql.Date(d.getTime());
            df.format(dSql);

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDescricao.getText());
            pst.setString(2, txtMarca.getText());
            pst.setString(3, txtModelo.getText());
            pst.setString(4, txtAno.getText());
            pst.setString(5, txtCor.getText());
            pst.setString(6, txtTamanho.getText());
            pst.setString(7, IdCliente.getText());
            pst.setString(8, txtOBS.getText());
            pst.setString(9, "Maquina");
            pst.setTimestamp(10, Timestamp.valueOf(df.format(d)));
            pst.setString(11, cbUsuarioEntrada.getSelectedItem().toString());

            //Validação dos Campos Obrigatorios
            if ((txtDescricao.getText().isEmpty() == true)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else {

                //A linha abaixo atualiza os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "SubCliente adicionado com sucesso");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void InstanciarComboboxUsuario() {
        try {
            cbUsuarioEntrada.removeAllItems();
            String sql = "select usuario from tbusuarios";
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                cbUsuarioEntrada.addItem(rs.getString("usuario"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();

        }
    }

    public void NomeReferencia() {
        try {
            String sql = "select nomecli from tbclientes where idcli=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, IdCliente.getText());
            rs = pst.executeQuery();
            tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));
            NomeCliente.setText(tbAuxilio.getModel().getValueAt(0, 0).toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void instanciarTabela() {
        try {
            NomeReferencia();
            String sql = "select idsub as ID, nome as Nome, ultimaEntrada as UltimaEntrada from tbsubclientes where tipo='Maquina' and referencia=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, IdCliente.getText());
            rs = pst.executeQuery();
            tbSubTabela.setModel(DbUtils.resultSetToTableModel(rs));
            pnTabela.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Cliente Referencia: " + NomeCliente.getText(), javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

            instanciarTabelaAuxilio();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    public void instanciarTabelaAuxilio() {
        try {
            String sql = "select idsub,nome,especie_marca,raca_modelo,ano,cor,tamanho,referencia,obs,usuario_que_deu_entrada,garantia from tbsubclientes where tipo='Maquina' and referencia=?";
            pst = conexao.prepareStatement(sql);
            pst.setString(1, IdCliente.getText());
            rs = pst.executeQuery();
            tbAuxilioSub.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void alterar() {
        //idsub,nome,especie_marca,raca_modelo,ano,cor,tamanho,obs
        String sql = "update tbsubclientes set nome=?,especie_marca=?,raca_modelo=?,ano=?,cor=?,tamanho=?,obs=?,usuario_que_deu_entrada=? where idsub=?";
        try {

            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtDescricao.getText());
            pst.setString(2, txtMarca.getText());
            pst.setString(3, txtModelo.getText());
            pst.setString(4, txtAno.getText());
            pst.setString(5, txtCor.getText());
            pst.setString(6, txtTamanho.getText());
            pst.setString(7, txtOBS.getText());
            pst.setString(8, cbUsuarioEntrada.getSelectedItem().toString());
            pst.setString(9, ID_Sub.getText());

            if ((txtDescricao.getText().isEmpty() == true)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else {

                //A linha abaixo atualiza os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "SubCliente alterado com sucesso");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    private void atualizarEntrada() {
        //idsub,nome,especie_marca,raca_modelo,ano,cor,tamanho,obs
        String sql = "update tbsubclientes set ultimaEntrada=?, usuario_que_deu_entrada=? where idsub=?";
        try {

            Date d = new Date();
            java.sql.Date dSql = new java.sql.Date(d.getTime());
            df.format(dSql);

            pst = conexao.prepareStatement(sql);
            pst.setTimestamp(1, Timestamp.valueOf(df.format(d)));
            pst.setString(2, cbUsuarioEntrada.getSelectedItem().toString());
            pst.setString(3, ID_Sub.getText());

            if ((txtDescricao.getText().isEmpty() == true)) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatorios");
            } else {

                //A linha abaixo atualiza os dados do novo usuario
                int adicionado = pst.executeUpdate();
                //A Linha abaixo serve de apoio ao entendimento da logica
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Entrada da maquina atualizada com sucesso");
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }
    }

    private void remover() {
        int confirma = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja remover este SubCliente?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {

            try {

                String sql = "delete from tbsubclientes where idsub=?";
                pst = conexao.prepareStatement(sql);
                pst.setString(1, ID_Sub.getText());
                int apagado = pst.executeUpdate();

                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Clique no OK e Aguarde.");

                    JOptionPane.showMessageDialog(null, "SubCliente removido com sucesso.");
                    limpar();

                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                limpar();
            }
        }

    }

    public void setar_campos() {
        int setar = tbSubTabela.getSelectedRow();
        instanciarTabelaAuxilio();
        DateFormat dfo = new SimpleDateFormat("yyyy-MM-dd");
        //idsub,nome,especie_marca,raca_modelo,ano,cor,tamanho,obs
        try {

            ID_Sub.setText(tbAuxilioSub.getModel().getValueAt(setar, 0).toString());

            if (tbAuxilioSub.getModel().getValueAt(setar, 1) != null) {
                txtDescricao.setText(tbAuxilioSub.getModel().getValueAt(setar, 1).toString());
            } else {
                txtDescricao.setText("");
            }

            if (tbAuxilioSub.getModel().getValueAt(setar, 2) != null) {
                txtMarca.setText(tbAuxilioSub.getModel().getValueAt(setar, 2).toString());
            } else {
                txtMarca.setText("");
            }

            if (tbAuxilioSub.getModel().getValueAt(setar, 3) != null) {
                txtModelo.setText(tbAuxilioSub.getModel().getValueAt(setar, 3).toString());
            } else {
                txtModelo.setText("");
            }
            if (tbAuxilioSub.getModel().getValueAt(setar, 4) != null) {
                txtAno.setText(tbAuxilioSub.getModel().getValueAt(setar, 4).toString());
            } else {
                txtAno.setText("");
            }
            if (tbAuxilioSub.getModel().getValueAt(setar, 5) != null) {
                txtCor.setText(tbAuxilioSub.getModel().getValueAt(setar, 5).toString());
            } else {
                txtCor.setText("");
            }
            if (tbAuxilioSub.getModel().getValueAt(setar, 6) != null) {
                txtTamanho.setText(tbAuxilioSub.getModel().getValueAt(setar, 6).toString());
            } else {
                txtTamanho.setText("");
            }
            if (tbAuxilioSub.getModel().getValueAt(setar, 8) != null) {
                txtOBS.setText(tbAuxilioSub.getModel().getValueAt(setar, 8).toString());
            } else {
                txtOBS.setText("");
            }
            if (tbAuxilioSub.getModel().getValueAt(setar, 9) != null) {
                cbUsuarioEntrada.setSelectedItem(tbAuxilioSub.getModel().getValueAt(setar, 9).toString());
            } else {
                cbUsuarioEntrada.setSelectedItem("Administrador");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            limpar();
        }

        //A Linha Abaixo desabilita o botão adicionar
        btnAdicionar.setEnabled(false);
        btnImprimir.setEnabled(true);
        btnAtualizarEntrada.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnRemover.setEnabled(true);
        btnAtualizarEntrada.setEnabled(true);

    }

    public void imprimir() {

        int confirma = JOptionPane.showConfirmDialog(null, "Deseja criar um relatorio dos dados do equipamento selecionado?", "Atenção", JOptionPane.YES_NO_OPTION);

        if (confirma == JOptionPane.YES_OPTION) {
            try {
                String opcao = JOptionPane.showInputDialog("Escolha o tipo de relatorio\n"
                        + "A4 >>> Digite '1'.\n"
                        + "Rolo 0.8mm >>> Digite '2'.\n"
                        + "OBS: Caracteres aceitados são somente 1,2.");

                String path = System.getProperty("user.home") + File.separator + "RelatoriosERP"
                        + File.separator + "OrdensDeServiço" + File.separator
                        + txtDescricao.getText().replace(" ", "") + "_" + ".pdf";
                new File(path).getParentFile().mkdirs();

                String imprecao = "select nome_empresa,nome_proprietario,email_proprietario,descricao,obs,numero,imagem from tbrelatorio where idRelatorio=1";
                pst = conexao.prepareStatement(imprecao);
                rs = pst.executeQuery();
                tbAuxilio.setModel(DbUtils.resultSetToTableModel(rs));

                String nome_empresa = tbAuxilio.getModel().getValueAt(0, 0).toString();
                String nome_proprietario = tbAuxilio.getModel().getValueAt(0, 1).toString();
                String descricao = tbAuxilio.getModel().getValueAt(0, 3).toString();

                if (opcao.equals("1")) {

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                    Document document = new Document(pdfDocument);
                    pdfDocument.setDefaultPageSize(PageSize.A4);

                    float col = 280f;
                    float columnWidth[] = {360f, 200f};
                    com.itextpdf.layout.element.Table sumario = new com.itextpdf.layout.element.Table(columnWidth);

                    sumario.setBackgroundColor(new DeviceRgb(63, 169, 219))
                            .setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));

                    sumario.addCell(new Cell().add(new Paragraph("Dados do equipamento")).setTextAlignment(TextAlignment.CENTER)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE).setMarginTop(30f).setMarginBottom(30f)
                            .setFontSize(30f).setBorder(Border.NO_BORDER));

                    sumario.addCell(new Cell().add(new Paragraph(" \n" + nome_empresa + " \n"))
                            .setTextAlignment(TextAlignment.RIGHT).setMarginTop(30f).setMarginBottom(30f)
                            .setBorder(Border.NO_BORDER).setMarginRight(10f));

                    float colCliente[] = {560};
                    com.itextpdf.layout.element.Table dadosCliente = new com.itextpdf.layout.element.Table(colCliente);

                    dadosCliente.addCell(new Cell().add(new Paragraph("Informações do cliente")).setBold().setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Nome: " + NomeCliente.getText())).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Nome do equipamento: " + txtDescricao.getText())).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Funcionario que registrou")).setBold().setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Nome: " + cbUsuarioEntrada.getSelectedItem().toString())).setBorder(Border.NO_BORDER));

                    float colSubCliente[] = {560};
                    com.itextpdf.layout.element.Table dadosSubCliente = new com.itextpdf.layout.element.Table(colSubCliente);

                    dadosSubCliente.addCell(new Cell(2, 2).add(new Paragraph("Dados do equipamento selecionado")).setBold().setBorder(Border.NO_BORDER));

                    float colDetalhamentoDadosSubCliente[] = {280, 280};
                    com.itextpdf.layout.element.Table detalhamentoDadosSubCliente = new com.itextpdf.layout.element.Table(colDetalhamentoDadosSubCliente);

                    detalhamentoDadosSubCliente.addCell(new Cell(1, 1).add(new Paragraph("Nome: " + txtDescricao.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell(1, 1).add(new Paragraph("Cor: " + txtCor.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell(1, 1).add(new Paragraph("Marca: " + txtMarca.getText())).setBorder(Border.NO_BORDER));

                    detalhamentoDadosSubCliente.addCell(new Cell(2, 2).add(new Paragraph("Modelo: " + txtModelo.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell(2, 2).add(new Paragraph("Tamanho: " + txtTamanho.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell(2, 2).add(new Paragraph("Ano: " + txtAno.getText())).setBorder(Border.NO_BORDER));

                    com.itextpdf.layout.element.Table observacao = new com.itextpdf.layout.element.Table(colSubCliente);
                    observacao.addCell(new Cell(2, 2).add(new Paragraph("Observacoes")).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                    observacao.addCell(new Cell(1, 1).add(new Paragraph("Detalhamento: " + txtOBS.getText())).setBorder(Border.NO_BORDER));

                    document.add(sumario);
                    document.add(new Paragraph("\n"));
                    document.add(dadosCliente);
                    document.add(new Paragraph("\n"));
                    document.add(dadosSubCliente);
                    document.add(detalhamentoDadosSubCliente);
                    document.add(new Paragraph("\n"));
                    document.add(observacao);
                    document.close();

                    JOptionPane.showMessageDialog(null, "Pdf criado com sucesso, guardado em " + path);

                    if (Desktop.isDesktopSupported()) {
                        File pdfFile = new File(path);
                        Desktop.getDesktop().open(pdfFile);
                    }

                } else if (opcao.equals("2")) {

                    PdfWriter pdfWriter = new PdfWriter(path);
                    PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                    PageSize pageSize = new PageSize(226.77f, 800f);
                    pdfDocument.setDefaultPageSize(pageSize);
                    Document document = new Document(pdfDocument);

                    float columnWidth[] = {113f, 113f};
                    com.itextpdf.layout.element.Table sumario = new com.itextpdf.layout.element.Table(columnWidth);
                    sumario.setBackgroundColor(new DeviceRgb(63, 169, 219))
                            .setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));
                    sumario.addCell(new Cell().add(new Paragraph("Dados do equipamento"))
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setMarginTop(5f).setMarginBottom(5f)
                            .setFontSize(12f).setBorder(Border.NO_BORDER));
                    sumario.addCell(new Cell().add(new Paragraph(" \n" + nome_empresa + " \n"))
                            .setTextAlignment(TextAlignment.RIGHT).setMarginTop(5f).setMarginBottom(5f)
                            .setBorder(Border.NO_BORDER).setMarginRight(5f));

                    float colCliente[] = {226.77f};
                    com.itextpdf.layout.element.Table dadosCliente = new com.itextpdf.layout.element.Table(colCliente);
                    dadosCliente.addCell(new Cell().add(new Paragraph("Informações do cliente")).setBold().setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Nome: " + NomeCliente.getText())).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Equipamento: " + txtDescricao.getText())).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Funcionário que registrou")).setBold().setBorder(Border.NO_BORDER));
                    dadosCliente.addCell(new Cell().add(new Paragraph("Nome: " + cbUsuarioEntrada.getSelectedItem().toString())).setBorder(Border.NO_BORDER));

                    float colSubCliente[] = {226.77f};
                    com.itextpdf.layout.element.Table dadosSubCliente = new com.itextpdf.layout.element.Table(colSubCliente);
                    dadosSubCliente.addCell(new Cell(1, 1).add(new Paragraph("Dados do equipamento selecionado")).setBold().setBorder(Border.NO_BORDER));

                    float colDetalhamentoDadosSubCliente[] = {226f};
                    com.itextpdf.layout.element.Table detalhamentoDadosSubCliente = new com.itextpdf.layout.element.Table(colDetalhamentoDadosSubCliente);
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Nome: " + txtDescricao.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Cor: " + txtCor.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Marca: " + txtMarca.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Modelo: " + txtModelo.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Tamanho: " + txtTamanho.getText())).setBorder(Border.NO_BORDER));
                    detalhamentoDadosSubCliente.addCell(new Cell().add(new Paragraph("Ano: " + txtAno.getText())).setBorder(Border.NO_BORDER));

                    com.itextpdf.layout.element.Table observacao = new com.itextpdf.layout.element.Table(colSubCliente);
                    observacao.addCell(new Cell().add(new Paragraph("Observações"))
                            .setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
                    observacao.addCell(new Cell().add(new Paragraph("Detalhamento: " + txtOBS.getText())).setBorder(Border.NO_BORDER));

                    document.add(sumario);
                    document.add(new Paragraph("\n"));
                    document.add(dadosCliente);
                    document.add(new Paragraph("\n"));
                    document.add(dadosSubCliente);
                    document.add(detalhamentoDadosSubCliente);
                    document.add(new Paragraph("\n"));
                    document.add(observacao);
                    document.close();

                    JOptionPane.showMessageDialog(null, "Pdf criado com sucesso, guardado em " + path);

                    if (Desktop.isDesktopSupported()) {
                        File pdfFile = new File(path);
                        Desktop.getDesktop().open(pdfFile);
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Alternativa invalida, as Disponiveis são 1,2.");
                    limpar();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void limpar() {
        txtAno.setText(null);
        txtCor.setText(null);
        txtDescricao.setText(null);
        txtMarca.setText(null);
        txtModelo.setText(null);
        txtOBS.setText(null);
        txtTamanho.setText(null);

        btnAdicionar.setEnabled(true);
        btnAtualizarEntrada.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnRemover.setEnabled(false);
        btnAtualizarEntrada.setEnabled(false);
        btnImprimir.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ID_Sub = new javax.swing.JTextField();
        ID = new javax.swing.JTextField();
        scAuxilioSub = new javax.swing.JScrollPane();
        tbAuxilioSub = new javax.swing.JTable();
        IdCliente = new javax.swing.JTextField();
        NomeCliente = new javax.swing.JTextField();
        scAuxilio = new javax.swing.JScrollPane();
        tbAuxilio = new javax.swing.JTable();
        scImpressao = new javax.swing.JScrollPane();
        tbImpressao = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        pnTabela = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbSubTabela = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        lblCamposObrigatorios = new javax.swing.JLabel();
        btnAtualizarEntrada = new javax.swing.JToggleButton();
        btnRemover = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnAdicionar = new javax.swing.JButton();
        btnAtualizar = new javax.swing.JToggleButton();
        btnImprimir = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        txtMarca = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        txtModelo = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        txtTamanho = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        txtDescricao = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        txtCor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        txtAno = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOBS = new javax.swing.JTextArea();
        jPanel11 = new javax.swing.JPanel();
        cbUsuarioEntrada = new javax.swing.JComboBox<>();

        tbAuxilioSub.setModel(new javax.swing.table.DefaultTableModel(
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
        scAuxilioSub.setViewportView(tbAuxilioSub);

        NomeCliente.setText("jTextField1");

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

        tbImpressao.setModel(new javax.swing.table.DefaultTableModel(
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
        scImpressao.setViewportView(tbImpressao);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("LeGnu`s_EPR - Tela SubClientes");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel8.setBackground(java.awt.SystemColor.control);
        jPanel8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createMatteBorder(2, 0, 0, 0, new java.awt.Color(204, 204, 204))));

        jPanel9.setBackground(java.awt.SystemColor.control);
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        pnTabela.setBackground(java.awt.SystemColor.control);
        pnTabela.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, new java.awt.Color(153, 153, 153)), "Cliente Referencia", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        tbSubTabela = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tbSubTabela.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        tbSubTabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbSubTabela.setFocusable(false);
        tbSubTabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbSubTabelaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbSubTabela);

        javax.swing.GroupLayout pnTabelaLayout = new javax.swing.GroupLayout(pnTabela);
        pnTabela.setLayout(pnTabelaLayout);
        pnTabelaLayout.setHorizontalGroup(
            pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnTabelaLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );
        pnTabelaLayout.setVerticalGroup(
            pnTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnTabelaLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                .addGap(8, 8, 8))
        );

        jPanel10.setBackground(java.awt.SystemColor.control);
        jPanel10.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 0, new java.awt.Color(153, 153, 153)));

        lblCamposObrigatorios.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblCamposObrigatorios.setText("* Campos Obrigatorios");

        btnAtualizarEntrada.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/entrada.png"))); // NOI18N
        btnAtualizarEntrada.setToolTipText("");
        btnAtualizarEntrada.setBorderPainted(false);
        btnAtualizarEntrada.setContentAreaFilled(false);
        btnAtualizarEntrada.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnAtualizarEntrada.setEnabled(false);
        btnAtualizarEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarEntradaActionPerformed(evt);
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

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/LeGnusERP/icones/ImpresoraIcon.png"))); // NOI18N
        btnImprimir.setToolTipText("");
        btnImprimir.setBorderPainted(false);
        btnImprimir.setContentAreaFilled(false);
        btnImprimir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnImprimir.setEnabled(false);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        jPanel3.setBackground(java.awt.SystemColor.control);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Marca", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(txtMarca)
                .addGap(0, 0, 0))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBackground(java.awt.SystemColor.control);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Modelo", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtModeloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtModelo)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel7.setBackground(java.awt.SystemColor.control);
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Tamanho", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTamanho, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtTamanho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBackground(java.awt.SystemColor.control);
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "*Nome", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescricao)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel6.setBackground(java.awt.SystemColor.control);
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Cor", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCor)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtCor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(java.awt.SystemColor.control);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 1, new java.awt.Color(153, 153, 153)), "Ano", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAno, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel1.setBackground(java.awt.SystemColor.control);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "Observação", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        txtOBS.setColumns(20);
        txtOBS.setRows(5);
        jScrollPane1.setViewportView(txtOBS);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
        );

        jPanel11.setBackground(java.awt.SystemColor.control);
        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(153, 153, 153)), "* Usuário que deu entrada", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Arial", 3, 12))); // NOI18N

        cbUsuarioEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUsuarioEntradaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbUsuarioEntrada, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(cbUsuarioEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)))
                .addGap(0, 0, 0))
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblCamposObrigatorios)
                .addGap(16, 16, 16))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btnAtualizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(lblCamposObrigatorios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAtualizarEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnImprimir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(pnTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(816, 608));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        alterar();
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnAtualizarEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarEntradaActionPerformed
        // TODO add your handling code here:
        atualizarEntrada();
    }//GEN-LAST:event_btnAtualizarEntradaActionPerformed

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
        remover();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        adicionar();
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void txtModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtModeloActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
        instanciarTabela();
    }//GEN-LAST:event_formWindowActivated

    private void tbSubTabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbSubTabelaMouseClicked
        // TODO add your handling code here:
        setar_campos();
    }//GEN-LAST:event_tbSubTabelaMouseClicked

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        limpar();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void cbUsuarioEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUsuarioEntradaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUsuarioEntradaActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        imprimir();
    }//GEN-LAST:event_btnImprimirActionPerformed

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
            java.util.logging.Logger.getLogger(SubClienteMaquina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SubClienteMaquina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SubClienteMaquina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SubClienteMaquina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SubClienteMaquina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField ID;
    private javax.swing.JTextField ID_Sub;
    public static javax.swing.JTextField IdCliente;
    private javax.swing.JTextField NomeCliente;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JToggleButton btnAtualizar;
    private javax.swing.JToggleButton btnAtualizarEntrada;
    private javax.swing.JToggleButton btnImprimir;
    private javax.swing.JButton btnRemover;
    private javax.swing.JComboBox<String> cbUsuarioEntrada;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
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
    private javax.swing.JLabel lblCamposObrigatorios;
    private javax.swing.JPanel pnTabela;
    private javax.swing.JScrollPane scAuxilio;
    private javax.swing.JScrollPane scAuxilioSub;
    private javax.swing.JScrollPane scImpressao;
    private javax.swing.JTable tbAuxilio;
    private javax.swing.JTable tbAuxilioSub;
    private javax.swing.JTable tbImpressao;
    private javax.swing.JTable tbSubTabela;
    private javax.swing.JTextField txtAno;
    private javax.swing.JTextField txtCor;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextArea txtOBS;
    private javax.swing.JTextField txtTamanho;
    // End of variables declaration//GEN-END:variables
}
