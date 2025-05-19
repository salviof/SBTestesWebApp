/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes;

import br.org.coletivojava.erp.comunicacao.transporte.ERPTipoCanalComunicacao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringNomeArquivosEDiretorios;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoFormulario;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoGerenciarEntidade;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.UtilSBCoreArquivoTexto;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.UtilSBCoreArquivos;
import com.super_bits.modulosSB.SBCore.modulos.comunicacao.FabTipoComunicacao;
import com.super_bits.modulosSB.SBCore.modulos.comunicacao.FabTipoRespostaComunicacao;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabricaAcoes;
import com.super_bits.modulosSB.SBCore.modulos.view.formulario.ItfFormularioEntidade;

import org.junit.Assert;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author desenvolvedor
 */
public abstract class UtilTestePagina {

    public static final String DIRETORIO_PADRAO_MODULO_WEBAPP_FW_DESENVOLVIMENTO = "/home/superBits/projetos/coletivoJava/source/fw/SBWebPaginas";

    public static boolean temPastaModuloNoProjeto() {
        String pastaModulo = SBCore.getCaminhoDesenvolvimento() + "/src/main/webapp/site/modulos";
        return UtilSBCoreArquivos.isArquivoExiste(pastaModulo);
    }

    private static String getPastaExemplos() {
        String pastaExemplo = SBCore.getCaminhoDesenvolvimento() + "/src/main/webapp/exemplos";
        return pastaExemplo;
    }

    private static boolean eUmaPaginaDoSistema(ItfAcaoFormulario pAcao) {
        return pAcao.getXhtml().contains("resources");
    }

    private static boolean eUmaPaginaModular(ItfAcaoFormulario pAcao) {
        return pAcao.getXhtml().contains("modulos");
    }

    private static void validarXHTMLAcaoFormulario(ItfAcaoFormulario pAcao) {
        // se for uma pagina do sistema sai fora.
        if (eUmaPaginaDoSistema(pAcao)) {
            return;
        }
        // se for uma pagina de modulo, e este não for um projeto do modulo não faça nada
        if (eUmaPaginaModular(pAcao) && !temPastaModuloNoProjeto()) {
            return;
        }
        // outros casos verifica e existencia e sugere a criação.
        // o formulário padrão para cada tipo de pagina, deve ficar em /webbapp/exemplos,
        // ou seja você pode der uma pagina padrão diferente para cada projeto, legal não?
        //

        String caminhoCompletoArquivoFormulario = SBCore.getServicoVisualizacao().getCaminhoWebAppDeveloper() + pAcao.getXhtml();
        if (!UtilSBCoreArquivos.isArquivoExiste(caminhoCompletoArquivoFormulario)) {
            System.out.println("Formulário não encontrado:");
            System.out.println(caminhoCompletoArquivoFormulario);
            String arquivoformularioModelo = getPastaExemplos() + "/formularioPadrao.xhtml";
            switch (pAcao.getTipoAcaoGenerica()) {

                case FORMULARIO_NOVO_REGISTRO:
                case FORMULARIO_EDITAR:
                case FORMULARIO_VISUALIZAR:
                    arquivoformularioModelo = getPastaExemplos() + "/editar.xhtml";
                    break;
                case FORMULARIO_LISTAR:
                    arquivoformularioModelo = getPastaExemplos() + "/listar.xhtml";
                    break;

                case GERENCIAR_DOMINIO:
                    arquivoformularioModelo = getPastaExemplos() + "/gerenciar.xhtml";
                    break;

            }

            String mensagemComunicacao = "O XHTML da ação  " + pAcao.getNomeUnico() + "  não foi encontrado, deseja criar o arquivo de maneira automática ? \\n \"\n"
                    + "O arquivo será criado em: " + caminhoCompletoArquivoFormulario + " \\n\"\n"
                    + "A partir de uma cópia de: " + arquivoformularioModelo;
            if (SBCore.getServicoComunicacao().aguardarRespostaComunicacao(ERPTipoCanalComunicacao.INTRANET_BLOQUEIO_TELA.getRegistro(),
                    SBCore.getServicoComunicacao().gerarComunicacaoSistema_UsuarioLogado(FabTipoComunicacao.PERGUNTAR_SIM_OU_NAO,
                            mensagemComunicacao),
                    0, FabTipoRespostaComunicacao.PERSONALIZADA) == FabTipoRespostaComunicacao.SIM) {

                if (!UtilSBCoreArquivos.isArquivoExiste(arquivoformularioModelo)) {
                    throw new UnsupportedOperationException("Não será possível criar uma pagina para seu projeto baseado em um modelo \n "
                            + "O arquivo xhtml modelo não foi encontrado em: " + arquivoformularioModelo + " \n  "
                            + "se você está perdido, e não tem a mínima ideia de como você pode criar estes arquivos, não entre em pânico, \n "
                            + "você pode encontrar uma pasta de exemplos como referencia no projeto webapp do SuperBitsWpStarter localizado em \n "
                            + "(/home/superBits/projetos/Super_Bits/source/SuperBits_FrameWork/SuperBitsWPStarter/)");
                }
                if (!UtilSBCoreArquivos.criarDiretorioParaArquivo(caminhoCompletoArquivoFormulario)) {
                    throw new UnsupportedOperationException("não foi possícel criar o diretorio para " + UtilSBCoreStringNomeArquivosEDiretorios.getDiretorioArquivo(caminhoCompletoArquivoFormulario));
                }
                if (!UtilSBCoreArquivos.copiarArquivos(arquivoformularioModelo, caminhoCompletoArquivoFormulario)) {
                    throw new UnsupportedOperationException(
                            "O sistema tentou criar o arquivo de formulario para " + pAcao.getNomeUnico()
                            + "mas um erro imprevisto aconteceu! \n "
                            + "nessas horas não se esqueça: quem debuga usa a força para o bem \n");
                }
            }
        }
        //   throw new UnsupportedOperationException("O arquivo xhtml da ação " + pAcao.getNomeUnico() + " não foi encontrado em " + SBWebPaginas.getCaminhoWebAppDeveloper() + pAcao.getXhtml());

    }

