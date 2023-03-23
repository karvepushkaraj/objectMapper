import mapper.JsonMapper;
import model.Address;
import model.Employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Application {

    public static void main(String[] args) throws IOException {
        List<String> contactNumbers = List.of("124-729","134-293");
        Address address = new Address("161 S Sunnyvale Ave","Sunnyvale","California",94086);
        Employee employee = new Employee(728,"Michael","Scott",36,contactNumbers,address);
        String destinationJson = Files.readString(Path.of("src/main/resources/destination.json"));
        JsonMapper<Employee> employeeJsonMapper = new JsonMapper<>(employee, destinationJson);
        String mappedJson = employeeJsonMapper.getMappedJson();
        System.out.println(mappedJson);
    }
}
