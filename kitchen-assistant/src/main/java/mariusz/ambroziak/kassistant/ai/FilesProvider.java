package mariusz.ambroziak.kassistant.ai;

import org.springframework.core.io.*;

import mariusz.ambroziak.kassistant.dao.DaoProvider;

public class FilesProvider {
	private static FilesProvider singleton;
	
	public static FilesProvider getInstance(){
		return singleton;
	}
	
	public FilesProvider() {
		super();

		
		
		if(singleton==null)
			singleton=this;
		else
			throw new IllegalStateException("two instances of FilesProvider");

	}

	Resource categoriesFile;

	public Resource getCategoriesFile() {
		return categoriesFile;
	}

	public void setCategoriesFile(Resource categoriesFile) {
		this.categoriesFile = categoriesFile;
	}
	
	
}
