package com.sapashev;

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
    public static void main (String[] args) {
        try {
            new SimpleConcurrentSearch().start(args, 20);
        }
        catch (MissedArgumentException ex){
            //TODO log exception
        }
        catch (FileNotFoundException ex){
            //TODO log exception
            System.out.println("wrong path");
        }
    }

    /**
     * Checks arguments and creates thread pool.
     * @param args - first argument it's place from which start to search, the second one is text to search.
     * @param numThreads - desirable number of threads to conduct search.
     * @throws MissedArgumentException - if one/both arguments absent.
     * @throws FileNotFoundException - if first argument points to wrong path
     */
    private void start (String[] args, int numThreads) throws MissedArgumentException, FileNotFoundException {
        if (args.length < 2){
            throw new MissedArgumentException("missed some arguments");
        }
        if(!(new File(args[0]).exists())){
            throw new FileNotFoundException("no such file/directory");
        }
        String startPlace = args[0];
        String textToSearch = args[1];
        ThreadPool pool = new ThreadPool(numThreads);
        try{
            pool.start(new File(startPlace),textToSearch);
        }
        catch (InterruptedException ex){
            //TODO log exception
            return;
        }
    }
}
