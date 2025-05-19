/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsMaiuculoMinusculo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.icones.ItfFabricaIcone;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.icones.itfIcone;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 *
 *
 * @author SalvioF
 */
public class GeradorGetIcones extends GeradorClasseEscopoApp {

    private final Class<? extends ItfFabricaIcone> fabricaIcones;
    private boolean componenteNativo;

    public GeradorGetIcones(Class<? extends ItfFabricaIcone> pFabrica, boolean componenteNativo) {
        super("org.coletivoJava.superBitsFW.webPaginas.constantes.icones", pFabrica.getSimpleName().replace("Fab", "Icone"), DIRETORIO_CODIGO_WEB_PAGINAS);
        fabricaIcones = pFabrica;
    }

    private void adicionaIcone(ItfFabricaIcone pIcone, JavaClassSource pEstruturaClasse) {

        pEstruturaClasse.addProperty(itfIcone.class,
                UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilSBCoreStringFiltros.gerarUrlAmigavel(pIcone.toString())))
                .setMutable(false);

    }

    @Override
    public final JavaClassSource gerarCodigo() {
        getCodigoJava().addImport(fabricaIcones);
        String corpoCosntructor = "";

        for (ItfFabricaIcone pcomp : fabricaIcones.getEnumConstants()) {
            String nomeVariavel = UtilSBCoreStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilSBCoreStringFiltros.gerarUrlAmigavel(pcomp.toString()));
            corpoCosntructor += "this." + nomeVariavel + " = " + fabricaIcones.getSimpleName() + "." + pcomp.toString() + ".getIcone(); ";
        }

        MethodSource<JavaClassSource> constructor = getCodigoJava().addMethod();

        constructor.setBody(corpoCosntructor);
        constructor.setConstructor(true);
        for (ItfFabricaIcone pIcone : fabricaIcones.getEnumConstants()) {
            adicionaIcone(pIcone, getCodigoJava());

        }

        return getCodigoJava();
    }

}
