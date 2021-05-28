package com.atlassian.jsm.spikes.jsonstreaming;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.concurrent.Callable;

public class Utils {
    private static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %ciB", value / 1024.0, ci.current());
    }

    public static void withMemoryProfiling(Callable<Integer> runnable) throws Exception {
        long initialTotalMemory = Runtime.getRuntime().totalMemory();
        long initialFreeMemory = Runtime.getRuntime().freeMemory();

        long start = System.currentTimeMillis();
        System.gc();
        Integer count = runnable.call();
        long timeTaken = System.currentTimeMillis() - start;

        long finalTotalMemory = Runtime.getRuntime().totalMemory();
        long finalFreeMemory = Runtime.getRuntime().freeMemory();

        long usedMemory = (finalTotalMemory - finalFreeMemory) - (initialTotalMemory - initialFreeMemory);

        System.out.println("\nObject count: " + count);
        System.out.println("\nTime taken: " + timeTaken + " milliseconds");

        System.out.println("\n\n---- Memory stats ----");
        System.out.println("\t\t\t" + "Free\t\t\tTotal");
        System.out.println("Before\t\t\t" + humanReadableByteCountBin(initialFreeMemory) + "\t\t\t" + humanReadableByteCountBin(initialTotalMemory));
        System.out.println("After\t\t\t" + humanReadableByteCountBin(finalFreeMemory) + "\t\t\t" + humanReadableByteCountBin(finalTotalMemory));
        System.out.println("\nTotal used " + humanReadableByteCountBin(usedMemory));
    }
}
