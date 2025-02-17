
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
        addComponent(constraints, CepTxt, 1, 0);
        addComponent(constraints, new JLabel("CEP:"), 0, 1);
        addComponent(constraints, CepTxtII, 1, 1);
        addComponent(constraints, new JLabel("Logradouro:"), 0, 2);
        addComponent(constraints, logradouroTxt, 1, 2);
        addComponent(constraints, new JLabel("Complemento:"), 0, 3);
        addComponent(constraints, complementoTxt, 1, 3);
        addComponent(constraints, new JLabel("Bairro:"), 0, 4);
        addComponent(constraints, bairroTxt, 1, 4);
        addComponent(constraints, new JLabel("Localidade:"), 0, 5);
        addComponent(constraints, localidadeTxt, 1, 5);
        addComponent(constraints, new JLabel("UF:"), 0, 6);
        addComponent(constraints, ufTxt, 1, 6);
        addComponent(constraints, new JLabel("DDD:"), 0, 7);
        addComponent(constraints, dddTxt, 1, 7);
        addComponent(constraints, buttonConsultar, 0, 8);
        addComponent(constraints, buttonHistorico, 1, 8);
        addComponent(constraints, buttonLimpar, 0, 9);
        
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