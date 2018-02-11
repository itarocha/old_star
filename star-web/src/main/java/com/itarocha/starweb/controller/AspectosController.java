package com.itarocha.starweb.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itarocha.starweb.model.MapaPlanetaAspecto;
import com.itarocha.starweb.model.PlanetaSigno;
import com.itarocha.starweb.model.TipoAspecto;
import com.itarocha.starweb.model.TipoPlaneta;
import com.itarocha.starweb.model.TipoRelacao;
import com.itarocha.starweb.model.TipoSigno;
import com.itarocha.starweb.repository.MapaPlanetaAspectoRepository;
import com.itarocha.starweb.repository.PlanetaSignoRepository;


@Controller
@RequestMapping("aspectos")
public class AspectosController {
	
	private static final String PAGINA = "aspectos";
	private static final String PAGINA_INDEX = "aspectos/index";
	private static final String PAGINA_EDIT = "aspectos/edit";
	private static final String PAGINA_CONSULTAS = "aspectos/index";
	//private static final String PAGINA_REDIRECT = "redirect:/clientes";
	private static final String PAGINA_REDIRECT = "redirect:/aspectos";
	private static final String NOME_CLASSE = "MapaPlanetaAspecto";
	
	
	@Autowired
	private MapaPlanetaAspectoRepository service;
	
	/*

	@Autowired
	private ConsultaService consultasService;
	
	@Autowired
	private Mensagens mensagens;
	*/
	
	@RequestMapping
	public ModelAndView all()
	{
		ModelAndView mv = new ModelAndView(PAGINA_INDEX);
		
		List<MapaPlanetaAspecto> lista = service.findAll();
		mv.addObject("lista",lista);
		
		return mv;
	}
	
	
	/*
	private boolean userHasAuthority(String authority, Authentication auth)
	{
	    for (GrantedAuthority grantedAuthority :  auth.getAuthorities()) {
	        if (authority.equals(grantedAuthority.getAuthority())) {
	            return true;
	        }
	    }
	    return false;
	}	
	*/
	
	@RequestMapping("/new")
	public ModelAndView novo(){
		ModelAndView mv = new ModelAndView(PAGINA_EDIT);
		MapaPlanetaAspecto model = new MapaPlanetaAspecto();
		mv.addObject("model", model);
		return mv;
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView editar(@PathVariable("id") MapaPlanetaAspecto model){
		ModelAndView mv = new ModelAndView(PAGINA_EDIT);
		mv.addObject("model", model);
		return mv;
	}
	
	@PostMapping()
	public String salvar(	@Valid @ModelAttribute("model") MapaPlanetaAspecto model, 
							BindingResult bindingResult, 
							final RedirectAttributes attributes,
							HttpSession session)
	{
		
		Long idAspectoMestre = (model.getAspectoMestre() != null) ? model.getAspectoMestre().getId() : 0;
		//Long idCliente = (objeto.getCliente() != null) ? objeto.getCliente().getId() : 0;
		
		
		
		//model.getAspectoMestre()
		if (TipoRelacao.DETALHE.equals(model.getTipoRelacao()) && (idAspectoMestre.equals(model.getId()))) {
			ObjectError error = new ObjectError("aspectoMestre","Você não pode fazer auto referência");
			bindingResult.addError(error);		
		}

		/*
		if (objeto.getPctGorduraCorporal() != null && objeto.getPctGorduraCorporal().doubleValue() >= 100) {
			ObjectError error = new ObjectError("pctGorduraCorporal","Percentual de gordura corporal deve ser inferior a 100%");
			bindingResult.addError(error);		
		}		
		*/
		
		if (bindingResult.hasErrors()){
			return PAGINA_EDIT;
		}

		if (TipoRelacao.MESTRE.equals(model.getTipoRelacao())) {
			model.setAspectoMestre(null);
		}
		
		if (TipoRelacao.DETALHE.equals(model.getTipoRelacao())) {
			model.setTexto(null);
		}
		
		//model.setNome(model.getNome().toUpperCase());
		//model.setCidade(model.getCidade().toUpperCase());
		
		//constroiMapa(model);
		
		try{
			service.save(model);
			
			//service.gravar(objeto);
			//session.setAttribute("model", model);
			//attributes.addFlashAttribute("model","Itamar");
			//attributes.addFlashAttribute("mensagem", mensagens.getMensagemGravacaoSucesso(NOME_CLASSE));
			return PAGINA_REDIRECT;
		}catch(IllegalArgumentException e){
			return PAGINA_EDIT;
		}
	}
	
	/*
	private Mapa constroiMapa(Cliente model){
		Mapa mapa = MapaBuilder.getInstance().build(model.getNome(), model.getDataNascimento(), model.getHoraNascimento(), model.getCidade(), model.getUf());
		if (mapa != null) {
			String json = new DecoradorMapa(mapa).getJSON();
			System.out.println(json);
			//return json;
		} else {
			System.out.println("MAPA ESTÁ NULO");
		}	
		return mapa;
	}
	*/
	
	/*
	@RequestMapping("/consultas/{id}")
	public ModelAndView consultas(@PathVariable("id") Cliente model){
		ModelAndView mv = new ModelAndView(PAGINA_CONSULTAS);
		List<Consulta> consultas = consultasService.buscarConsultasPorCliente(model);
		
		consultas.sort((c1, c2) -> c1.getDataCalculada().compareTo(c2.getDataCalculada()));
		
		List<ConsultaGrafico> grafico = consultasService.getGrafico(model);

		mv.addObject("graficoData", grafico);
		mv.addObject("model", model);
		mv.addObject("consultas", consultas);
		return mv;
	}

	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public String excluir(@PathVariable Long id, final RedirectAttributes attributes){
		service.excluir(id);
		attributes.addFlashAttribute("mensagem", "Cliente excluído");
		return PAGINA_REDIRECT;
	}

	@RequestMapping(value="/consultas/delete/{id}", method=RequestMethod.DELETE)
	public String excluirConsulta(@PathVariable Long id, final RedirectAttributes attributes){
		Consulta c = consultasService.findById(id);
		
		if (c != null) {
			//System.out.println(c.getId() + " - " +c.getDataAgenda());
			consultasService.excluir(id);
			attributes.addFlashAttribute("mensagem", "Consulta excluída");
			String retorno = String.format("redirect:/clientes/consultas/%d", c.getCliente().getId());
			System.out.println(retorno);
			return retorno;
		} else {
			attributes.addFlashAttribute("mensagem", "Consulta não encontrada");
			return "redirect:/clientes";
		}
	}
	
	@ModelAttribute("todosSexo")
	public List<Sexo> todosSexo(){
		return Arrays.asList(Sexo.values());
	}
	*/
	
	@ModelAttribute("listaAspectos")
	public List<TipoAspecto> listaTipoAspecto(){
		return Arrays.asList(TipoAspecto.values());
	}
	
	@ModelAttribute("listaPlanetas")
	public List<TipoPlaneta> listaPlaneta(){
		return Arrays.asList(TipoPlaneta.values());
	}
	
	@ModelAttribute("listaRelacoes")
	public List<TipoRelacao> listaRelacoes(){
		return Arrays.asList(TipoRelacao.values());
	}
	
	@ModelAttribute("listaMaster")
	public List<MapaPlanetaAspecto> listaMaster(){
		// Where mestre
		return service.findAllMaster();
	}
	
}


//<td th:text="${#dates.format(item.dataVisita, 'dd/MM/yyyy')}"></td>



