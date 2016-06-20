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
            new SimpleConcurrentSearch().startProcession(args);
        }
        catch (MissedArgumentException ex){
            //TODO log exception
        }
        catch (FileNotFoundException ex){
            //TODO log exception
            System.out.println("wrong path");
        }
    }

    private void startProcession(String[] args) throws MissedArgumentException, FileNotFoundException {
        if (args.length < 2){
            throw new MissedArgumentException("missed some arguments");
        }
        String startPlace = args[0];
        String textToSearch = args[1];
        if(!(new File(startPlace).exists())){
            throw new FileNotFoundException("no such file");
        }
        ThreadPool pool = new ThreadPool(20);
        try{
            pool.start(new File(startPlace),textToSearch);
        }
        catch (InterruptedException ex){
            //TODO log exception
            return;
        }

    }
}
