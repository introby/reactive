package by.intro.server.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Table(value = "VW")
public class Car {

    @Id
    private Long id;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    private int count;
}
