package be.alexandre01.dreamzon.network.client;

import be.alexandre01.dreamzon.network.spigot.server.ReadData;
import be.alexandre01.dreamzon.network.utils.message.Message;
import io.netty.channel.ChannelHandlerContext;

public interface IClient {
    /**
     * Client interface
     */
    //

    /**
     * Authentification
     */
    public void auth();

    /**
     * Read the data Client to Server(s)
     * @param data
     * @param server
     */
    public void readData(Message data, String server, ChannelHandlerContext ctx);
    /**
     * Send data Client to Server(s)
     * @param data
     */
    public void sendData(Message data);
}
