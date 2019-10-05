package server;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import logs.*;

public class JSONConverter {
    public Command decodeCommand(String jsonString){
        ObjectMapper objectMapper = new ObjectMapper();
        Command command = null;
        try {
            command = objectMapper.readValue(jsonString,Command.class);
        } catch (JsonProcessingException e) {
            Log.LOG_JSON_CONVERTER.error("Coding of command interrupted with error: " + e.getMessage());
        }
        return command;
    }

    public String codeCommand(Command command){
        ObjectMapper mapper = new ObjectMapper();
        String result=null;
        try {
            result = mapper.writeValueAsString(command);
        } catch (JsonProcessingException e) {
            Log.LOG_JSON_CONVERTER.error("Decoding of command interrupted with error: " + e.getMessage());
        }
        return result;
    }

}
