package org.example;

import java.io.*;

public class BinaryFileBuffer {
    public static int BUFFERSIZE = 2048;
    public BufferedReader bufferedReader;
    public File originalfile;
    private String cache;
    private boolean empty;

    public BinaryFileBuffer(File f) throws IOException {
        originalfile = f;
        bufferedReader = new BufferedReader(new FileReader(f), BUFFERSIZE);
        reload();
    }

    public boolean empty() {
        return empty;
    }

    private void reload() throws IOException {
        try {
            if((this.cache = bufferedReader.readLine()) == null){
                empty = true;
                cache = null;
            }
            else{
                empty = false;
            }
        } catch(EOFException oef) {
            empty = true;
            cache = null;
        }
    }

    public void close() throws IOException {
        bufferedReader.close();
    }


    public String peek() {
        if(empty()) return null;
        return cache.toString();
    }
    public String pop() throws IOException {
        String answer = peek();
        reload();
        return answer;
    }
}
