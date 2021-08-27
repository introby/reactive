package by.intro.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String from;
    private String interaction;
    private long index;

    public Message(String from, String interaction) {
        this.from = from;
        this.interaction = interaction;
        this.index = 0;
    }

}
