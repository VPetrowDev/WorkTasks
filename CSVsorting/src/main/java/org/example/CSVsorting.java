package org.example;

import java.io.*;
import java.util.*;

public class CSVsorting {

    public static long estimateBestSizeOfBlocks(File fileToBeSorted) {

        long sizeOfFile = fileToBeSorted.length();

        // we don't want to open up much more than 1024 temporary files
        final int MAXTEMPFILES = 1024;
        long blocksize = sizeOfFile / MAXTEMPFILES ;

        // on the other hand, we don't want to create many temporary files
        // for nothing. If blocksize is smaller than half the free memory, grow it.

        long freeMem = Runtime.getRuntime().freeMemory();

        if( blocksize < freeMem/2)
            blocksize = freeMem/2;
        else {
            if(blocksize >= freeMem)
                System.err.println("We expect to run out of memory. ");
        }
        return blocksize;
    }

    public static List<File> splitFile(File filePath, CSVComparator comparatorColumn) throws IOException {
        List<File> files = new ArrayList<File>();

        String header = null;

        long blocksize = estimateBestSizeOfBlocks(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            header = reader.readLine();

            try {
                List<String> tempList = new ArrayList<>();
                String line = "";
                try {
                    while (line != null) {
                        long currBlockSize = 0;
                        while ((currBlockSize < blocksize) && ((line = reader.readLine()) != null)) {
                            tempList.add(line);
                            currBlockSize += line.length();
                        }
                        files.add(sortAndSave(tempList, comparatorColumn));

                        tempList.clear();
                    }
                } catch (EOFException eof) {
                    if (tempList.size() > 0) {

                        files.add(sortAndSave(tempList, comparatorColumn));

                        tempList.clear();
                    }
                }
            } finally {
                reader.close();
            }
        }
        return files;
    }
    public static File sortAndSave(List<String> tmpList, CSVComparator sortByComparatorColumn) throws IOException  {

        Collections.sort(tmpList, sortByComparatorColumn);

        File newtmpfile = File.createTempFile("sortAndSave" , "flatFile.csv");

        newtmpfile.deleteOnExit();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newtmpfile));
        try {
            for(String r : tmpList) {
                bufferedWriter.write(r);
                bufferedWriter.newLine();
            }
        } finally {
            bufferedWriter.close();
        }
        return newtmpfile;
    }

    public static void mergeSortedFiles(List<File> files, File outputfile, final Comparator<String> cmp) throws IOException {
        PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<BinaryFileBuffer>(11,
                new Comparator<BinaryFileBuffer>() {
                    public int compare(BinaryFileBuffer i, BinaryFileBuffer j) {
                        return cmp.compare(i.peek(), j.peek());
                    }
                }
        );

        for (File f : files) {
            BinaryFileBuffer binaryFileBuffer = new BinaryFileBuffer(f);
            pq.add(binaryFileBuffer);
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputfile));

        try {
            while(pq.size()>0) {

                BinaryFileBuffer binaryFileBuffer = pq.poll();

                String r = binaryFileBuffer.pop();

                bufferedWriter.write(r);

                bufferedWriter.newLine();

                if(binaryFileBuffer.empty()) {

                    binaryFileBuffer.bufferedReader.close();
                    binaryFileBuffer.originalfile.delete();// we don't need you anymore

                } else {
                    pq.add(binaryFileBuffer); // add it back
                }
            }
        } finally {
            bufferedWriter.close();
            for(BinaryFileBuffer binaryFileBuffer : pq ) binaryFileBuffer.close();
        }
    }


    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\vpetrov\\Desktop\\csv\\CSV_Data_2023_1_26 16_5.csv";
        String finalFile = "C:\\Users\\vpetrov\\Desktop\\csv\\combined_large.csv";

        File combinedFile = new File(finalFile);
        CSVComparator cmp = new CSVComparator("first_name");
        List<File> file = splitFile(new File(filePath), cmp) ;
        mergeSortedFiles(file, combinedFile ,cmp);

    }
}
