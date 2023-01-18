package org.example;


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVsorting {
    public static int splitFile(String filePath, int numberOfRowsInSigleFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        int currNumberOfRows = 0;

        List<List<String>> files = new ArrayList<>();
        List<String> currFile = new ArrayList<>();

        String line;
        try{
            while((line = reader.readLine()) != null){

                line = reader.readLine();
                currFile.add(line);
                currNumberOfRows++;

                if(currNumberOfRows == numberOfRowsInSigleFile) {
                    Collections.sort(currFile);
                    files.add(currFile);
                    currNumberOfRows = 0;
                    currFile = new ArrayList<>();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        reader.close();

        for(int i = 0; i < files.size(); i++){
            File file = new File(filePath + "-" + i + ".csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(String s : files.get(i)){
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        }
        return files.size();
    }

    public static void combineFiles(String filePath, int numChunks, String fieldName) throws IOException {
        List<String> lines = new ArrayList<>();

        for(int i = 0; i < numChunks; i++) {

            BufferedReader reader = new BufferedReader(new FileReader(filePath + "-" + i + ".csv"));

            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();

        }

        Collections.sort(lines, new CSVComparator(fieldName));

        File outputFile = new File(filePath + "combined.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        for (String sortedLine : lines){
            writer.write(sortedLine);
            writer.newLine();
        }
        writer.close();
    }


    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\vpetrov\\Desktop\\csv\\MOCK_DATA.csv";

        //Max rows in exel.
        int maxRowsInOneFile = 1048575;

        String sortBy = "age";

        int numChunks = splitFile(filePath, maxRowsInOneFile);

        combineFiles(filePath,numChunks,sortBy);

    }
}
