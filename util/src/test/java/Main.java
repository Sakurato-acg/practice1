import lombok.extern.slf4j.Slf4j;
import org.example.thread.AsyncUtil;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class Main {

    public static void main(String[] args) {
        List<? extends Serializable> execute = AsyncUtil.execute(() -> 1, () -> "A", () -> true);
        execute.forEach(item -> log.info(item.toString()));

    }
}
