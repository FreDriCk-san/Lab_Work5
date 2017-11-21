package classes;

public class Formatter {

    public static String build(String formatString, Object... arguments) {

        if (formatString == null || arguments == null) {
            throw new NullPointerException();
        } else if (formatString.equals("") || arguments.length == 0) {
            return formatString;
        }


        StringBuilder builder = new StringBuilder(formatString);

        int currentPosition = 0;
        while (true) {

            int openIndex = builder.indexOf("{", currentPosition);
            int closeIndex = builder.indexOf("}", openIndex);

            if (openIndex < 0 && closeIndex < 0) {
                break;
            }

            String match = builder.substring(openIndex + 1, closeIndex);
            try {
                int count = Integer.parseInt(match);

                String insert = "";
                if ( null != arguments[count]) {
                    insert= String.valueOf(arguments[count]);
                }

                builder.replace(openIndex, closeIndex + 1, insert);

                currentPosition += (insert.length() - 2);

            } catch (NumberFormatException exception) {
                currentPosition = closeIndex;

            } catch (ArrayIndexOutOfBoundsException exception) {
                throw exception;
            }
        }

        return builder.toString();
    }
}
