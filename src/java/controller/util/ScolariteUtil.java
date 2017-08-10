package controller.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abdessabour
 */
public class ScolariteUtil {

    private final String premiereAnnee;
    private final String deuxiemeAnnee;
    private final String troisiemeAnnee;
    private final String quatriemeAnnee;
    private final String cinquiemeAnnee;
    private final String sixiemeAnnee;
    private final String premiereAnneeCollege;
    private final String deuxiemeAnneeCollege;
    private final String troisiemeAnneeCollege;
    private final String troncCommun;
    private final String premiereAnneeBac;
    private final String deuxiemeAnneeBac;
    private final String universite;

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
        ScolariteUtil scolariteStrings = getNiveauxScolarite();
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
        ScolariteUtil scolariteStrings = getNiveauxScolarite();
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
        ScolariteUtil scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.premiereAnneeCollege);
        list.add(scolariteStrings.deuxiemeAnneeCollege);
        list.add(scolariteStrings.troisiemeAnneeCollege);
        return list;
    }

    private static List<String> niveauxScolariteLycee() {
        ScolariteUtil scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.troncCommun);
        list.add(scolariteStrings.premiereAnneeBac);
        list.add(scolariteStrings.deuxiemeAnneeBac);
        return list;
    }

    private static List<String> niveauxScolariteUniversitaire() {
        ScolariteUtil scolariteStrings = getNiveauxScolarite();
        List<String> list = new ArrayList<>();
        list.add(scolariteStrings.universite);
        return list;
    }

    private static ScolariteUtil getNiveauxScolarite() {
        return new ScolariteUtil();
    }

    private ScolariteUtil() {
        premiereAnnee = "الاول ابتدائى";
        deuxiemeAnnee = "الثاني ابتدائى";
        troisiemeAnnee = "الثالث ابتدائى";
        quatriemeAnnee = "الرابع ابتدائى";
        cinquiemeAnnee = "الخامس ابتدائى";
        sixiemeAnnee = "السادس ابتدائى";
        premiereAnneeCollege = "الاولى اعدادي";
        deuxiemeAnneeCollege = "الثانية اعدادي";
        troisiemeAnneeCollege = "الثالثة اعدادي";
        troncCommun = "الجدع المشترك";
        premiereAnneeBac = "الاولى باكالوريا";
        deuxiemeAnneeBac = "الثانية باكالوريا";
        universite = "الجامعي";
    }

    public static String succesNiveauScolaire(String niveauActuel) {
        return succesNiveauScolairePrivate(niveauActuel);
    }

    private static String succesNiveauScolairePrivate(String niveauActuel) {
        ScolariteUtil scolariteUtil = new ScolariteUtil();
        String niveauSuivant = "";
        if (niveauActuel.equals(scolariteUtil.premiereAnnee)) {
            niveauSuivant = scolariteUtil.deuxiemeAnnee;
        } else if (niveauActuel.equals(scolariteUtil.deuxiemeAnnee)) {
            niveauSuivant = scolariteUtil.troisiemeAnnee;
        } else if (niveauActuel.equals(scolariteUtil.troisiemeAnnee)) {
            niveauSuivant = scolariteUtil.quatriemeAnnee;
        } else if (niveauActuel.equals(scolariteUtil.quatriemeAnnee)) {
            niveauSuivant = scolariteUtil.cinquiemeAnnee;
        } else if (niveauActuel.equals(scolariteUtil.cinquiemeAnnee)) {
            niveauSuivant = scolariteUtil.sixiemeAnnee;
        } else if (niveauActuel.equals(scolariteUtil.sixiemeAnnee)) {
            niveauSuivant = scolariteUtil.premiereAnneeCollege;
        } else if (niveauActuel.equals(scolariteUtil.premiereAnneeCollege)) {
            niveauSuivant = scolariteUtil.deuxiemeAnneeCollege;
        } else if (niveauActuel.equals(scolariteUtil.deuxiemeAnneeCollege)) {
            niveauSuivant = scolariteUtil.troisiemeAnneeCollege;
        } else if (niveauActuel.equals(scolariteUtil.troisiemeAnneeCollege)) {
            niveauSuivant = scolariteUtil.troncCommun;
        } else if (niveauActuel.equals(scolariteUtil.troncCommun)) {
            niveauSuivant = scolariteUtil.premiereAnneeBac;
        } else if (niveauActuel.equals(scolariteUtil.premiereAnneeBac)) {
            niveauSuivant = scolariteUtil.deuxiemeAnneeBac;
        } else if (niveauActuel.equals(scolariteUtil.deuxiemeAnneeBac)) {
            niveauSuivant = scolariteUtil.universite;
        } else if (niveauActuel.equals(scolariteUtil.universite)) {
            niveauSuivant = scolariteUtil.universite;
        }
        return niveauSuivant;
    }

    public static String succesSilkScolaire(String niveauActuel) {
        return succesSilkScolairePrivate(niveauActuel);
    }

    private static String succesSilkScolairePrivate(String niveauActuel) {
        ScolariteUtil scolariteUtil = new ScolariteUtil();
        String silkScolaire = "";
        if (niveauActuel.equals(scolariteUtil.premiereAnnee) || niveauActuel.equals(scolariteUtil.deuxiemeAnnee)
                || niveauActuel.equals(scolariteUtil.troisiemeAnnee) || niveauActuel.equals(scolariteUtil.quatriemeAnnee)
                || niveauActuel.equals(scolariteUtil.cinquiemeAnnee) || niveauActuel.equals(scolariteUtil.sixiemeAnnee)) {
            silkScolaire = getPimaire();
        } else if (niveauActuel.equals(scolariteUtil.premiereAnneeCollege) || niveauActuel.equals(scolariteUtil.deuxiemeAnneeCollege)
                || niveauActuel.equals(scolariteUtil.troisiemeAnneeCollege)) {
            silkScolaire = getCollege();
        } else if (niveauActuel.equals(scolariteUtil.troncCommun) || niveauActuel.equals(scolariteUtil.premiereAnneeBac)
                || niveauActuel.equals(scolariteUtil.deuxiemeAnneeBac)) {
            silkScolaire = getLycee();
        } else if (niveauActuel.equals(scolariteUtil.universite)) {
            silkScolaire = getUniversiteName();
        }
        return silkScolaire;
    }
    
    public static List<String> getSilkScolaires() {
        List<String> list = new ArrayList<>();
        list.add(ScolariteUtil.getPimaire());
        list.add(ScolariteUtil.getCollege());
        list.add(ScolariteUtil.getLycee());
        list.add(ScolariteUtil.getUniversiteName());
        return list;
    }

    public static String getPimaire() {
        return "ابتدائي";
    }

    public static String getCollege() {
        return "اعدادي";
    }

    public static String getLycee() {
        return "ثانوي";
    }

    public static String getUniversiteName() {
        return "جامعي";
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
