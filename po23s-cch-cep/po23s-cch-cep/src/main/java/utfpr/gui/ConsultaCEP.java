/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utfpr.gui;

/**
 *
 * @author Jhean
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.JSONObject;
import utfpr.model.Endereco;
import utfpr.model.Historico;
import utfpr.http.ClienteHttp;

public class ConsultaCEP extends JFrame {
    private JTextField cepField;
    private JTextArea resultArea, historicoArea;
    private Historico historico = new Historico();
    private ClienteHttp clienteHttp = new ClienteHttp();

    public ConsultaCEP() {
        setTitle("Consulta CEP");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        topPanel.add(new JLabel("Digite o CEP: "));
        cepField = new JTextField(15);
        topPanel.add(cepField);
        JButton buscarBtn = new JButton("Buscar");
        topPanel.add(buscarBtn);
        JButton limparBtn = new JButton("Limpar");
        topPanel.add(limparBtn);
        add(topPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 1));
        resultArea = new JTextArea(5, 40);
        resultArea.setBorder(BorderFactory.createTitledBorder("Resultado"));
        centerPanel.add(new JScrollPane(resultArea));
        
        historicoArea = new JTextArea(5, 40);
        historicoArea.setBorder(BorderFactory.createTitledBorder("Histórico de Consultas"));
        centerPanel.add(new JScrollPane(historicoArea));
        add(centerPanel, BorderLayout.CENTER);
        
        buscarBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCEP();
            }
        });
        
        limparBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
    }
    
    private void buscarCEP() {
        String cep = cepField.getText().trim();
        if (cep.length() < 8) {
            resultArea.setText("Erro: CEP inválido!");
            return;
        }
        try {
            String jsonResponse = clienteHttp.buscaDados(cep);
            JSONObject json = new JSONObject(jsonResponse);
            if (json.has("erro")) {
                resultArea.setText("Erro: CEP não encontrado!");
                return;
            }
            Endereco endereco = new Endereco(
                json.getString("cep"),
                json.getString("logradouro"),
                json.getString("bairro"),
                json.getString("localidade"),
                json.getString("uf")
            );
            resultArea.setText(endereco.toString());
            historico.adicionarConsulta(endereco);
            historicoArea.setText(historico.getHistorico());
        } catch (Exception ex) {
            resultArea.setText("Erro ao buscar CEP.");
        }
    }
    
    private void limparCampos() {
        cepField.setText("");
        resultArea.setText("");
        historicoArea.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConsultaCEP frame = new ConsultaCEP();
            frame.setVisible(true);
        });
    }
}
