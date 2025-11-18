/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoFormulario;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeNormal;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.view.formulario.ItfFormularioEntidade;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import testesFW.TesteJunitSBPersistencia;

/**
 *
 * @author cristopher
 * @param <T> informa a entidade a ser trabalhada no teste
 */
public abstract class TestePaginaEntidade<T> extends TesteJunitSBPersistencia {

    protected ItfFormularioEntidade<T> pagina;

    private ComoAcaoDoSistema acaoAprovarCampanha;
    private ComoAcaoDoSistema acaoReprovarCampanha;

    /**
     * Metodo executado antes do inicio do fluxo de testes, usado por exemplo
     * para efetuar login
     */
    public abstract void configuracoesIniciais();

    public abstract void testesAdicionas();

    @Before
    public void inicio() {
        pagina = definirPagina();

    }

    @Test
    public void testeFluxo() {

        try {
            SBCore.getControleDeSessao().logarComoRoot();
            configuracoesIniciais();
            pagina.listarDados();

            UtilTestePagina.testaAcaoFormulario(pagina.getAcaoVinculada());
            UtilTestePagina.testaconfigIcone(pagina.getAcaoVinculada().getEnumAcaoDoSistema());

            UtilTestePagina.testaAcaoFormulario(pagina.getAcaoListarRegistros());
            UtilTestePagina.testaconfigIcone(pagina.getAcaoListarRegistros().getEnumAcaoDoSistema());

            configurarPesquisa();

            for (ComoAcaoDoSistema acao : pagina.getAcoesRegistros()) {
                if (acao.isUmaAcaoFormulario()) {
                    UtilTestePagina.testaAcaoFormulario((ItfAcaoFormulario) acao);
                }
                UtilTestePagina.testaconfigIcone(acao.getEnumAcaoDoSistema());
            }

            if (pagina.isTemNovo()) {
                criarNovaEntidade();
                UtilTestePagina.testaAcaoFormulario(pagina.getAcaoNovoRegistro());
                UtilTestePagina.testaconfigIcone(pagina.getAcaoNovoRegistro().getEnumAcaoDoSistema());
            }
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
        try {
            pesquisar();

        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
        try {
            if (pagina.isTemEditar()) {
                UtilTestePagina.testaAcaoFormulario(pagina.getAcaoEditar());
                UtilTestePagina.testaconfigIcone(pagina.getAcaoEditar().getEnumAcaoDoSistema());
                editarDados();
            }
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }

        try {
            if (pagina.isTemVisualizar()) {
                visualisarDados();
            }
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }

        try {
            if (pagina.isTemAlterarStatus()) {
                alterarStatus();

                UtilTestePagina.testaconfigIcone(pagina.getAcaoAlterarStatus().getEnumAcaoDoSistema());
            }

        } catch (Throwable t) {
            lancarErroJUnit(t);
        }

        try {
            testesAdicionas();
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
    }

    public abstract void configurarDAdosInsert();

    public abstract void configurarDadosEditar();

    public abstract void configurarPesquisa();

    public abstract ItfFormularioEntidade definirPagina();

    public void visualisarDados() {
        pagina.setAcaoSelecionada((ComoAcaoDoSistema) pagina.getAcaoVisualizar());
        pagina.executarAcao(pagina.getEntidadesListadas().get(0));

        assertTrue("O XHTML para visualizar um registro não foi configurado ao executar a ação visualizar", pagina.getXhtmlAcaoAtual().equals(pagina.getAcaoVisualizar().getXhtml()));
        assertTrue(" entidade selecionada está nula, durante a solicitação de visualização", pagina.getEntidadeSelecionada() != null);
        assertTrue("O boolean is novo registro precisa estar falso durante a visualização", !pagina.isNovoRegistro());
        assertTrue("O boolean  que permite a edição deve ser falso durante a visualização", !pagina.isPodeEditar());

    }

    public void criarNovaEntidade() {
        try {

            // A pagina de gerenciar comprador pega a ação solicitada e define na ação selecionada.(Define a ação selecionada como ação de novo registro)
            pagina.setAcaoSelecionada(pagina.getAcaoNovoRegistro());
            // A pagina executa a ação selecionada.
            pagina.executarAcao(pagina.getEntidadeSelecionada());
            assertTrue("A entidade selecionada está nula após executar a ação Cadastrar novo registro, Verique o case do metodo executarAcao(), e set o registro selecionado com uma nova instancia utilizando setEntidadeSelecionad(new EntidadeVinculadaApagina)", pagina.getEntidadeSelecionada() != null);
            //FabAcaoCadastros.COMPRADOR_NOVO.getAcaoDoSistema().getXHTMLAcao();
            // Testa se o xhtml foi definido como xhtml da ação de novo registro
            assertTrue("O XHTML para cadastro de novo registro está nulo", pagina.getXhtmlAcaoAtual() != null);
            assertTrue("O XHTML para cadastrar um novo registro não foi configurado ao executar a ação com um novo registro", pagina.getXhtmlAcaoAtual().equals(pagina.getAcaoNovoRegistro().getXhtml()));
            assertTrue(" entidade selecionada não foi instanciado", pagina.getEntidadeSelecionada() != null);
            assertTrue("O boolean is novo registro deve ser true ao executar a ação novo registro", !pagina.isNovoRegistro());
            assertTrue("O boolean  que permite edição deve ser true ao executação a ação novo registro", pagina.isPodeEditar());
            // int quantidadeAnterior = pagina.getEntidadesListadas().size();
            // Define os valores para o usuario do novo comprador
            configurarDAdosInsert();

            // Define que a ação selecionada é salvar alterações
            pagina.setAcaoSelecionada(pagina.getAcaoSalvarAlteracoes());
            pagina.executarAcao(pagina.getEntidadeSelecionada());

            assertTrue("O boolean is novo registro deve ser falso  apos salvar um novo registo", !pagina.isNovoRegistro());
            assertTrue("O boolean  que permite edição deve ser Falsee ao salvar um novo registro", !pagina.isPodeEditar());

            T entidadeCadastrada = pagina.getEntidadeSelecionada();
            int quantidadePosterior = pagina.getEntidadesListadas().size();
            //assertTrue("A entidade selecionada não foi cadastrada", entidadeCadastrada != null);

            if (pagina.isTemPesquisa()) {
                //assertTrue(" A  lista de registros/entidades deve ser atualizada com um único registro apos o cadastro com sucesso (Verifique o procedore que atualiza a lista, e o parametro tem pesquisa foi configurado corretamente)", pagina.getEntidadesListadas().size() == 1);
                //assertTrue("A entidade exibida na lista, não parece ser a entidade que foi cadastrada", pagina.getEntidadesListadas().get(0).equals(entidadeCadastrada));
            } else {
                //assertTrue("O novo registro não está aparecendo na lista, (Verifique o procedore que atualiza a lista, e o parametro tem pesquisa foi configurado corretamente)", quantidadePosterior > quantidadeAnterior);

            }

        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
    }

    public void pesquisar() {
        try {
            configurarPesquisa();
            pagina.listarDados();

            assertTrue("Nenhum registro foi selecionado na pesquisa", pagina.getEntidadesListadas().size() > 0);
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
    }

    public void editarDados() {
        try {
            // Usuario clieca no botao editar dentro do primeiro registro que é o entidadesListadas.get(0)
            pagina.setAcaoSelecionada((ComoAcaoDoSistema) pagina.getAcaoEditar());

            ComoEntidadeSimples entidadeQueOUsuarioIraSelecionar = (ComoEntidadeSimples) pagina.getEntidadesListadas().get(0);
            pagina.executarAcao((T) entidadeQueOUsuarioIraSelecionar);

            // espera-se que o registro selecionado agora seja o primeiro registro da lista
            assertTrue("O boolean is novo registro deve ser igual a false", !pagina.isNovoRegistro());
            assertTrue("O boolean  que permite edição não foi modificado", pagina.isPodeEditar());

            assertTrue("O XHTML para Alterar um registro não foi configurado ao executar a ação Alterar Registro", pagina.getXhtmlAcaoAtual().equals(pagina.getAcaoEditar().getXhtml()));

            assertTrue("O comprador Selecionado está nulo para edição!", pagina.getEntidadeSelecionada() != null);
            //assertTrue(" A entidade Selecionada não parece ser a que foi configurada ao executar a ação", pagina.getEntidadeSelecionada().equals(pagina.getEntidadesListadas().get(0)));

            ComoEntidadeSimples entidadeSelecionada = (ComoEntidadeSimples) pagina.getEntidadeSelecionada();

            String nomeAntigo = ((ComoEntidadeSimples) pagina.getEntidadeSelecionada()).getNomeCurto();
            String nomenovo = "[EDIT]" + nomeAntigo;
            Long idEntidadeSelecionada = ((ComoEntidadeSimples) pagina.getEntidadeSelecionada()).getId();
            try {
                ((ComoEntidadeSimples) pagina.getEntidadeSelecionada()).getCampoByNomeOuAnotacao(FabTipoAtributoObjeto.NOME.toString()).setValor(nomenovo);
                nomenovo = ((ComoEntidadeSimples) pagina.getEntidadeSelecionada()).getNomeCurto();
            } catch (Throwable t) {
                fail("Ocorreu um erro ao tentar configurar um novo valor para o nome curto da entidade");
                lancarErroJUnit(t);
            }

            pagina.setAcaoSelecionada(pagina.getAcaoSalvarAlteracoes());
            pagina.executarAcao(pagina.getEntidadeSelecionada());
            ComoEntidadeSimples entidadeAlterada = null;
            for (T entidade : pagina.getEntidadesListadas()) {
                if (idEntidadeSelecionada == ((ComoEntidadeSimples) entidade).getId()) {
                    entidadeAlterada = (ComoEntidadeSimples) entidade;
                }
            }

            assertFalse(" O Xhtml de listar dados deveria ser definido apos o registro ser cadastrado com sucesso, e o valor esta nulo", pagina.getXhtmlAcaoAtual() == null);
            assertTrue("O XHTML de listar não foi alterado apos salvar o registro", pagina.getXhtmlAcaoAtual().equals(pagina.getAcaoListarRegistros().getXhtml()));
            assertTrue("A entidade selecionada não foi encontrada na lista de entidades", entidadeAlterada != null);
            assertFalse(" O nome da entidade alterada está nulo", entidadeAlterada.getNomeCurto() == null);
            assertTrue("Os dados do registro 0 da lista de entidades não foram alterados", !entidadeAlterada.getNomeCurto().equals(nomeAntigo));
            assertTrue("O dado alterado do regsitro 0 da lista  de entidades não parece ser o mesmo da alteração realizada", entidadeAlterada.getNomeCurto().equals(nomenovo));

        } catch (Throwable t) {
            lancarErroJUnit(t);
        }

    }

    public void alterarStatus() {

        // Usuario clieca no botao Alterar status dentro do primeiro registro que é o entidadesListadas.get(0)
        ComoEntidadeNormal entidadeQueOUsuarioIraSelecionar = (ComoEntidadeNormal) pagina.getEntidadesListadas().get(0);
        boolean statusAnterior;
        if (entidadeQueOUsuarioIraSelecionar.isAtivo()) {
            statusAnterior = true;

        } else {
            statusAnterior = false;
        }

        pagina.setAcaoSelecionada((ComoAcaoDoSistema) pagina.getAcaoAlterarStatus());
        pagina.executarAcao(pagina.getEntidadesListadas().get(0));
        assertTrue("O status não foi alterado", entidadeQueOUsuarioIraSelecionar.isAtivo() != statusAnterior);

    }

    public ComoAcaoDoSistema getAcaoAprovarCampanha() {
        return acaoAprovarCampanha;
    }

    public ComoAcaoDoSistema getAcaoReprovarCampanha() {
        return acaoReprovarCampanha;
    }

}
