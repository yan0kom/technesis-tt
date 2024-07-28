package technesis.regulator;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RegulatorTest {

    @Test
    void reservedBitRequried() {
        var reg = RegulatorFactory.createInstance();
        var err = reg.adjustTemp((byte) 0, 0, null, 0);
        assertEquals(Regulator.errReservedBitMissed, err);
        RegulatorFactory.releaseInstance();
    }

    @Test
    void getAfterInit() {
        var reg = RegulatorFactory.createInstance();

        var out = new ArrayList<Float>(5);
        var err = reg.adjustTemp((byte) (Regulator.getCmd | (5 << 1) | Regulator.reservedBit), 0, out, 0);
        assertEquals(0, err);
        assertEquals(1, out.size());

        out.clear();
        err = reg.adjustTemp((byte) (Regulator.getCmd | (5 << 1) | Regulator.reservedBit), 0, out, 10);
        assertEquals(0, err);
        assertEquals(1, out.size());

        RegulatorFactory.releaseInstance();
    }

    @Test
    void setAndGetTemp() {
        var reg = RegulatorFactory.createInstance();

        var out = new ArrayList<Float>(5);
        var err = reg.adjustTemp(
                (byte) (Regulator.setCmd | Regulator.getCmd | (5 << 1) | Regulator.reservedBit),
                30.0f, out, 0);
        assertEquals(0, err);
        assertTrue(out.size() > 3);
        assertEquals(30.0f, out.get(out.size()-1));

        out.clear();
        err = reg.adjustTemp(
                (byte) (Regulator.clearCmd | Regulator.setCmd | Regulator.getCmd | (2 << 1) | Regulator.reservedBit),
                -15.0f, out, 0);
        assertEquals(0, err);
        assertEquals(2, out.size());
        assertTrue(out.get(out.size()-2) < 0);
        assertEquals(-15.0f, out.get(out.size()-1));

        out.clear();
        err = reg.adjustTemp((byte) (Regulator.getCmd | (3 << 1) | Regulator.reservedBit), 0, out, 1);
        assertEquals(0, err);
        assertEquals(3, out.size());
        assertTrue(out.get(out.size()-1) > -15.0f);

        RegulatorFactory.releaseInstance();
    }

    @Test
    void setAndClear() {
        var reg = RegulatorFactory.createInstance();

        var err = reg.adjustTemp((byte) (Regulator.setCmd | Regulator.reservedBit), 20.0f, null, 0);
        assertEquals(0, err);

        err = reg.adjustTemp((byte) (Regulator.clearCmd | Regulator.reservedBit), 0, null, 0);
        assertEquals(0, err);

        var out = new ArrayList<Float>(10);
        err = reg.adjustTemp((byte) (Regulator.getCmd | (10 << 1) | Regulator.reservedBit), 0, out, 0);
        assertEquals(0, err);
        assertEquals(1, out.size());
        assertEquals(0, out.get(out.size()-1));

        RegulatorFactory.releaseInstance();
    }
}
