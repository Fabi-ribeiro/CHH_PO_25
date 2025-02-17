
package utfpr.Frame;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONObject;

public class TelaConsultaII extends JFrame {
    private final List<String> historicoConsultas = new ArrayList<>();
    
    // Declaração de variáveis
    private JTextField CepTxt, CepTxtII, logradouroTxt, complementoTxt, bairroTxt, localidadeTxt, ufTxt, dddTxt;
    private JButton buttonConsultar, buttonHistorico, buttonLimpar;

    public TelaConsultaII() {
        initComponents();
    }

    private void initComponents() {
    setTitle("Consulta CEP");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(450, 400);
    setLocationRelativeTo(null);
    setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.insets = new Insets(5, 5, 5, 5);

    // Componentes da tela
    JLabel labelCep = new JLabel("CEP:");
    CepTxt = new JTextField(10);
    CepTxtII = new JTextField(15);
    logradouroTxt = new JTextField(15);
    complementoTxt = new JTextField(15);
    bairroTxt = new JTextField(15);
    localidadeTxt = new JTextField(15);
    ufTxt = new JTextField(5);
    dddTxt = new JTextField(5);

    buttonConsultar = new JButton("Consultar");
    buttonConsultar.addActionListener(this::buttonConsultarActionPerformed);
    
    buttonHistorico = new JButton("Histórico");
    buttonHistorico.addActionListener(this::buttonHistoricoActionPerformed);
    
    buttonLimpar = new JButton("Limpar");
    buttonLimpar.addActionListener(this::buttonLimparActionPerformed);

    // Adicionando componentes usando GridBagLayout
    addComponent(constraints, labelCep, 0, 0);

    // Colocando o campo CEP e os botões na mesma linha
    constraints.gridx = 1;
    constraints.gridy = 0;
    constraints.gridwidth = 2;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    add(CepTxt, constraints);

    // Botões ao lado do campo CEP
    constraints.gridx = 3;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    add(buttonConsultar, constraints);

    constraints.gridx = 4;
    add(buttonHistorico, constraints);

    constraints.gridx = 5;
    add(buttonLimpar, constraints);

    // Criando um JPanel para os resultados da busca
    JPanel resultadoPanel = new JPanel();
    resultadoPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
    resultadoPanel.setLayout(new GridBagLayout()); // Usando GridBagLayout dentro do painel
    GridBagConstraints resultConstraints = new GridBagConstraints();
    resultConstraints.insets = new Insets(5, 5, 5, 5);
    
    // Adicionando os campos de resultado dentro do painel "Resultado"
    addComponent(resultadoPanel, new JLabel("CEP:"), resultConstraints, 0, 0);
    addComponent(resultadoPanel, CepTxtII, resultConstraints, 1, 0);
    addComponent(resultadoPanel, new JLabel("Logradouro:"), resultConstraints, 0, 1);
    addComponent(resultadoPanel, logradouroTxt, resultConstraints, 1, 1);
    addComponent(resultadoPanel, new JLabel("Complemento:"), resultConstraints, 0, 2);
    addComponent(resultadoPanel, complementoTxt, resultConstraints, 1, 2);
    addComponent(resultadoPanel, new JLabel("Bairro:"), resultConstraints, 0, 3);
    addComponent(resultadoPanel, bairroTxt, resultConstraints, 1, 3);
    addComponent(resultadoPanel, new JLabel("Localidade:"), resultConstraints, 0, 4);
    addComponent(resultadoPanel, localidadeTxt, resultConstraints, 1, 4);
    addComponent(resultadoPanel, new JLabel("UF:"), resultConstraints, 0, 5);
    addComponent(resultadoPanel, ufTxt, resultConstraints, 1, 5);
    addComponent(resultadoPanel, new JLabel("DDD:"), resultConstraints, 0, 6);
    addComponent(resultadoPanel, dddTxt, resultConstraints, 1, 6);

    // Colocando o painel "Resultado" no GridBagLayout principal
    constraints.gridx = 0;
    constraints.gridy = 1;
    constraints.gridwidth = 6; // Ocupa toda a largura
    constraints.fill = GridBagConstraints.HORIZONTAL;
    add(resultadoPanel, constraints);

    setVisible(true);
    }

    private void addComponent(GridBagConstraints constraints, JLabel label, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(label, constraints);
    }

    private void addComponent(GridBagConstraints constraints, JTextField textField, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(textField, constraints);
    }

    private void addComponent(GridBagConstraints constraints, JButton button, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(button, constraints);
    }

    private void addComponent(JPanel panel, JLabel label, GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, constraints);
    }

    private void addComponent(JPanel panel, JTextField textField, GridBagConstraints constraints, int x, int y) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(textField, constraints);
    }

    private void buttonConsultarActionPerformed(ActionEvent evt) {
        String cep = CepTxt.getText().trim();
        if (cep.isEmpty() || !cep.matches("\\d{8}")) {
            JOptionPane.showMessageDialog(this, "Digite um CEP válido (apenas números)!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject jsonResponse = new JSONObject(response.toString());

            if (jsonResponse.has("erro")) {
                JOptionPane.showMessageDialog(this, "CEP não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Preenche os campos com os dados retornados da API
            CepTxtII.setText(jsonResponse.getString("cep"));
            logradouroTxt.setText(jsonResponse.optString("logradouro", "N/A"));
            complementoTxt.setText(jsonResponse.optString("complemento", "N/A"));
            bairroTxt.setText(jsonResponse.optString("bairro", "N/A"));
            localidadeTxt.setText(jsonResponse.optString("localidade", "N/A"));
            ufTxt.setText(jsonResponse.optString("uf", "N/A"));
            dddTxt.setText(jsonResponse.optString("ddd", "N/A"));

            // Adiciona ao histórico
            addConsultaToHistorico(cep);

            JOptionPane.showMessageDialog(this, "Consulta realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar o CEP!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addConsultaToHistorico(String cep) {
        String dataConsulta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String historico = String.format("Data: %s\nCEP: %s\nLogradouro: %s\nComplemento: %s\nBairro: %s\nLocalidade: %s\nUF: %s\nDDD: %s\n-------------------", 
                                          dataConsulta, CepTxtII.getText(), logradouroTxt.getText(), complementoTxt.getText(), bairroTxt.getText(), localidadeTxt.getText(), ufTxt.getText(), dddTxt.getText());
        historicoConsultas.add(historico);
    }

    private void buttonHistoricoActionPerformed(ActionEvent evt) {
        if (historicoConsultas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhuma consulta realizada ainda.", "Histórico", JOptionPane.INFORMATION_MESSAGE);
        } else {
            String historico = String.join("\n", historicoConsultas);
            JOptionPane.showMessageDialog(this, "Histórico de Consultas:\n" + historico, "Histórico", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buttonLimparActionPerformed(ActionEvent evt) {
        CepTxt.setText("");
        CepTxtII.setText("");
        logradouroTxt.setText("");
        complementoTxt.setText("");
        bairroTxt.setText("");
        localidadeTxt.setText("");
        ufTxt.setText("");
        dddTxt.setText("");
        JOptionPane.showMessageDialog(this, "Campos limpos com sucesso!", "Limpar", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new TelaConsultaII();
    }
}

