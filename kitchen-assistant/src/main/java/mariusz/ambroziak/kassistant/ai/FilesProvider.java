package mariusz.ambroziak.kassistant.ai;

import org.springframework.core.io.*;

import mariusz.ambroziak.kassistant.dao.DaoProvider;

public class FilesProvider {
	public Resource getTeachingShopComFile() {
		return teachingShopComFile;
	}

	public void setTeachingShopComFile(Resource teachingShopComFile) {
		this.teachingShopComFile = teachingShopComFile;
	}

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
	Resource categoriesTestFile;
	Resource teachingTescoFile;
	Resource teachingShopComFile;
	
	public Resource getTeachingTescoFile() {
		return teachingTescoFile;
	}

	public void setTeachingTescoFile(Resource teachingExpectationsFile) {
		this.teachingTescoFile = teachingExpectationsFile;
	}

	public Resource getCategoriesTestFile() {
		return categoriesTestFile;
	}

	public void setCategoriesTestFile(Resource categoriesTestFile) {
		this.categoriesTestFile = categoriesTestFile;
	}

	public Resource getCategoriesFile() {
		return categoriesFile;
	}

	public void setCategoriesFile(Resource categoriesFile) {
		this.categoriesFile = categoriesFile;
	}
	
	
}
