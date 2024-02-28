// Keine Klasse

// ohne static, ohne String[] Signatur
void main() {
    System.out.println(gruesse());
    new ScriptPartner().methode();
}

public String gruesse() {
    return "Hallo " + System.getProperty("user.name");
}
