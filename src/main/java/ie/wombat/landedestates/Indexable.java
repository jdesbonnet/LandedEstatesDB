package ie.wombat.landedestates;

/**
 * Classes implementing this interface are intended to be indexed by the
 * Lucene free text search engine. In order to facilitate this any indexed
 * entity must return: a unique document id, a short one line name, and and 
 * optional longer description.
 * 
 * @author Joe Desbonnet
 */
public interface Indexable {

    /**
     * Returns a unique document ID. Must be unique across the application.
     * @return unique document ID 
     */
    public String getLuceneId ();
    
    /**
     * One short line name of the document
     * @return One line document name
     */
    public String getName();
    
    /**
     * Longer document description / summary.
     * 
     * @return Document description 
     */
    public String getDescription();
	
}
