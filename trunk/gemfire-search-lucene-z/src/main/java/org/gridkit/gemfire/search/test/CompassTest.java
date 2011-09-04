package org.gridkit.gemfire.search.test;

import org.compass.core.*;
import org.compass.core.config.CompassConfiguration;
import java.util.Date;

import org.compass.core.config.RuntimeCompassSettings;
import org.compass.core.engine.SearchEngine;
import org.compass.core.marshall.MarshallingStrategy;
import org.compass.core.spi.InternalCompass;
import org.gridkit.gemfire.search.compass.marshall.GridkitMarshallingStrategy;
import org.gridkit.gemfire.search.example.model.Author;
import org.gridkit.gemfire.search.example.model.Book;

public class CompassTest {
    static Author author = new Author();
    static Book book = new Book();

    static {
        author.setId(0);
        author.setName("The quick brown fox jumped over the lazy dogs");
        author.setBirthday(new Date());

        book.setId(0);
        book.setAuthor(author);
    }

    public static void main(String[] args) throws InterruptedException {
        CompassConfiguration configuration = new CompassConfiguration().configure("/compass.cfg.xml");

        InternalCompass compass = (InternalCompass)configuration.buildCompass();

        RuntimeCompassSettings runtimeSettings = new RuntimeCompassSettings(compass.getSettings());
        SearchEngine searchEngine = compass.getSearchEngineFactory().openSearchEngine(runtimeSettings);

        MarshallingStrategy marshallingStrategy = new GridkitMarshallingStrategy(
            compass.getMapping(), searchEngine, compass.getConverterLookup(),
            compass.getResourceFactory(), compass.getPropertyNamingStrategy()
        );

        printProperties(marshallingStrategy.marshall(author));

        compass.close();
    }

    public static void printProperties(Resource resource) {
        for (Property property : resource.getProperties()) {
            System.out.println(property);
        }
    }
}
