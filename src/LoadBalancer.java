import io.grpc.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LoadBalancer extends LoadBalancerProvider {

    private List<EquivalentAddressGroup> backendServers;

    public LoadBalancer(List<String> serverAddresses) {
        backendServers = new ArrayList<>();

        for (String address : serverAddresses) {
            String[] parts = address.split(":");
            backendServers.add(new EquivalentAddressGroup(new InetSocketAddress(parts[0], Integer.parseInt(parts[1]))));
        }
    }

    @Override
    public LoadBalancer build() {
        return this;
    }

    @Override
    public String getPolicyName() {
        return "round_robin";
    }

    @Override
    public void handleResolvedAddresses(ResolvedAddresses resolvedAddresses) {
        // No-op
    }

    @Override
    public void handleNameResolutionError(Status error) {
        // No-op
    }

    @Override
    public void shutdown() {
        // No-op
    }

    @Override
    public Subchannel createSubchannel(CreateSubchannelArgs args) {
        return new Subchannel() {
            @Override
            public void start(Listener listener) {
                listener.onSubchannelState(
                        SubchannelState.newBuilder().setAddresses(backendServers).build());
            }

            @Override
            public void requestConnection() {
                // No-op
            }

            @Override
            public void shutdown() {
                // No-op
            }
        };
    }

    @Override
    public Factory getFactory() {
        return new Factory() {
            @Override
            public LoadBalancerProvider newProvider() {
                return LoadBalancer.this;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            @Override
            public int getPriority() {
                return 5;
            }
        };
    }
}
