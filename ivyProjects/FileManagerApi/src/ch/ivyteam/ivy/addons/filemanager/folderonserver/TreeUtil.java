package ch.ivyteam.ivy.addons.filemanager.folderonserver;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.addons.filemanager.util.MethodArgumentsChecker;
import ch.ivyteam.ivy.scripting.objects.Tree;

public class TreeUtil {
	
	public static boolean isFolderOnServerInTree(FolderOnServer fos, Tree tree) {
		MethodArgumentsChecker.throwIllegalArgumentExceptionIfOneOfTheArgumentIsNull("The FolderOnServer or tree parameters cannot be null.", fos, tree);
		if(fos.getId() <= 0) {
			throw new IllegalArgumentException("The given FolderOnServer has an invalid id (zero or less). This method supports only folders stored in a database.");
		}
		
		FolderOnServer root = (FolderOnServer) tree.getValue();
		boolean found = root.getId() == fos.getId();
		
		if(!found) {
			java.util.List<Tree> trees = tree.getAllDeepChildren();
			for(Tree t: trees) {
				FolderOnServer f = (FolderOnServer) t.getValue();
				found = f.getId() == fos.getId();
				if(found) {
					break;
				}
			}
		}
		return found;
	}

}
