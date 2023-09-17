import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException {
        // Задание: CSV в JSON
        File csv = new File("data.csv");
        csv.createNewFile();
        var employ = "1,John,Smith,USA,25".split(",");
        var employ2 = "2,Ivan,Petrov,RU,23".split(",");

        FileWriter fw = new FileWriter(csv);
        CSVWriter cw = new CSVWriter(fw);

        cw.writeNext(employ);
        cw.writeNext(employ2);
        cw.close();

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> list = parseCSV(columnMapping, "data.csv");

        String json = listToJson(list);
        writeString(json);

    }
    private static List<Employee> parseCSV(String[] strategy, String fileName) {
        List<Employee> employes = null;
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strat =
                    new ColumnPositionMappingStrategy<>();
            strat.setType(Employee.class);
            strat.setColumnMapping(strategy);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strat)
                    .build();
            employes = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employes;
    }

    private static String listToJson(List<?> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>(){}.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static String writeString(String json) throws IOException {
        new File("data.json").createNewFile();
        StringBuilder sb = new StringBuilder();
        sb.append(json);
        FileWriter fw = new FileWriter("data.json");
        fw.write(sb.toString());
        fw.flush();
        return sb.toString();
    }
}