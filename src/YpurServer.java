import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class YourServer extends YourServiceGrpc.YourServiceImplBase {

    @Override
    public void yourMethod(YourRequest request, StreamObserver<YourResponse> responseObserver) {
        // Process the request and generate a response
        String responseData = "Response from Server: " + request.getData();
        YourResponse response = YourResponse.newBuilder()
                .setData(responseData)
                .build();

        // Send the response to the client
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public static void main(String[] args) throws Exception {
        // Create the server
        Server server = ServerBuilder.forPort(50051)
                .addService(new YourServer())
                .build();

        // Start the server
        server.start();

        // Wait for the server to terminate
        server.awaitTermination();
    }
}
