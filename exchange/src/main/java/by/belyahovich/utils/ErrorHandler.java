package by.belyahovich.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorHandler {

    public static void sendError(int status, String message, HttpServletResponse response) {
        try {
            response.sendError(status, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
