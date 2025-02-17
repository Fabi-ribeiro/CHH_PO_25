/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utfpr.model;

/**
 *
 * @author Jhean
 */
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Historico {
    private final ArrayList<String> consultas = new ArrayList<>();
    
    public void adicionarConsulta(Endereco endereco) {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        consultas.add(data + " - " + endereco);
    }
    
    public String getHistorico() {
        return String.join("\n", consultas);
    }
}
