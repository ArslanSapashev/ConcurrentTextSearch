package com.sapashev;

import java.io.File;

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
            new SimpleConcurrentSearch().startProcession(args);
        }
        catch (MissedArgumnetExcetion ex){
            //TODO log exception
        }
    }

    private void startProcession(String[] args) throws MissedArgumnetExcetion{
        if (args.length < 2){
            throw new MissedArgumnetExcetion("missed some arguments");
        }
        String startPlace = args[0];
        String textToSearch = args[1];
        ThreadPool pool = new ThreadPool(10);
        try{
            pool.start(new File(startPlace),textToSearch);
        }
        catch (InterruptedException ex){
            //TODO log exception
            return;
        }

    }
}
