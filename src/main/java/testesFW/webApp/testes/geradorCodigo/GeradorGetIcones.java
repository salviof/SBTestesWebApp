/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package testesFW.webApp.testes.geradorCodigo;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringValidador;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringsMaiuculoMinusculo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.icones.ComoFabricaIcone;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.icones.ComoIcone;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

/**
 *
 *
 * @author SalvioF
 */
public class GeradorGetIcones extends GeradorClasseEscopoApp {

    private final Class<? extends ComoFabricaIcone> fabricaIcones;
    private boolean componenteNativo;

    public GeradorGetIcones(Class<? extends ComoFabricaIcone> pFabrica, boolean componenteNativo) {
        super("org.coletivoJava.superBitsFW.webPaginas.constantes.icones", pFabrica.getSimpleName().replace("Fab", "Icone"), DIRETORIO_CODIGO_WEB_PAGINAS);
        fabricaIcones = pFabrica;
    }

    private void adicionaIcone(ComoFabricaIcone pIcone, JavaClassSource pEstruturaClasse) {

        pEstruturaClasse.addProperty(ComoIcone.class,
                UtilCRCStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilCRCStringFiltros.gerarUrlAmigavel(pIcone.toString())))
                .setMutable(false);

    }

    @Override
    public final JavaClassSource gerarCodigo() {
        getCodigoJava().addImport(fabricaIcones);
        String corpoCosntructor = "";

        for (ComoFabricaIcone pcomp : fabricaIcones.getEnumConstants()) {
            String nomeVariavel = UtilCRCStringsMaiuculoMinusculo.getPrimeiraLetraMinuscula(UtilCRCStringFiltros.gerarUrlAmigavel(pcomp.toString()));
            corpoCosntructor += "this." + nomeVariavel + " = " + fabricaIcones.getSimpleName() + "." + pcomp.toString() + ".getIcone(); ";
        }

        MethodSource<JavaClassSource> constructor = getCodigoJava().addMethod();

        constructor.setBody(corpoCosntructor);
        constructor.setConstructor(true);
        for (ComoFabricaIcone pIcone : fabricaIcones.getEnumConstants()) {
            adicionaIcone(pIcone, getCodigoJava());

        }

        return getCodigoJava();
    }

}
