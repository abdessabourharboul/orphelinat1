package controller.util;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.faces.context.FacesContext;

public class CreateFileUtil {

    public static void main(String[] args) throws Exception {
        System.out.println(ServerConfigUtil.getPhotoOrphelinPath(false));
        createFilesIfNotExists();
    }

    public static void createFilesIfNotExists() throws Exception {
        createBackUp(ServerConfigUtil.getPhotoOrphelinPath(false));
    }

    public static void createBackUp(String path) throws Exception {
        //vérifier si le dossier backup existe
        System.out.println("hahwaa*********");
        File file = new File(path);
        if (file.exists()) { // C:/...
            System.out.println("le dossier origine existe");
        } else {
            file.mkdirs();
            System.out.println("le dossier n'existe pas");
//            createFolder(path);
            System.out.println("le dossier " + path + " est crée");
        }
    }

    public static String getPath(String relativePath) {
        String absolutePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(relativePath);
        return absolutePath;
    }

    public static void concat2Pdf(String cover, String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfReader reader2 = new PdfReader(cover);
// Create a stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
// Create an imported page to be inserted
        PdfImportedPage page = stamper.getImportedPage(reader2, 1);
        stamper.insertPage(1, reader2.getPageSize(1));
        stamper.getUnderContent(1).addTemplate(page, 0, 0);
// Close the stamper and the readers
        stamper.close();
        reader.close();
        reader2.close();
    }

    public static String getPathByOsName() {
        String path = "";
        String os = System.getProperty("os.name");
        String home = System.getProperty("user.home");
        String langue = System.getProperty("user.language");
        if (os.startsWith("Wind")) {
            if (langue.equals("en")) {
                path = home + "\\Downloads";
            } else if (langue.equals("fr")) {
                path = home + "\\Téléchargement";
            }
        } else if (os.startsWith("linux")) {
//            path = getPath("root");
        }
        return path;
    }

}
