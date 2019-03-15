package md.leonis.assistant.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import md.leonis.assistant.domain.User;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ConfigHolder {

    private User user = new User(UUID.randomUUID(), "Leonis", "email", "password", 0L, 3000L, 200L, 30L, 50L, 2L);

}
