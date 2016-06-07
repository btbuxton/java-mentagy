package net.blabux.mentagy.loader;

import java.io.IOException;
import java.io.Reader;

import net.blabux.mentagy.domain.BoardDefinition;

/**
 * Created by btbuxton on 5/30/16.
 */
public interface BoardDefinitionLoader {
    public BoardDefinition load(Reader reader) throws IOException;
}
