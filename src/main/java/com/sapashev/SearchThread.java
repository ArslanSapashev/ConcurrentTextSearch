package com.sapashev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOG = LoggerFactory.getLogger(SearchThread.class);


    public SearchThread(List<File> files, String textToSearch, List<Thread> threads){
        this.files = files;
        this.textToSearch = textToSearch;
        this.threads = threads;
    }

    /**
     * Conducts basic activity for text search.
     * Invokes checkFileByString method for each file in file list.
     * If text found in some file, invokes interruptOtherThreads method to stop other threads.
     */
    public void run () {
        for(File f : files){
            try {
                if(checkFileByString(f)){
                    LOG.info(String.format("Text has been found in %s file by %s.", f, Thread.currentThread()));
                    interruptOtherThreads();
                    break;
                }
            }
            catch (FileNotFoundException ex){
                LOG.error(String.format("Exception %s occurred.", ex));
            }
            if(Thread.currentThread().isInterrupted()){
                LOG.info(String.format("Thread %s interrupted.", Thread.currentThread()));
                break;
            }
        }
    }

    /**
     * Reads file string by string and invokes checkMatch method for each string to check
     * if string from file matches to the text.
     * @param file - file from which to read data.
     * @return - false - no any match, true - file contains text.
     * @throws FileNotFoundException - in case there is no such file.
     */
    private boolean checkFileByString(File file) throws FileNotFoundException{
        boolean isTextFound = false;
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String text = scanner.nextLine();
                    if (text.toUpperCase().contains(textToSearch.toUpperCase())) {
                        isTextFound = true;
                        break;
                    }
                }
            }
        return isTextFound;
    }

    /**
     * Sends interrupt message to all threads in thread pool, exclusive current thread.
     * Before sending interrupt message it checks if target thread is still alive.
     */
    private void interruptOtherThreads() {
        for(Thread thread : threads){
            if(!Thread.currentThread().equals(thread)){
                thread.interrupt();
            }
        }
    }
}
