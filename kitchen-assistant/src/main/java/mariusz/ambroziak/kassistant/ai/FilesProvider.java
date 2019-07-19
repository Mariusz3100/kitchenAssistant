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
	Resource teachingEdamanFile;
	Resource edamanCategoriesFile;
	Resource posModelFile;
	Resource sentModelFile;
	Resource tokenModelFile;
	Resource lemmaModelFile;
	Resource maxentModelFile;
	Resource chunkerModelFile;
	Resource parserChunkerModelFile;

	
	public Resource getParserChunkerModelFile() {
		return parserChunkerModelFile;
	}

	public void setParserChunkerModelFile(Resource parserChunkerModelFile) {
		this.parserChunkerModelFile = parserChunkerModelFile;
	}

	public Resource getMaxentModelFile() {
		return maxentModelFile;
	}

	public void setMaxentModelFile(Resource maxentModelFile) {
		this.maxentModelFile = maxentModelFile;
	}

	public Resource getChunkerModelFile() {
		return chunkerModelFile;
	}

	public void setChunkerModelFile(Resource chunkerModelFile) {
		this.chunkerModelFile = chunkerModelFile;
	}

	public Resource getLemmaModelFile() {
		return lemmaModelFile;
	}

	public void setLemmaModelFile(Resource lemmaModelFile) {
		this.lemmaModelFile = lemmaModelFile;
	}

	Resource wordsInputFile;

	
	
	
	public Resource getWordsInputFile() {
		return wordsInputFile;
	}

	public void setWordsInputFile(Resource wordsInputFile) {
		this.wordsInputFile = wordsInputFile;
	}

	public Resource getTokenModelFile() {
		return tokenModelFile;
	}

	public void setTokenModelFile(Resource tokenModelFile) {
		this.tokenModelFile = tokenModelFile;
	}

	public Resource getPosModelFile() {
		return posModelFile;
	}

	public void setPosModelFile(Resource posModelFile) {
		this.posModelFile = posModelFile;
	}

	public Resource getSentModelFile() {
		return sentModelFile;
	}

	public void setSentModelFile(Resource sentModelFile) {
		this.sentModelFile = sentModelFile;
	}

	public Resource getEdamanCategoriesFile() {
		return edamanCategoriesFile;
	}

	public void setEdamanCategoriesFile(Resource edamanCategoriesFile) {
		this.edamanCategoriesFile = edamanCategoriesFile;
	}

	public Resource getTeachingEdamanFile() {
		return teachingEdamanFile;
	}

	public void setTeachingEdamanFile(Resource teachingEdamanFile) {
		this.teachingEdamanFile = teachingEdamanFile;
	}

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
