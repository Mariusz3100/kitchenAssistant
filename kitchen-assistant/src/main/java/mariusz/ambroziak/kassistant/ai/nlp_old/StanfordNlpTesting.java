package mariusz.ambroziak.kassistant.ai.nlp_old;

import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;


public class StanfordNlpTesting {

//  public static String text = "Joe Smith was born in California. " +
//      "In 2017, he went to Paris, France in the summer. " +
//      "His flight left at 3:00pm on July 10th, 2017. " +
//      "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
//      "He sent a postcard to his sister Jane Smith. " +
//      "After hearing about Joe's trip, Jane decided she might go to France one day.";

  
	public static String text = "pinch baking soda";
  
	public static void main(String[] args) {
		// set up pipeline properties
		Properties props = new Properties();
		// set the list of annotators to run
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref");
		// set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
		props.setProperty("coref.algorithm", "neural");
		// build pipeline
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		// create a document object
		CoreDocument document = new CoreDocument(text);
		// annnotate the document
		pipeline.annotate(document);
		// examples
		CoreSentence sentence = document.sentences().get(0);

		// 10th token of the document
		System.out.println("Tokens:");
		for(CoreLabel token:document.tokens()) {
			System.out.println("token:"+token+" - results: \n originalText:"+token.originalText()+"\n category: "+token.category()+"\n ner: "+token.ner()+"\n value: "+token.value()+"\n lemma: "+token.lemma()+"\n tag: "+token.tag()+"\n nerConfidence: "+token.nerConfidence());

		}

		System.out.println();

		// text of the first sentence
		CoreSentence sentenceText = sentence;
		System.out.println("Example: sentence");
		System.out.println(sentenceText);
		System.out.println();

		// second sentence

		// list of the part-of-speech tags for the second sentence
		List<String> posTags = sentence.posTags();
		System.out.println("Example: pos tags");
		System.out.println(posTags);
		System.out.println();

		// list of the ner tags for the second sentence
		List<String> nerTags = sentence.nerTags();
		System.out.println("Example: ner tags");
		System.out.println(nerTags);
		System.out.println();

		// constituency parse for the second sentence
		Tree constituencyParse = sentence.constituencyParse();
		System.out.println("Example: constituency parse");
		System.out.println(constituencyParse);
		System.out.println();
		JSONObject toString=parseRecursivelyAsJson(constituencyParse);

		System.out.println("Example: constituency parsed to Json:");
		System.out.println(toString);
		System.out.println("Done \n\n");

		System.out.println("Example: constituency intended:");
		constituencyParse.indentedListPrint();

		//		 	indentedXMLPrint();
		System.out.println("Done \n\n");


		// dependency parse for the second sentence
		SemanticGraph dependencyParse = sentence.dependencyParse();
		System.out.println("Example: dependency parse");
		System.out.println(dependencyParse);
		System.out.println();

		// kbp relations found in fifth sentence
		List<RelationTriple> relations =
				sentence.relations();
		System.out.println("Example: relation");
		System.out.println(relations==null||relations.size()==0?"brak":relations.get(0));
		System.out.println();

		// entity mentions in the second sentence
		List<CoreEntityMention> entityMentions = sentence.entityMentions();
		System.out.println("Example: entity mentions");
		System.out.println(entityMentions);
		System.out.println();

		// coreference between entity mentions
		if(sentence.entityMentions()!=null&& sentence.entityMentions().size()>0) {
			CoreEntityMention originalEntityMention = sentence.entityMentions().get(0);
			System.out.println("Example: original entity mention");
			System.out.println(originalEntityMention);
			System.out.println("Example: canonical entity mention");
			System.out.println(originalEntityMention.canonicalEntityMention().get());
			System.out.println();
		}else {
			System.out.println("\n\nNo entity mentions\n\n");

		}
		// get document wide coref info
		Map<Integer, CorefChain> corefChains = document.corefChains();
		System.out.println("Example: coref chains for document");
		System.out.println(corefChains);
		System.out.println();

		// get quotes in document
//		List<CoreQuote> quotes = document.quotes();
//		if(quotes.size()==0) {
//			System.out.println("No quotes");
//
//		}else {
//			CoreQuote quote = quotes.get(0);
//			System.out.println("Example: quote");
//			System.out.println(quote);
//			System.out.println();
//
//			// original speaker of quote
//			// note that quote.speaker() returns an Optional
//			System.out.println("Example: original speaker of quote");
//			System.out.println(quote.speaker().get());
//			System.out.println();
//
//			// canonical speaker of quote
//			System.out.println("Example: canonical speaker of quote");
//			System.out.println(quote.canonicalSpeaker().get());
//			System.out.println();
//		}
	}
	private static JSONObject parseRecursivelyAsJson(Tree constituencyParse) {
		JSONObject retValue=new JSONObject();
		retValue.put("penn", constituencyParse.pennString());
		retValue.put("node", constituencyParse.nodeString());
		retValue.put("number", constituencyParse.nodeNumber(constituencyParse));
		retValue.put("value", constituencyParse.value());
		retValue.put("label", constituencyParse.label());

		JSONArray children=new JSONArray();

		for(Tree child:constituencyParse.getChildrenAsList()) {
			JSONObject jsonChild=parseRecursivelyAsJson(child);
			children.put(jsonChild);
		}

		retValue.put("children", children);

		return retValue;


	}
  
  
  
  
  
  
  
  
  
  
  
  
//  public static void main(String[] args) {
//    // set up pipeline properties
//    Properties props = new Properties();
//    // set the list of annotators to run
//    props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
//    // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
//    props.setProperty("coref.algorithm", "neural");
//    // build pipeline
//    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//    // create a document object
//    CoreDocument document = new CoreDocument(text);
//    // annnotate the document
//    pipeline.annotate(document);
//    // examples
//
//    // 10th token of the document
//    CoreLabel token = document.tokens().get(10);
//    System.out.println("Example: token");
//    System.out.println(token);
//    System.out.println();
//
//    // text of the first sentence
//    String sentenceText = document.sentences().get(0).text();
//    System.out.println("Example: sentence");
//    System.out.println(sentenceText);
//    System.out.println();
//
//    // second sentence
//    CoreSentence sentence = document.sentences().get(1);
//
//    // list of the part-of-speech tags for the second sentence
//    List<String> posTags = sentence.posTags();
//    System.out.println("Example: pos tags");
//    System.out.println(posTags);
//    System.out.println();
//
//    // list of the ner tags for the second sentence
//    List<String> nerTags = sentence.nerTags();
//    System.out.println("Example: ner tags");
//    System.out.println(nerTags);
//    System.out.println();
//
//    // constituency parse for the second sentence
//    Tree constituencyParse = sentence.constituencyParse();
//    System.out.println("Example: constituency parse");
//    System.out.println(constituencyParse);
//    System.out.println();
//
//    // dependency parse for the second sentence
//    SemanticGraph dependencyParse = sentence.dependencyParse();
//    System.out.println("Example: dependency parse");
//    System.out.println(dependencyParse);
//    System.out.println();
//
//    // kbp relations found in fifth sentence
//    List<RelationTriple> relations =
//        document.sentences().get(4).relations();
//    System.out.println("Example: relation");
//    System.out.println(relations.get(0));
//    System.out.println();
//
//    // entity mentions in the second sentence
//    List<CoreEntityMention> entityMentions = sentence.entityMentions();
//    System.out.println("Example: entity mentions");
//    System.out.println(entityMentions);
//    System.out.println();
//
//    // coreference between entity mentions
//    CoreEntityMention originalEntityMention = document.sentences().get(3).entityMentions().get(1);
//    System.out.println("Example: original entity mention");
//    System.out.println(originalEntityMention);
//    System.out.println("Example: canonical entity mention");
//    System.out.println(originalEntityMention.canonicalEntityMention().get());
//    System.out.println();
//
//    // get document wide coref info
//    Map<Integer, CorefChain> corefChains = document.corefChains();
//    System.out.println("Example: coref chains for document");
//    System.out.println(corefChains);
//    System.out.println();
//
//    // get quotes in document
//    List<CoreQuote> quotes = document.quotes();
//    CoreQuote quote = quotes.get(0);
//    System.out.println("Example: quote");
//    System.out.println(quote);
//    System.out.println();
//
//    // original speaker of quote
//    // note that quote.speaker() returns an Optional
//    System.out.println("Example: original speaker of quote");
//    System.out.println(quote.speaker().get());
//    System.out.println();
//
//    // canonical speaker of quote
//    System.out.println("Example: canonical speaker of quote");
//    System.out.println(quote.canonicalSpeaker().get());
//    System.out.println();
//
//  }

}

