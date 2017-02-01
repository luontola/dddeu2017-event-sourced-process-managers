package dddeu2017.espm.util;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

import java.util.UUID;

public class Util {

    private static final TimeBasedGenerator uuid1 = Generators.timeBasedGenerator(EthernetAddress.fromInterface());

    public static UUID newUUID() {
        return uuid1.generate();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
