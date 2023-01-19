package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVsorting {

    public static void writeInFile(String filePath, List<List<String>> files){

        for(int i = 0; i < files.size(); i++){
            File file = new File(filePath + "-" + i + ".csv");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String s : files.get(i)) {
                    writer.write(s);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

        public static int splitFile(String filePath, int numberOfRowsInSingleFile) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            int currNumberOfRows = 0;

            List<List<String>> files = new ArrayList<>();
            List<String> currFile = new ArrayList<>();

            String line = "";
            String header = reader.readLine();
            String nextLine = "";

            try{
                while((line = reader.readLine()) != null){

                    nextLine = reader.readLine();
                    currFile.add(line);
                    currNumberOfRows++;

                    if(nextLine != null) {
                        currFile.add(nextLine);
                        currNumberOfRows++;
                    }
                    if(currNumberOfRows == numberOfRowsInSingleFile || nextLine == null) {

                        files.add(currFile);
                        currNumberOfRows = 0;
                        currFile = new ArrayList<>();

                    }

                }
                if(!currFile.isEmpty()){
                    files.add(currFile);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            finally {
            reader.close();
            }

            writeInFile(filePath,files);

            return files.size();
        }

        public static void sortFiles(String filePath, int chunks) throws IOException {
            List<List<String>> files = new ArrayList<>();
            List<String> currFile = new ArrayList<>();

            for(int i = 0; i < chunks; i++){

                BufferedReader reader = new BufferedReader(new FileReader(filePath + "-" + i + ".csv"));
                String line = "";
                while((line = reader.readLine()) != null){
                    currFile.add(line);
                }
                reader.close();
                Collections.sort(currFile);
                files.add(currFile);
                currFile = new ArrayList<>();
            }

            writeInFile(filePath,files);
        }

    public static void combineFiles(String filePath, int numChunks, String fieldName) throws IOException {
        List<String> lines = new ArrayList<>();

        for(int i = 0; i < numChunks; i++) {

            BufferedReader reader = new BufferedReader(new FileReader(filePath + "-" + i + ".csv"));

            String line = "";
            while((line = reader.readLine())!= null) {
                lines.add(line);
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

        int maxRowsInOneFile = 300;

        String sortBy = "age";

        int numChunks = splitFile(filePath, maxRowsInOneFile);

        sortFiles(filePath,numChunks);

        combineFiles(filePath,numChunks,sortBy);

    }
}
