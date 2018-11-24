package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import entidades.Descritor;

/**
 * Classe que representa o controlador dos descritores cadastrados no sistema.
 * @author
 *
 */
public class ControllerDescritor {
	private Set<Descritor> descritores;
	
	/**
	 * Construtor da classe ControllerDescritor
	 */
	public ControllerDescritor() {
		this.descritores = new HashSet<>();
	}

	/**
	 * M�todo respons�vel por cadastrar um descritor no sistema. Cont�m uma exce��o para verificar se o par�metro inserido
	 * n�o ir� interferir no funcionamento do programa.
	 * @param descritor Descritor
	 */
	public void cadastraDescritor(Descritor descritor) {
		if (!descritores.contains(descritor)) {
			descritores.add(descritor);
		}
		else {
			throw new IllegalArgumentException("");
		}
	}
	
	public String representacaoDeTodosOsDescritores() {
		ArrayList<String> listaDescritores = new ArrayList<String>();
		for (Descritor descricao : descritores) {
			listaDescritores.add(descricao.toString());
		}
		Collections.sort(listaDescritores);
		return listaDescritores.stream().map(c -> c.toString()).collect(Collectors.joining(" | "));
	}
}	
