import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class LoadBalancerClient {

    public static void main(String[] args) {
        // Create the gRPC channel with load balancing
        ManagedChannel channel = ManagedChannelBuilder.forTarget("loadbalancer")
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();

        // Create a gRPC client stub
        YourServiceGrpc.YourServiceStub stub = YourServiceGrpc.newStub(channel);

        // Make a gRPC request
        YourRequest request = YourRequest.newBuilder()
                .setData("Hello, Server!")
                .build();

        stub.yourMethod(request, new StreamObserver<YourResponse>() {
            @Override
            public void onNext(YourResponse response) {
                // Handle the response
                System.out.println("Response received: " + response.getData());
            }

            @Override
            public void onError(Throwable throwable) {
                // Handle errors
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // Request completed
            }
        });

        // Shutdown the channel gracefully
        channel.shutdown();
    }
}

