package com.sapashev;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Searches first occurrence of specified text in files. Implements Runnable interface.
 * If thread finds specified text, it sends to other threads interrupt message and finishes his activity.
 * @author Arslan Sapashev
 * @since 19.06.2016
 * @version 1.0
 */
public class SearchThread implements Runnable {
    /**
     * files - list of files that directory and subdirectories contains.
     * threads - list of threads created in thread pool
     * textToSearch - text to search in files
     */
    private final List<File> files;
    private final List<Thread> threads;
    private final String textToSearch;

    public SearchThread(List<File> files, String textToSearch, List<Thread> threads){
        this.files = files;
        this.textToSearch = textToSearch;
        this.threads = threads;
    }

    /**
     * Conducts basic activity for text search.
     */
    public void run () {
        for(File f : files){
            try {
                if(checkFileByString(f)){
                    System.out.printf("Text has been found in %s file\n",f);
                    interruptOtherThreads();
                    break;
                }
            }
            catch (FileNotFoundException ex){
                //TODO log exception here
            }
            if(Thread.currentThread().isInterrupted()){
                //TODO log thread exit
                return;
            }
        }
    }

    /**
     * Reads file string by string and invokes checkMatch method fro each string to check
     * if string from file matches to the text.
     * @param file - file from which to read data.
     * @return - false - no any match, true - file contains text.
     * @throws FileNotFoundException - in case there is no such file.
     */
    private boolean checkFileByString(File file) throws FileNotFoundException{
        boolean isTextFound = false;
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()){
                String text = scanner.nextLine();
                if(checkMatch(text)){
                    isTextFound = true;
                    break;
                }
            }
        }
        return isTextFound;
    }

    /**
     * Checks if textToSearch field is presents in text parameter.
     * @param text - text where to search textToSearch occurrence.
     * @return - false - no any match, true - text contains textToSearch.
     */
    private boolean checkMatch(String text){
        boolean isMatches = false;
        for(int i = 0; i < textToSearch.length(); i++){
            isMatches = textToSearch.regionMatches(true,0,text,i,textToSearch.length());
            if(isMatches){
                break;
            }
        }
        return isMatches;
    }

    /**
     * Sends interrupt message to all threads in thread pool, exclusive current thread.
     * Before sending interrupt message it checks if target thread is still alive.
     */
    private void interruptOtherThreads(){
        for(Thread thread : threads){
            if(thread.isAlive() && !Thread.currentThread().equals(thread)){
                thread.interrupt();
            }
        }
    }


}
