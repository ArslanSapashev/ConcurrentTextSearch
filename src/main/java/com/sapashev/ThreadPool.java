package com.sapashev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates and manipulates threads for text search in files.
 * @author Arslan Sapashev
 * @since 19.06.2016
 * @version 1.0
 */
public class ThreadPool {
    private final List<File> files = new ArrayList<File>();
    private final List<Thread> threads = new ArrayList<Thread>();
    private int numThreads;
    private static final Logger LOG = LoggerFactory.getLogger(ThreadPool.class);


    /**
     * Creates ThreadPool object.
     * @param numTheads - preferable number of threads to search text concurrently.
     * NOTICE, if number of files for text search, is less than numThreads,
     * then numThreads will be set equal to number of files.
     */
    public ThreadPool(int numTheads){
        this.numThreads = numTheads;
    }

    /**
     * Invokes createThreads method and after that starts all the created threads,
     * and joins main thread to each of them.
     * @param rootOfSearch - directory to start search of text.
     * @param textToSearch - text to search in files.
     * @throws InterruptedException
     */
    public void start(File rootOfSearch, String textToSearch) throws InterruptedException{
        createThreads(rootOfSearch, textToSearch);
        for(Thread thread : threads){
            thread.start();
            thread.join();
        }
        LOG.info("All Threads started");
    }

    /**
     * Creates threads and adds them to threads list.
     * Every thread gets equal number of files to process, except last one.
     * Number of files to process by each thread calculated as number of files divided by number of threads.
     * @param rootOfSearch - directory to start search of text.
     * @param textToSearch - text to search in files.
     */
    private void createThreads(File rootOfSearch, String textToSearch){
        //Creates list of all files in directory and subdirectories
        createFileList(rootOfSearch);
        //Calculates number of threads
        numThreads = files.size() < numThreads ? files.size() : numThreads;
        //Divides file list to the sublist for each thread
        int filesToProcess = files.size()/numThreads;
        int fromIndex = 0;
        int toIndex = fromIndex + filesToProcess;
        //Creates new threads and adds them to the tread list
        for(int i = 0; i < numThreads; i++){
            threads.add(new Thread(new SearchThread(files.subList(fromIndex,toIndex),textToSearch, threads)));
            //increases fromIndex and toIndex to get next sublist of files
            fromIndex = toIndex;
            toIndex = fromIndex + filesToProcess;
            if(toIndex > files.size()){
                toIndex = files.size();
            }
            LOG.info(String.format("%s created.", threads.get(threads.size()-1)));
        }
    }

    /**
     * Creates list of all files that resides in that directory and all subdirectories.
     * Recursively passes through directory and subdirectories to create list of all files.
     * @param root - place from which to create list of files.
     */
    private void createFileList(File root){
        File[] list = root.listFiles();
        if(list == null){
            return;
        }
        for(File f : list){
            if(f.isFile()){
                files.add(f);
            } else {
                createFileList(f);
            }
        }
    }
}
