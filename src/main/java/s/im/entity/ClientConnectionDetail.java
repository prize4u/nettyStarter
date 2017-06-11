package s.im.entity;


import s.im.connection.client.api.ClientConnectionChannel;

import java.util.Date;

/**
 * Created by za-zhujun on 2017/5/21.
 */
public class ClientConnectionDetail extends HostConnectionDetail {
    private final ClientConnectionChannel client;

    public ClientConnectionDetail(AddressInfo srcHost, AddressInfo destHost, ClientConnectionChannel client, Date connDate) {
        super(srcHost, destHost, connDate);
        this.client = client;
    }

    public ClientConnectionChannel getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = super.equals(o);
        if (!equals) {
            return false;
        }

        ClientConnectionDetail conn = (ClientConnectionDetail) o;
        return client.getKeyIdentifier().equals(conn.client.getKeyIdentifier());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 37 * result + client.getKeyIdentifier().hashCode();
        return result;
    }
}
