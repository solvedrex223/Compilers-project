package edu.scratchpp.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jodd.io.StreamGobbler;

@RestController
public class Controller {

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/compile", produces = MediaType.TEXT_PLAIN_VALUE)
    public String compileCode(@RequestBody String code) {
        try {
            File out_file = new File("Code.java");
            if (out_file.exists()) {
                out_file.delete();
                out_file.createNewFile();
            }
            FileWriter fw = new FileWriter(out_file);
            fw.write(code);
            fw.close();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating java file");
        }
        try {
            ProcessBuilder pBuilder = new ProcessBuilder("sh", "-c", "javac Code.java");
            Process process = pBuilder.start();
            StringBuilder error_message = new StringBuilder();
            StreamGobbler sg = new StreamGobbler(process.getInputStream());
            sg.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                error_message.append(line + "\n");
            }
            int error = process.waitFor();
            if (error != 0) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error_message.toString());
            }
            ProcessBuilder pBuilder2 = new ProcessBuilder("sh", "-c", "java Code.java");
            Process process2 = pBuilder2.start();
            StringBuilder output = new StringBuilder();
            StreamGobbler sg2 = new StreamGobbler(process2.getErrorStream());
            sg2.start();
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
            String line2;
            while ((line2 = reader2.readLine()) != null) {
                output.append(line2 + "\n");
            }
            process2.waitFor();
            return output.toString();
        } catch (InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with the compilation process");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error starting java compiler");
        }
    }
}