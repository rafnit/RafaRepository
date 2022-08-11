package com.projeto.contas.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.contas.model.Conta;
import com.projeto.contas.model.enums.TipoConta;
import com.projeto.contas.service.ContasService;

@Controller
@RequestMapping("/contas")
public class ContasController {
	
	private static final String CADASTRO_VIEW = "contas-cadastrar";
	
	private List<Conta> lista = new ArrayList<Conta>();
	
	@Autowired
	private ContasService contasService;
	
	// RequestMapping ANOTA O METODO QUE VAI SER CHAMADO AO COLOCAR O ENDEREÇO NO BROWSER
	// O REQUESMAPPING DA CLASSE CONCATENA COM O DO METODO, FICANDO: /CONTAS/NOVO
	@RequestMapping("/cadastro")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Conta());
		return mv;
	}
	
	
	@RequestMapping(method = RequestMethod.POST)
	/* SPRING CONSEGUE RECEBER O MEU OBJETO 'CONTA' SE TODOS OS ELEMENTOS DA PAGINA ESTIVEREM COM A ANOTAÇÃO
	 * 'NAME' EM CADA ELEMENTO, E COM A DESCRICAO DESSE NOME IDENTICA AO NOME DO ATRIBUTO DA CLASSE 'CONTA'
	 * EXEMPLO: private Date dataInicio
	 * O OBJETO NA TELA PRECISA TER O NAME 'dataInicio'
	 * 
	 * @Validated - SPRING VAI VALIDAR O OBJETO DE ACORDO COM AS REGRAS ANOTADAS NO OBJETO
	 * 
	 * Errors - DENTRO DELE TEM O ERRO DAS VALIDACOES
	*/ 
	public String salvar(@Validated Conta conta, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return CADASTRO_VIEW;
		}
		
		try {
			contasService.salvar(conta);
			
			attributes.addFlashAttribute("mensagem", "Conta " + conta.getCodigo() + " cadastrada com sucesso");
			
			return "redirect:/contas/cadastro";
			
		} catch (IllegalArgumentException e) {
			errors.rejectValue("CAMPO_DATA", null, e.getMessage());
			return CADASTRO_VIEW;
		}
	}
	
	
	@RequestMapping("/pesquisa")
	public ModelAndView pesquisar(Conta conta) {
		lista = contasService.pesquisar();
		
		ModelAndView mv = new ModelAndView("contas-pesquisar");
		
		mv.addObject("listaConta", lista);
		
		calculaTotais(mv);
		
		return mv;
	}
	
	
	/*
	 * @PathVariable - ANOTA QUE A VARIAVEL VEM DO PATH (ATRAVES DO REQUESTMAPPING)
	*/
	@RequestMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Conta conta) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		
		mv.addObject(conta);
		
		return mv;
	}
	
	
	@RequestMapping(value = "{codigo}", method = RequestMethod.DELETE)
	public String excluir(@PathVariable Integer codigo, RedirectAttributes attributes) {
		contasService.excluir(codigo);
		
		attributes.addFlashAttribute("mensagem", "Conta " + codigo + " excluída com sucesso");
		
		return "redirect:/contas/pesquisa";
	}
	
	
	@RequestMapping(value = "/executar/{codigo}")
	public ModelAndView executar(@PathVariable Integer codigo) {
		contasService.executar(codigo);
		
		lista = contasService.pesquisar();
		
		ModelAndView mv = new ModelAndView("contas-pesquisar");
		
		mv.addObject("listaConta", lista);
		
		calculaTotais(mv);
		
		return mv;
	}
	
	
	private void calculaTotais(ModelAndView mv) {
		BigDecimal receitas = lista.stream()
				.filter(c -> c.getTipo().equals(TipoConta.RECEITA))
				.map(c -> c.getValor())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		BigDecimal despesas = lista.stream()
				.filter(c -> c.getTipo().equals(TipoConta.DESPESA))
				.map(c -> c.getValor())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		mv.addObject("saldo", new DecimalFormat("###,##0.00").format(receitas.add(despesas.negate())));
		
		receitas = lista.stream()
				.filter(c -> c.getTipo().equals(TipoConta.RECEITA) && c.getExecutado())
				.map(c -> c.getValor())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		despesas = lista.stream()
				.filter(c -> c.getTipo().equals(TipoConta.DESPESA) && c.getExecutado())
				.map(c -> c.getValor())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		
		mv.addObject("saldoExecutado", new DecimalFormat("###,##0.00").format(receitas.add(despesas.negate())));
	}
	
	
	@ModelAttribute("listaTipos")
	public List<TipoConta> listaTipos() {
		return Arrays.asList(TipoConta.values());
	}
}
