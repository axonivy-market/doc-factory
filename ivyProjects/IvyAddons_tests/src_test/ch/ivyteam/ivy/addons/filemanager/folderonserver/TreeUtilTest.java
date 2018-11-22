package ch.ivyteam.ivy.addons.filemanager.folderonserver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.ivyteam.ivy.addons.filemanager.FolderOnServer;
import ch.ivyteam.ivy.scripting.objects.List;
import ch.ivyteam.ivy.scripting.objects.Tree;

public class TreeUtilTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	Tree folderTree;

	@Before
	public void setup() {
		this.folderTree = makeTree();
	}

	@Test
	public void isFolderOnServerInTree_throws_IAE_if_FolderIsNull() {
		thrown.expect(IllegalArgumentException.class);
		TreeUtil.isFolderOnServerInTree(null, folderTree);
	}

	@Test
	public void isFolderOnServerInTree_throws_IAE_if_TreeIsNull() {
		thrown.expect(IllegalArgumentException.class);
		TreeUtil.isFolderOnServerInTree(makeFolderWithId(55), null);
	}
	
	@Test
	public void isFolderOnServerInTree_throws_IAE_if_FolderAsNoValidId() {
		thrown.expect(IllegalArgumentException.class);
		TreeUtil.isFolderOnServerInTree(makeFolderWithId(0), folderTree);
	}
	
	@Test
	public void isFolderOnServerInTree_returnsTrue_if_folder_in_tree() {
		FolderOnServer fos = makeFolderWithId(4);
		
		assertTrue(makeListFolders().contains(fos));
		assertTrue(TreeUtil.isFolderOnServerInTree(fos, folderTree));
	}
	
	@Test
	public void isFolderOnServerInTree_returnsFalse_if_folder_not_in_tree() {
		FolderOnServer fos = makeFolderWithId(12);
		
		assertFalse(makeListFolders().contains(fos));
		assertFalse(TreeUtil.isFolderOnServerInTree(fos, folderTree));
	}
	
	@Test
	public void isFolderOnServerInTree_returnsTrue_if_folder_isRoot() {
		FolderOnServer fos = makeFolderWithId(1);
		
		assertTrue(((FolderOnServer) this.folderTree.getValue()).getId() == fos.getId());
		assertTrue(TreeUtil.isFolderOnServerInTree(fos, folderTree));
	}

	private Tree makeTree() {
		Tree t = new Tree();
		FolderOnServer fos = new FolderOnServer();
		fos.setId(1);
		t.setInfo("aDir");
		t.setValue(fos);
		t.createChildren(makeListFolders());
		return t;
	}

	private List<FolderOnServer> makeListFolders() {
		List<FolderOnServer> foss = List.create(FolderOnServer.class);
		while(foss.size() < 10) {
			foss.add(makeFolderWithId(foss.size() + 2));
		}
		return foss;
	}

	private FolderOnServer makeFolderWithId(int id) {
		FolderOnServer fos = new FolderOnServer();
		fos.setId(id); 
		return fos;
	}

}
