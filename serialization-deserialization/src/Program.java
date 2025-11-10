import java.io.*;

public class Program {

    public static void main(String[] args) {
        Person person = new Person("Alice");
        
        SerializeToFile(person);
        
        DeserializeFromFile("person.ser");
    }
    
    public static void SerializeToFile(Person p) {
    
    	System.out.println("Serializing: " + p);
    	
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("person.ser"))) {
            out.writeObject(p);
            System.out.println("Object serialized to person.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Person DeserializeFromFile(String filename) {
    	
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("person.ser"))) {
            Person p = (Person) in.readObject();
            System.out.println("Deserialized: " + p);
            return p;
		} catch (Exception e) {
        	e.printStackTrace();
        }
		return null;
    }
}