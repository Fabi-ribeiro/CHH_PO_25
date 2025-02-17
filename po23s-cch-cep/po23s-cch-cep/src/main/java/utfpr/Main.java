/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utfpr;

import utfpr.http.ClienteHttp;

/**
 *
 * @author evand
 */
public class Main {
    public static String url_base = "https://viacep.com.br/ws/";
    public static void main(String[] args) {
        String cep = "85660000"; //"80230901";
        ClienteHttp cliente = new ClienteHttp();
        String resultado = cliente.buscaDados(url_base + cep + "/json/");
        System.out.println(resultado);
        
    }
    
}
