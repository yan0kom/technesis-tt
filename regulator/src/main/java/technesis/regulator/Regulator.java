package technesis.regulator;

import java.util.List;

public interface Regulator {
    byte reservedBit = (byte) 0b00000001;
    byte clearCmd    = (byte) 0b10000000;
    byte setCmd      = (byte) 0b01000000;
    byte getCmd      = (byte) 0b00100000;
    byte getSizeMask = (byte) 0b00011110;

    int errReservedBitMissed = 1;
    int errTempTooHigh = 2;
    int errTempTooLow = 3;

    /**
     * Метод для взаимодействия с регулятором
     * @param operation операция в битовом поле
     * @param inData новое значение температуры
     * @param outData выходной список предыдущих значений температуры (дополняется, обязателен для операции чтения)
     * @param offsetOut количество пропускаемых последних значений при записи в выходной список
     * @return 0 при успешном вызове, при возникновении ошибки ее код
     */
    int adjustTemp(byte operation, float inData, List<Float> outData, int offsetOut);
}
