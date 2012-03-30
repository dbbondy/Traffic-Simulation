
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Dan
 */
public class TestMain {

    public static void main(String[] args) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnOption = fileChooser.showOpenDialog(null);
        if (returnOption == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            int i = filePath.lastIndexOf("\\");
            String testDir = filePath.substring(0, i);

            System.out.println(filePath);
            System.out.println(testDir);
        }
    }
}
