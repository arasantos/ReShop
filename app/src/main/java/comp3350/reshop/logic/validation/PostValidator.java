package comp3350.reshop.logic.validation;

import comp3350.reshop.logic.EnumUtils;
import comp3350.reshop.logic.exceptions.EmptyInputException;
import comp3350.reshop.logic.exceptions.InvalidChoiceException;
import comp3350.reshop.logic.exceptions.InvalidInputException;
import comp3350.reshop.logic.exceptions.InvalidInputFormatException;


public final class PostValidator {
    static final int MAX_PRICE = 100000;
    private PostValidator() {
    }

    public static void validateDescription(String desc) throws InvalidInputException {
        if (desc.trim().isEmpty()) {
            throw new EmptyInputException("Description must not be empty.");
        }

        if (desc.length() > 500) {
            throw new InvalidInputFormatException("Description must be 500 characters or less.");
        }
    }

    public static void validateItemName(String name) throws InvalidInputException {
        if (name.trim().isEmpty()) {
            throw new EmptyInputException("Item name must not be empty.");
        }

        if (name.length() > 70) {
            throw new InvalidInputFormatException("Item name must be 70 characters or less.");
        }
    }

    public static void validatePrice(int price) throws InvalidInputException {
        if (price == 0) {
            throw new EmptyInputException("Price must not be 0.");
        }

        if (price < 0) {
            throw new InvalidInputFormatException("Price must not be negative.");
        }

        if (price > MAX_PRICE) {
            throw new InvalidInputFormatException("Price must not exceed "+MAX_PRICE/100.00+".");
        }
    }

    public static void validateImageUriString(String imageUriString) throws InvalidInputException {
        if (imageUriString == null || imageUriString.trim().isEmpty()) {
            throw new EmptyInputException("Image must not be empty.");
        }
    }

    public static <T extends Enum<T>> void validateChoice(String choice, String choiceType, T[] enumValues) throws InvalidInputException {
        if (choice.trim().isEmpty()) {
            throw new EmptyInputException(String.format("%s must not be empty.", choiceType));
        }

        if (EnumUtils.isMissingFromEnums(choice, enumValues)) {
            throw new InvalidChoiceException(String.format("%s must be one of the valid choices.", choiceType));
        }
    }
}
