import java.text.ListFormat;
import java.util.List;

public class ScriptPartner {
    public void methode() {
        System.out.println("ScriptPartner#methode");

        var daltons = List.of("Joe", "William", "Jack", "Averall");
        System.out.println(ListFormat.getInstance().format(daltons));
    }
}
