/*
 * The MIT License
 *
 * Copyright 2024 leand.
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
package br.com.LeGnusERP.dal;

/**
 *
 * @author leand
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class comunicacaoLocal {

	private static Connection conexao = ConexaoLocal.conector();
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;
	private static String sql;

	public static String getSql() {
		return sql;
	}

	public static void setSql(String sql) {
		comunicacaoLocal.sql = sql;
	}

	public static Connection getConexao() {
		return conexao;
	}

	public static void setConexao(Connection conexao) {
		comunicacaoLocal.conexao = conexao;
	}

	public static PreparedStatement getPst() {
		return pst;
	}

	public static void setPst(PreparedStatement pst) {
		comunicacaoLocal.pst = pst;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static void setRs(ResultSet rs) {
		comunicacaoLocal.rs = rs;
	}

	public static void prepararConexcao() {
		try {
			comunicacaoLocal.setPst(comunicacaoLocal.getConexao().prepareStatement(comunicacaoLocal.getSql()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}
	}

	public static void executar() {
		try {
			comunicacaoLocal.getPst().execute();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void executarQuery() {
		try {
			setRs(comunicacaoLocal.getPst().executeQuery());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
	}

	public static void atualizarQuery() {
		try {
			comunicacaoLocal.getPst().executeUpdate();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e);

		}
	}

}
