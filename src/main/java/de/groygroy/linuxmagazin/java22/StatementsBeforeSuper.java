package de.groygroy.linuxmagazin.java22;

public class StatementsBeforeSuper {

    private int x;
    private int y;
    public StatementsBeforeSuper( int x, int y) {
        if ( x < 1 || y < 1) {
            throw new IllegalArgumentException("positive x und y Werte erwartet" );
        }
        this.x = x;
        this.y = y;
    }

    // vor Java 22
    // public StatementsBeforeSuper(String sval) {
    //  this( parse(sval,0), parse(sval,1));
    // }

    // mit Java 22
    public StatementsBeforeSuper(String sval) {
        String[] split = sval.split(" ");
        int x = Integer.parseInt(split[0]);
        int y = Integer.parseInt(split[1]);
        // kein Zugriff auf Membervariablen und -methoden !
        this( x, y);
    }

    private static int parse(String sval, int pos) {
        String[] split = sval.split(" ");
        return Integer.parseInt(split[pos]);
    }
}
