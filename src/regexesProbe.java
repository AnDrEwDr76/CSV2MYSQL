/*
* Левый класс, на пробу
* */
public class regexesProbe {
    public static void main(String [] args) {
        String lexema = "3.23";
        if (lexema.matches("^[-+]?[0-9]*[\\.][0-9]+")) {
            System.out.println("yes");
        } else {
            System.out.println("no");
        }

    }
}