    public static void testaAcaoFormulario(ItfAcaoFormulario pAcao) {

        Assert.assertNotNull(pAcao);

        Assert.assertNotNull(" A ação não foi definida, impossivel testar ação de formulario em " + pAcao.getNomeUnico(), pAcao);
        Assert.assertNotNull(" O xhtml do formulário não foi definido para " + pAcao.getNomeUnico(), pAcao.getXhtml());
        Assert.assertNotNull(" O xhtml do formulário não foi definido " + pAcao.getNomeUnico(), pAcao.getXhtml());

        if (pAcao.getXhtml().equals("/resources/SBComp/SBSystemPages/naoimplementado.xhtml")) {
            throw new UnsupportedOperationException("O xhtml da ação" + pAcao.getNomeUnico() + " está definido como não implementado");
        }

        validarXHTMLAcaoFormulario(pAcao);

    }

    public static void testaconfigIcone(ItfFabricaAcoes pAcaoDoSistema) {

        String icone = pAcaoDoSistema.getRegistro().getIconeAcao();

        Assert.assertNotNull("O Icone da acao " + pAcaoDoSistema.getRegistro().getNomeUnico() + " esta nulo", icone);
        Assert.assertNotSame("O Icone da acao " + pAcaoDoSistema.getRegistro().getNomeUnico() + " esta nulo", "");

        if (!(icone.startsWith("fa ") || icone.startsWith("icon"))) {
            throw new UnsupportedOperationException("O ícone  da ação " + pAcaoDoSistema.getRegistro().getNomeUnico() + icone + " deveria começar com [fa ] (Para os fontawsome) ou [icon] (para os icones nativos)");
        }
        if (icone.startsWith("fa ")) {

            String iconeSemInicio = icone.substring(3);
            String iconeClassFOntAnsome = iconeSemInicio.split(" ")[0];
            String arquivoCSSFOntAnsome = "/home/superBits/projetos/coletivoJava/source/fw/SBWebPaginas/src/main/resources/META-INF/resources/fontAwesome/css/font-awesome.css";
            Assert.assertTrue("O arquivo font-awesome.css não foi encontrado no sistema", UtilSBCoreArquivos.isArquivoExiste(arquivoCSSFOntAnsome));

            if (!UtilSBCoreArquivoTexto.isTemPalavraNoArquivo(arquivoCSSFOntAnsome, iconeClassFOntAnsome)) {

                throw new UnsupportedOperationException("O ícone  da ação " + pAcaoDoSistema.getRegistro().getNomeUnico() + " com nome [" + iconeSemInicio + "] não foi encontrado no arquivo css do FontAswome e");
            }
        }

    }

    public static void testaConfigPaginaBasico(ItfFormularioEntidade pagina) throws Throwable {
        String classepagina = "PAgina NULO ENVIADA";
        try {

            if (pagina == null) {
                throw new UnsupportedOperationException("enviado pagina nula para testar config basica");
            }
            classepagina = pagina.getClass().getSimpleName();
            if (!classepagina.equals("PaginaSimples")) {
                try {
                    ItfAcaoGerenciarEntidade acaoGerenciarDominio = pagina.getAcaoVinculada();
                    UtilTestePagina.testaAcaoFormulario((ItfAcaoFormulario) acaoGerenciarDominio);
                    assertNotNull("ação vinculada", acaoGerenciarDominio);

                    UtilTestePagina.testaconfigIcone(acaoGerenciarDominio.getEnumAcaoDoSistema());
                } catch (Throwable t) {
                    throw new UnsupportedOperationException("Erro validando Ação gerenciar dominio", t);

                }
            }

            pagina.getTitulo();
            //  pagina.getUrlPadrao();
            //  pagina.getRecursoXHTML();
            //  pagina.getLinkRotulo();
            //  pagina.getParametrosURL();

            assertNotNull("Titulo retornou nulo para:" + pagina.getClass().getSimpleName(), pagina.getTitulo());
            assertNotNull("URL retornou nulo para:" + pagina.getClass().getSimpleName(), pagina.getTitulo());
            assertNotNull("link rotulo retornou nulo para:" + pagina.getClass().getSimpleName(), pagina.getTitulo());
            assertNotNull("XHTML da pagina retornou  nulo para:" + pagina.getClass().getSimpleName(), pagina.getTitulo());

            if (!pagina.getClass().getSimpleName().equals("PaginaSimples")) {
                System.out.println("Anotacao para" + pagina.getTitulo() + ":" + pagina.getAcaoVinculada());
                assertNotNull("Ação vinculada retornou nulo para:" + pagina.getClass().getSimpleName(), pagina.getAcaoVinculada());
            }

        } catch (Throwable t) {

            throw new UnsupportedOperationException("Erro validando " + classepagina, t);

        }
    }

}
