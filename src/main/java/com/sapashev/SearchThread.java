package com.sapashev;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 * Searches the file for text. Implements Runnable interface
 * @author Arslan Sapashev
 * @since 19.06.2016
 * @version 1.0
 */
public class SearchThread implements Runnable {
    private final List<File> files;
    private final List<Thread> threads;
    private final String textToSearch;

    public SearchThread(List<File> files, String textToSearch, List<Thread> threads){
        this.files = files;
        this.textToSearch = textToSearch;
        this.threads = threads;
    }

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

    private void interruptOtherThreads(){
        for(Thread thread : threads){
            if(thread.isAlive() && !Thread.currentThread().equals(thread)){
                thread.interrupt();
            }
        }
    }


}
