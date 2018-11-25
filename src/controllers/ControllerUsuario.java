package controllers;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import entidades.Descritor;
import entidades.Usuario;

/**
 * Classe que representa o controlador dos usu�rios cadastrados no sistema.
 * @author
 *
 */
public class ControllerUsuario {
	
	private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();
	private int cont = 0;
	private ControllerDescritor controllerDescritor = new ControllerDescritor();
	/**
	 * Construtor da classe ControllerUsuario.
	 */
	public ControllerUsuario() {
		this.usuarios = new HashMap<String, Usuario>();
	}

	/**
	 * M�todo respons�vel por adicionar um doador no sistemma. Possui exce��es para garantir que os par�metros inseridos
	 * n�o ir�o afetar no funcionamento do programa.
	 * @param id Documento de identifica��o do doador.
	 * @param nome Nome do doador.
	 * @param email E-mail do doador.
	 * @param celular Celular do doador.
	 * @param classe Classe do doador.
	 * @return Retorna o n�mero de identifica��o do doador.
	 */
	public String adicionaDoador(String id, String nome, String email, String celular, String classe) {	
		if(id == null || id.trim().equals("")) 
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		
		if(usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario ja existente: " + id + ".");
		
		if(classe == null || classe.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: classe nao pode ser vazia ou nula.");
		
		switch(classe) {
		
		case "PESSOA_FISICA": 
		case "ONG": 
		case "IGREJA": 
		case "ORGAO_PUBLICO_MUNICIPAL": 
		case "ORGAO_PUBLICO_FEDERAL": 
		case "ORGAO_PUBLICO_ESTADUAL":
		case "ASSOCIA��O": case "SOCIEDADE":
			usuarios.put(id, new Usuario(id, nome, email, celular, classe, "doador", cont++));
			break;
			
		default:
			throw new IllegalArgumentException("Entrada invalida: opcao de classe invalida.");
		}
		return id;
	}
	
	/**
	 * M�todo respons�vel por pesquisar um determinado usu�rio cadastrado no sistema atrav�s do ID. Possui exce��es para garantir que os 
	 * par�metros inseridos n�o ir�o afetar no funcionamento do programa.
	 * @param id N�mero de identifica��o do usu�rio
	 * @return Retorna o toString do usu�rio.
	 */
	public String pesquisaUsuarioPorId(String id) {
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		
		return usuarios.get(id).toString();
	}
	
	/**
	 * M�todo respons�vel por pesquisar um determinado usu�rio cadastrado no sistema atrav�s do nome. Possui exce��es para garantir que os 
	 * par�metros inseridos n�o ir�o afetar no funcionamento do programa.
	 * @param nome Nome do usu�rio.
	 * @return Retorna o toString do usu�rio.
	 */
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
	
	/**
	 * M�todo respons�vel por atualizar os atributos de um usu�rio. Possui exce��es para garantir que os 
	 * par�metros inseridos n�o ir�o afetar no funcionamento do programa.
	 * @param id N�mero de identifica��o do usu�rio.
	 * @param nome Nome do usu�rio.
	 * @param email E-mail do usu�rio.
	 * @param celular Celular do usu�rio.
	 * @return Retorna o toString do usu�rio.
	 */
	public String atualizaUsuario(String id, String nome, String email, String celular) {
		
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		
		return usuarios.get(id).atualizaUsuario(nome, email, celular);
	}
	
	/**
	 * M�todo respons�vel por remover um usu�rio do sistema atrav�s do ID.
	 * @param id N�mero de identifica��o do usu�rio.
	 */
	public void removeUsuario(String id) {
		if(id == null || id.trim().equals(""))
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		
		if(!usuarios.containsKey(id))
			throw new IllegalArgumentException("Usuario nao encontrado: " + id + ".");
		
		usuarios.remove(id);
	}
	
	/**
	 * M�todo respons�vel por ler um arquivo .csv e cadastrar novos receptores no sistema, assim como atualizar os atributos
	 * dos que j� foram previamente inseridos.
	 * @param caminho
	 * @throws IOException
	 */
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
	
	/**
	 * M�todo respons�vel por cadastrar um item para doa��o no sistema. 
	 * @param idUsuario Documento de identifica��o do usu�rio referente ao item.
	 * @param descritor Descritor que representa o item a ser cadastrado.
	 * @param quantidade Quantidade de itens a serem cadastrados.
	 * @param tags Tags que caracterizam o item.
	 */
	public void cadastraItem(String idDoador, String descritor, int quantidade, String tags) {
		Descritor novoDescritor = new Descritor(descritor);
		
		if (descritor == null || descritor.trim().equals("")) {
			throw new IllegalArgumentException("Entrada invalida: descricao nao pode ser vazia ou nula.");
		}
		
		if (quantidade <= 0) {
			throw new IllegalArgumentException("Entrada invalida: quantidade deve ser maior que zero.");
		}
		
		if (idDoador == null || idDoador.trim().equals("")) {
			throw new IllegalArgumentException("Entrada invalida: id do usuario nao pode ser vazio ou nulo.");
		}
		
		if (!this.usuarios.containsKey(idDoador)) {
			throw new IllegalArgumentException("Usuario nao encontrado: " + idDoador + ".");
		}
		
		
		
	}
	
	public void atualizaItem(String idUsuario, int idItem, List<String> novasTags, int novaQuantidade) {
		this.usuarios.get(idUsuario).atualizaItem(idItem, novasTags, novaQuantidade);
	}

	private String editaLista(List<Usuario> listaDeUsuario) {
		String users = "";
		for(Usuario u : listaDeUsuario) {
			users += u.toString() + " | ";
		}
		return users.substring(0, users.length()-3);
	}
	
}
