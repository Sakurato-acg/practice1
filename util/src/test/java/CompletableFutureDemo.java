import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建一个CompletableFuture实例
        CompletableFuture<String> futurePrice = CompletableFuture.supplyAsync(() -> {
            // 模拟耗时操作，比如调用外部API
            simulateDelay();
            return "100元";
        });
        String price = futurePrice.get();
        // 当结果准备好后，获取它
        System.out.println("价格是：" + price);

        // 在这里，咱们可以做一些其他的事情，不必等待价格查询的结果
        doSomethingElse();

    }

    private static void simulateDelay() {
        try {
            Thread.sleep(4000); // 模拟1秒的延迟
        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
        }
    }

    private static void doSomethingElse() {
        // 做一些其他的事情
        System.out.println("小黑在做其他的事情...");
    }
}