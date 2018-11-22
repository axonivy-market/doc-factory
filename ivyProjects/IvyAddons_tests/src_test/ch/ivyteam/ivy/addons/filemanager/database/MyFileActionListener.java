package ch.ivyteam.ivy.addons.filemanager.database;

import ch.ivyteam.ivy.addons.filemanager.listener.AbstractFileActionListener;
import ch.ivyteam.ivy.addons.filemanager.listener.FileActionEvent;

public class MyFileActionListener extends AbstractFileActionListener {
	
	@Override
	public void fileChanged(FileActionEvent e) {
		System.out.println("Document has changed :"+e.getDocument());
	}

}
