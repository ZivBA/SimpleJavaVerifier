/**
 * Created by rooty on 6/21/15.
 */
import java.io.File;
import java.util.Arrays;

public class multiTest{
	private static final File basicSrc = new File("//abs path to tests folder");

	public static void main(String[] args) {
		File[] testFiles = basicSrc.listFiles();
		Arrays.sort(testFiles);

		for (File testFile : testFiles){
			System.out.println();
			System.out.println(testFile.getName());
			MyFileScript.main(new String[]{basicSrc.getAbsolutePath(), filter.getAbsolutePath()});
		}
		for (File filter : advFiltersList){
			System.out.println();
			System.out.println(filter.getName());
			MyFileScript.main(new String[]{advSrc.getAbsolutePath(), filter.getAbsolutePath()});
		}
	}


}