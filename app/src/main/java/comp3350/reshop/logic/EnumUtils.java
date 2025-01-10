package comp3350.reshop.logic;

public final class EnumUtils {
    private EnumUtils() {
        //prevents instantiation
    }

    public static <T extends Enum<T>> boolean isMissingFromEnums(String valueToFind, T[] enumValues) {
        boolean isMissing = true;

        for (int i = 0; i < enumValues.length && isMissing; i++) {
            String currentEnumValue = enumValues[i].toString();

            if (valueToFind.equals(currentEnumValue)) {
                isMissing = false;
            }
        }

        return isMissing;
    }
}
