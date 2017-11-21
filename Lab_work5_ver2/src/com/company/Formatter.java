package com.company;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class Formatter {

    private static ArrayList<Container> cache = new ArrayList<Container>();

    private String path;
    private Container container;

    public Formatter(String path) {

        if (path == null) {
            throw new NullPointerException("Nonexistent path!");
        }

        this.path = path;
        for (Container container : cache) {
            if (path.equals(container.path))
            {
                 this.container = container;
                 break;
            }
        }

        if (this.container == null)
        {
            container = new Container(path);
            cache.add(container);
        }
    }

    public int countLetters() {
        if (container.countLetters == -1) {

            int count = 0;
            HashMap<Character, Integer> statistic = container.refreshStatistic();
            for (Character symbol : statistic.keySet()) {
                if (Character.isLetter(symbol)) {
                    count += statistic.get(symbol);
                }
            }

            container.countLetters = count;
        }
        else {
            container.refreshStatistic();
        }

        return container.countLetters;
    }



    public static String build(String formatString, Object... arguments) {

        if (formatString == null || arguments == null) {
            throw new NullPointerException();
        } else if (formatString.equals("") || arguments.length == 0) {
            return formatString;
        }


        StringBuilder builder = new StringBuilder(formatString);

        int currentPosition = 0;
        while (true) {

            int openIndex = builder.indexOf("{", currentPosition);
            int closeIndex = builder.indexOf("}", openIndex);

            if (openIndex < 0 && closeIndex < 0) {
                break;
            }

            String match = builder.substring(openIndex + 1, closeIndex);
            try {
                int count = Integer.parseInt(match);

                String insert = "";
                if ( null != arguments[count]) {
                    insert= String.valueOf(arguments[count]);
                }

                builder.replace(openIndex, closeIndex + 1, insert);

                currentPosition += (insert.length() - 2);

            } catch (NumberFormatException exception) {
                currentPosition = closeIndex;

            } catch (ArrayIndexOutOfBoundsException exception) {
                throw new ArrayIndexOutOfBoundsException("Out of range");
            }
        }

        return builder.toString();
    }

    private class Container {

        public final String path;
        public long timestamp;

        public HashMap<Character, Integer> statistic;
        public int countLetters;

        private final File file;

        public Container (String path) {

            this.path = path;
            this.file = new File(path);
            this.timestamp = file.lastModified();

            try {
                String type = Files.probeContentType(file.toPath());
                if (!"text/plain".equals(type))
                {
                    throw new IllegalArgumentException("Incorrect file type!");
                }
            }

            catch (IOException exception)
            {
                throw new IllegalArgumentException("Can't check file extension", exception);
            }

            this.statistic = null;
            this.countLetters = -1;
        }

        public boolean isModified () {
            File file = new File(path);
            return timestamp != file.lastModified();
        }

        public HashMap<Character, Integer> refreshStatistic() {

            if (null == statistic) {
                analyze();
            }

            else if (isModified()) {
                statistic.clear();
                statistic = null;
                analyze();
            }

            return statistic;
        }

        private void analyze(){

            this.statistic = new HashMap<Character, Integer>();

            try {
                FileInputStream stream = new FileInputStream(path);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while ((line = reader.readLine()) != null) {
                    countCharactersTo(this.statistic, line);
                }
                reader.close();
            }
            catch (FileNotFoundException notFound) {
                throw new IllegalArgumentException("File not found", notFound);
            }
            catch (IOException exception) {
                throw new IllegalArgumentException("Problem with reading file's content!", exception);
            }
        }

        private void countCharactersTo(HashMap<Character, Integer> statistic, String line) {

            StringBuilder builder = new StringBuilder(line);
            builder.append('\n');

            for (Character symbol : builder.toString().toCharArray()) {

                Integer count = statistic.get(symbol);
                if (count != null) {
                    statistic.put(symbol, count + 1);
                }
                else {
                    statistic.put(symbol, 1);
                }
            }
        }

    }

}
