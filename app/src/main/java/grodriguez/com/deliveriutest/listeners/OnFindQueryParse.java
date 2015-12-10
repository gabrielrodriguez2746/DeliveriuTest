package grodriguez.com.deliveriutest.listeners;

import com.parse.ParseObject;

import java.util.List;

/**
 * @author Gabriel Rodriguez
 * @version 1.0
 */
public interface OnFindQueryParse {

    /**
     * Allows to notify a simple query has ended
     *
     * @param objects Return object
     * @param e       Parse Exception
     */
    void OnParseSimpleFindResult(List<ParseObject> objects, Exception e);

    /**
     * Allows to notify a inner query has ended
     *
     * @param objects Return object
     * @param e       Parse Exception
     */
    void OnParseInnerFindResult(List<ParseObject> objects, Exception e);
}
