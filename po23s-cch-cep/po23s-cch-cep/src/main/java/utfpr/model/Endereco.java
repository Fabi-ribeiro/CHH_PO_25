/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utfpr.model;

/**
 *
 * @author Jhean
 */
public class Endereco {
    private String cep, logradouro, bairro, cidade, estado;

    public Endereco(String cep, String logradouro, String bairro, String cidade, String estado) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s - %s (%s)", logradouro, bairro, cidade, estado, cep);
    }
}
