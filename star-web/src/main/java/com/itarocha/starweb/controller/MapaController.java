package com.itarocha.starweb.controller;

import java.awt.PageAttributes.MediaType;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itarocha.starweb.model.Cliente;
import com.itarocha.starweb.model.Interpretacao;
import com.itarocha.starweb.model.UnidadeFederacao;
import com.itarocha.starweb.service.GeradorPdfService;
import com.itarocha.starweb.service.MapaService;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import br.itarocha.star.DecoradorMapa;
import br.itarocha.star.Mapa;
import br.itarocha.star.MapaBuilder;
import br.itarocha.star.MapeadorCidades;
import br.itarocha.star.StarMain;

@Controller
@RequestMapping("mapa")
public class MapaController {
	
	private static final String PAGINA = "clientes";
	private static final String PAGINA_INDEX = "mapa/index";
	private static final String PAGINA_EDIT = "mapa/edit";
	private static final String PAGINA_CONSULTAS = "consultas/index";
	//private static final String PAGINA_REDIRECT = "redirect:/clientes";
	private static final String PAGINA_REDIRECT = "redirect:/mapa";
	private static final String NOME_CLASSE = "Cliente";
	private static final String RESOURCE_PATH = ".";
	
	
    public static final String DEST = "results/chapter01/text_paragraph_cardo.pdf";
    
    public static final String FONT_ASTRO = "src/main/resources/fonts/AstroDotBasic.ttf";

    
    public static final String FONT_REGULAR = "src/main/resources/fonts/Cardo-Regular.ttf";
    public static final String FONT_BOLD = "src/main/resources/fonts/Cardo-Bold.ttf";
    public static final String FONT_ITALIC = "src/main/resources/fonts/Cardo-Italic.ttf";
    
    @Autowired
    private MapaService servico;
    
	/*
	@Autowired
	private ClienteService service;

	@Autowired
	private ConsultaService consultasService;
	
	@Autowired
	private Mensagens mensagens;
	*/
	
	@RequestMapping
	public ModelAndView pesquisar()
	{
		ModelAndView mv = new ModelAndView(PAGINA_INDEX);
		
		
		//mv.addObject("lista",page);
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
		Cliente v = new Cliente();
		mv.addObject("model", v);
		return mv;
	}
	
	//https://memorynotfound.com/spring-mvc-download-file-examples/
	//http://www.baeldung.com/java-pdf-creation
	//http://www.vogella.com/tutorials/JavaPDF/article.html
	//https://developers.itextpdf.com/examples/itext-action-second-edition/chapter-11
	
	/*
	@RequestMapping("/edit/{id}")
	public ModelAndView editar(@PathVariable("id") Cliente model){
		ModelAndView mv = new ModelAndView(PAGINA_EDIT);
		mv.addObject("model", model);
		return mv;
	}
	*/
	
	@PostMapping()
	public String salvar(	@Valid @ModelAttribute("model") Cliente model, 
							BindingResult bindingResult, 
							final RedirectAttributes attributes,
							HttpSession session)
	{
		if (bindingResult.hasErrors()){
			return PAGINA_EDIT;
		}
		
		model.setNome(model.getNome().toUpperCase());
		model.setCidade(model.getCidade().toUpperCase());
		
		List<Interpretacao> lista = constroiMapa(model);
		
		try{
			//service.gravar(objeto);
			session.setAttribute("model", model);
			session.setAttribute("lista", lista);
			//attributes.addFlashAttribute("model","Itamar");
			//attributes.addFlashAttribute("mensagem", mensagens.getMensagemGravacaoSucesso(NOME_CLASSE));
			return PAGINA_REDIRECT;
		}catch(IllegalArgumentException e){
			return PAGINA_EDIT;
		}
	}

	/*
	@RequestMapping( method = RequestMethod.POST, 
	    value = MapaController.RESOURCE_PATH + "/file", 
	    headers = "content-type=application/json" )
	public void export( HttpServletResponse response ) 
	    throws IOException {
	    String myString = "Hello munnnnnnnndoooo \n Como está tudo aí? \n Tudo beleza?";
	    response.setContentType("text/plain");
	    response.setHeader("Content-Disposition","attachment;filename=myFile.txt");
	    ServletOutputStream out = response.getOutputStream();
	    out.println(myString);
	    out.flush();
	    out.close();
	}	
	*/
	private List<Interpretacao> constroiMapa(Cliente model){
		List<Interpretacao> retorno =  new ArrayList<Interpretacao>();
		Mapa mapa = MapaBuilder.getInstance().build(model.getNome(), model.getDataNascimento(), model.getHoraNascimento(), model.getCidade(), model.getUf());
		if (mapa != null) {
			String json = new DecoradorMapa(mapa).getJSON();
			System.out.println(json);
			
			try {
				GeradorPdfService g = new GeradorPdfService();
				retorno =  g.createArquivo(servico, mapa, json);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*
			String dest = "text.pdf";
			PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
			Document document = new Document(pdf);
			//BufferedReader br = new BufferedReader(new FileReader(SRC));
			String line;
			
			while ((line = br.readLine()) != null) {
				document.add(new Paragraph(line));
			}
			
			document.close();			
			*/
			
			/*
			PDDocument document = new PDDocument();
			PDPage page = new PDPage();
			document.addPage(page);
			 
			PDPageContentStream contentStream = new PDPageContentStream(document, page);
			 
			contentStream.setFont(PDType1Font.COURIER, 12);
			contentStream.beginText();
			contentStream.showText("Hello World");
			contentStream.endText();
			contentStream.close();
			 
			document.save("pdfBoxHelloWorld.pdf");
			document.close();			
			*/
			
			//return json;
		} else {
			System.out.println("MAPA ESTÁ NULO");
		}	
		return retorno;
	}
	
	 
	public void buildFile() {
        BufferedWriter writer = null;
        try {
            //create a temporary file
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(timeLog);

            // This will output the full path where the file will be written to...
            System.out.println(logFile.getCanonicalPath());

            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("Hello world!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e) {
            }
        }
    }	
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
	
	@ModelAttribute("todosUF")
	public List<UnidadeFederacao> todosUF(){
		return Arrays.asList(UnidadeFederacao.values());
	}
	
	
}


//<td th:text="${#dates.format(item.dataVisita, 'dd/MM/yyyy')}"></td>



