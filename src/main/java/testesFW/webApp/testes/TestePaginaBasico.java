/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes;

import com.super_bits.modulosSB.SBCore.modulos.view.formulario.ItfFormularioEntidade;
import org.junit.Test;
import testesFW.TesteJunitSBPersistencia;

/**
 *
 * @author desenvolvedor
 */
public abstract class TestePaginaBasico extends TesteJunitSBPersistencia {

    /**
     *
     * Retorna a pagina que será testada, já instanciada
     *
     * @return
     */
    public abstract ItfFormularioEntidade getPaginaBasico();

    @Test
    public void testeAcaoVinculada() {
        ItfFormularioEntidade pagina = null;

    }

}
