package de.groygroy.linuxmagazin.java22;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Arrays;

public class ForeignFunctionAndMemory {

    public static void main(String[] args) {
        // hier kommt man einen handle für die radixsort Funktion
        Linker linker          = Linker.nativeLinker();
        SymbolLookup stdlib    = linker.defaultLookup();
        MemorySegment strlen = stdlib.find("strlen").orElseThrow();
        MethodHandle handle = linker.downcallHandle(
                stdlib.find("strlen").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS));

        String string ="LinuxMagazin";

        try (Arena offHeap = Arena.ofConfined()) {
            // Anfragen von 'C' Speicherplatz
            MemorySegment pointer = offHeap.allocateFrom( string);
            System.out.println("string in memory '"+ pointer.getString(0) + "'");
            // Aufruf der Funktion
            long l1 =(long) handle.invoke( pointer);
            System.out.println("länge " + l1);
            // mit Jextract
            long l2 = string_h.strlen(pointer);
            System.out.println("länge " + l2);

        } catch (Throwable exp) {
            System.err.println("da ging was schief:"+ exp);
        }



    }
}
