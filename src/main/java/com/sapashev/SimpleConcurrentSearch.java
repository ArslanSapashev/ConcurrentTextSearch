package com.sapashev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Searches specified text in files. Search could be conducted concurrently.
 * Number of threads is subject to user's choice.
 * Text to search is command-line first argument, the second one is directory/file where to search.
 * @author Arslan Sapashev
 * @since 19.06.2016
 * @version 1.0
 */
public class SimpleConcurrentSearch {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleConcurrentSearch.class);

    public static void main (String[] args) {
        try {
            new SimpleConcurrentSearch().start(args);
        }
        catch (MissedArgumentException ex){
            LOG.error(String.format("%s. User passed %d parameters.", ex, args.length));
        }
        catch (FileNotFoundException ex){
            LOG.error(String.format("Wrong path %s %s",args[0],ex));
        }
        catch (IllegalArgumentException ex){
            LOG.error(String.format("%s user defined %s threads", ex, args[2]));
        }
    }

    /**
     * Checks arguments and creates thread pool.
     * @param args - first argument it's place from which start to search, the second one is text to search.
     * @throws MissedArgumentException - if one/both arguments absent.
     * @throws FileNotFoundException - if first argument points to wrong path
     */
    private void start (String[] args) throws MissedArgumentException, FileNotFoundException {
        if (args.length < 3){
            throw new MissedArgumentException("missed some arguments");
        }
        if(!(new File(args[0]).exists())){
            throw new FileNotFoundException("no such file/directory");
        }
        if(Integer.parseInt(args[2]) < 1){
            throw new IllegalArgumentException("Illegal number of threads");
        }
        String startPlace = args[0];
        String textToSearch = args[1];
        int numThread = Integer.parseInt(args[2]);
        ThreadPool pool = new ThreadPool(numThread);
        try{
            pool.start(new File(startPlace),textToSearch);
        }
        catch (InterruptedException ex){
            LOG.error(String.format("InterruptedException %s", ex));
        }
    }
}
