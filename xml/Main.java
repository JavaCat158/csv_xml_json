import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        // Задание: XML в JSON
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        Element staff = document.createElement("staff");
        document.appendChild(staff);

        Element employee1 = document.createElement("employee");
        staff.appendChild(employee1);

        Element id = document.createElement("id");
        id.appendChild(document.createTextNode("1"));
        employee1.appendChild(id);

        Element firstName = document.createElement("firstName");
        firstName.appendChild(document.createTextNode("John"));
        employee1.appendChild(firstName);

        Element lastName = document.createElement("lastName");
        lastName.appendChild(document.createTextNode("Smith"));
        employee1.appendChild(lastName);

        Element country = document.createElement("country");
        country.appendChild(document.createTextNode("USA"));
        employee1.appendChild(country);

        Element age = document.createElement("age");
        age.appendChild(document.createTextNode("25"));
        employee1.appendChild(age);
        //----------------------------------------------------//
        Element employee2 = document.createElement("employee");
        staff.appendChild(employee2);

        Element id2 = document.createElement("id");
        id2.appendChild(document.createTextNode("2"));
        employee2.appendChild(id2);

        Element firstName2 = document.createElement("firstName");
        firstName2.appendChild(document.createTextNode("Ivan"));
        employee2.appendChild(firstName2);

        Element lastName2 = document.createElement("lastName");
        lastName2.appendChild(document.createTextNode("Petrov"));
        employee2.appendChild(lastName2);

        Element country2 = document.createElement("country");
        country2.appendChild(document.createTextNode("RU"));
        employee2.appendChild(country2);

        Element age2 = document.createElement("age");
        age2.appendChild(document.createTextNode("23"));
        employee2.appendChild(age2);

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File("data.xml"));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            transformer.transform(domSource, streamResult);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        List<Employee> list = parseXML("data.xml");
        String str = listToJson(list);
        writeString(str);

    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        File newFile = new File(fileName);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document document = documentBuilder.parse(newFile);

        NodeList nodeList = document.getDocumentElement().getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                long id = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                String country = element.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                Employee emp = new Employee(id, firstName, lastName, country, age);
                list.add(emp);
            }
        }
        System.out.println(list);
        return list;
    }

    private static String listToJson(List<?> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static String writeString(String json) throws IOException {
        new File("data2.json").createNewFile();
        StringBuilder sb = new StringBuilder();
        sb.append(json);
        FileWriter fw = new FileWriter("data2.json");
        fw.write(sb.toString());
        fw.flush();
        fw.close();
        return sb.toString();
    }
}
