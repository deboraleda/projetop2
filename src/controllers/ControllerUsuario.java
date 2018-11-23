package controllers;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import entidades.Descricao;
import entidades.Usuario;

public class ControllerUsuario {

	private Map<String, Usuario> usuarios = new HashMap<>();
	private int cont = 0;	
	

	public void lerReceptores(String caminho) throws IOException {
		Scanner sc = new Scanner(new File(caminho));
		String linha = null;
		while(sc.hasNextLine()) {
			linha = sc.nextLine();
			if(linha.equals("id,nome,E-mail,celular,classe"))
				continue;
			String[] dadosReceptor = linha.split(",");
			if(dadosReceptor.length != 5) {
				sc.close();
				throw new IOException("Campos invalidos");
			}
				
			if(caminho.split("/")[1].equals("atualizaReceptores.csv"))
				usuarios.get(dadosReceptor[0]).atualizaReceptor(dadosReceptor[1], dadosReceptor[2], dadosReceptor[3]);
			else if(caminho.split("/")[1].equals("novosReceptores.csv"))
				usuarios.put(dadosReceptor[0], new Usuario(dadosReceptor[0],dadosReceptor[1], dadosReceptor[2], dadosReceptor[3], dadosReceptor[4], "receptor", cont++));
		}
		sc.close();
	}

	public String adicionaDoador(String id, String nome, String email, String celular, String classe) {	
		if(id == null || id.trim().equals("")) 
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		if(usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario ja existente: " + id + ".");
		if(classe == null || classe.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: classe nao pode ser vazia ou nula.");
		
		switch(classe) {
		case "PESSOA_FISICA": case "ONG": case "IGREJA": case "ORGAO_PUBLICO_MUNICIPAL": case "ORGAO_PUBLICO_FEDERAL": case "ORGAO_PUBLICO_ESTADUAL":
		case "ASSOCIAÇÃO": case "SOCIEDADE":
			usuarios.put(id, new Usuario(id, nome, email, celular, classe, "doador", cont++));
			break;
		default:
			throw new IllegalArgumentException("Entrada invalida: opcao de classe invalida.");
		}
		return id;
	}
	
	public String pesquisaUsuarioPorId(String id) {
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		return usuarios.get(id).toString();
	}
	
	public void removeUsuario(String id) {
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		usuarios.remove(id);
	}
	
	public String atualizaUsuario(String id, String nome, String email, String celular) {
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		return usuarios.get(id).atualizaUsuario(nome, email, celular);
	}
	
	public String pesquisaUsuarioPorNome(String nome) {
		if(nome == null || nome.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: nome nao pode ser vazio ou nulo.");
		List<Usuario> users = new ArrayList<Usuario>();
		boolean userNaoExiste = true;
		for(Usuario u : usuarios.values()) {
			if(u.getNome().equals(nome)) {
				users.add(u);
				userNaoExiste = false;
			}
		}
		if (userNaoExiste)
			throw new IllegalArgumentException("Usuario nao encontrado: " + nome + ".");
		Collections.sort(users);
		return editaLista(users);
	}
	
	private String editaLista(List<Usuario> listaDeUsuario) {
		String users = "";
		for(Usuario u : listaDeUsuario) {
			users += u.toString() + " | ";
		}
		return users.substring(0, users.length()-3);
	}
	
	public void adicionaItemDeUsuario(int idUsuario, int idItem, int quantidade, Descricao descricao, String data, List<String> tags) {
		this.usuarios.get(idUsuario).adicionaItem(idItem, quantidade, descricao, data, tags);
	}
	
	public void atualizaItem(int idUsuario, int idItem, List<String> novasTags, int novaQuantidade) {
		this.usuarios.get(idUsuario).atualizaItem(idItem, novasTags, novaQuantidade);
	}
	
	//erick
	public Map<String, Usuario> getUsuarios() {
		return usuarios;
	}
	
	
}