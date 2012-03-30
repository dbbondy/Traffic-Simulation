
import java.io.File;
import java.io.FilenameFilter;
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
            int x = filePath.lastIndexOf("\\");
            String fileDir = filePath.substring(0, x);
            File dir = new File(fileDir);
            String[] children = dir.list();
            if (children == null) {
                // Either dir does not exist or is not a directory
            } else {
                for (int i = 0; i < children.length; i++) {
                    // Get filename of file or directory
                    String filename = children[i];
                }
            }

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".st");
                }
            };
            children = dir.list(filter);

            for (int i = 0; i < children.length; i++) {
                System.out.println(children[i]);
            }
        }
    }
}
