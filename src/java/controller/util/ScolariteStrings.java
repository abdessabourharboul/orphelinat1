package controller.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdessabour
 */
public class ScolariteStrings {

    private String premiereAnnee;
    private String deuxiemeAnnee;
    private String troisiemeAnnee;
    private String quatriemeAnnee;
    private String cinquiemeAnnee;
    private String sixiemeAnnee;
    private String premiereAnneeCollege;
    private String deuxiemeAnneeCollege;
    private String troisiemeAnneeCollege;
    private String troncCommun;
    private String premiereAnneeBac;
    private String deuxiemeAnneeBac;
    private String universite;

    public static void main(String[] args) {
        List<String> loaded = niveauxScolarite();
        System.out.println("hani f lowel" + loaded.get(12));
    }

    public static List<String> findNiveauxBySilk(String silk) {
        if (null == silk) {
            return new ArrayList<>();
        } else {
            switch (silk) {
                case "ابتدائي":
                    return niveauxScolaritePrimaire();
                case "اعدادي":
                    return niveauxScolariteCollege();
                case "ثانوي":
                    return niveauxScolariteLycee();
                case "جامعي":
                    return niveauxScolariteUniversitaire();
                default:
                    break;
            }
        }
        return new ArrayList<>();
    }

    public static List<String> niveauxScolarite() {
        ScolariteStrings scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.premiereAnnee);
        list.add(scolariteStrings.deuxiemeAnnee);
        list.add(scolariteStrings.troisiemeAnnee);
        list.add(scolariteStrings.quatriemeAnnee);
        list.add(scolariteStrings.cinquiemeAnnee);
        list.add(scolariteStrings.sixiemeAnnee);
        list.add(scolariteStrings.premiereAnneeCollege);
        list.add(scolariteStrings.deuxiemeAnneeCollege);
        list.add(scolariteStrings.troisiemeAnneeCollege);
        list.add(scolariteStrings.troncCommun);
        list.add(scolariteStrings.premiereAnneeBac);
        list.add(scolariteStrings.deuxiemeAnneeBac);
        list.add(scolariteStrings.universite);
        return list;
    }

    private static List<String> niveauxScolaritePrimaire() {
        ScolariteStrings scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.premiereAnnee);
        list.add(scolariteStrings.deuxiemeAnnee);
        list.add(scolariteStrings.troisiemeAnnee);
        list.add(scolariteStrings.quatriemeAnnee);
        list.add(scolariteStrings.cinquiemeAnnee);
        list.add(scolariteStrings.sixiemeAnnee);
        return list;
    }

    private static List<String> niveauxScolariteCollege() {
        ScolariteStrings scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.premiereAnneeCollege);
        list.add(scolariteStrings.deuxiemeAnneeCollege);
        list.add(scolariteStrings.troisiemeAnneeCollege);
        return list;
    }

    private static List<String> niveauxScolariteLycee() {
        ScolariteStrings scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.troncCommun);
        list.add(scolariteStrings.premiereAnneeBac);
        list.add(scolariteStrings.deuxiemeAnneeBac);
        return list;
    }

    private static List<String> niveauxScolariteUniversitaire() {
        ScolariteStrings scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.universite);
        return list;
    }

    private static ScolariteStrings getNiveauxScolarite() {
        return new ScolariteStrings();
    }

    private ScolariteStrings() {
        premiereAnnee = "أولى ابتدائى";
        deuxiemeAnnee = "ثانية ابتدائى";
        troisiemeAnnee = "ثالثة ابتدائى";
        quatriemeAnnee = "رابعة ابتدائى";
        cinquiemeAnnee = "خامسة ابتدائى";
        sixiemeAnnee = "سادسة ابتدائى";
        premiereAnneeCollege = "أولى اعدادي";
        deuxiemeAnneeCollege = "ثانية اعدادي";
        troisiemeAnneeCollege = "ثالثة اعدادي";
        troncCommun = "الجدع المشترك";
        premiereAnneeBac = "اولى باكالوريا";
        deuxiemeAnneeBac = "ثانية باكالوريا";
        universite = "الجامعي";
    }

    public String getPremiereAnnee() {
        return premiereAnnee;
    }

    public String getDeuxiemeAnnee() {
        return deuxiemeAnnee;
    }

    public String getTroisiemeAnnee() {
        return troisiemeAnnee;
    }

    public String getQuatriemeAnnee() {
        return quatriemeAnnee;
    }

    public String getCinquiemeAnnee() {
        return cinquiemeAnnee;
    }

    public String getSixiemeAnnee() {
        return sixiemeAnnee;
    }

    public String getPremiereAnneeCollege() {
        return premiereAnneeCollege;
    }

    public String getDeuxiemeAnneeCollege() {
        return deuxiemeAnneeCollege;
    }

    public String getTroisiemeAnneeCollege() {
        return troisiemeAnneeCollege;
    }

    public String getTroncCommun() {
        return troncCommun;
    }

    public String getPremiereAnneeBac() {
        return premiereAnneeBac;
    }

    public String getDeuxiemeAnneeBac() {
        return deuxiemeAnneeBac;
    }

    public String getUniversite() {
        return universite;
    }

}
